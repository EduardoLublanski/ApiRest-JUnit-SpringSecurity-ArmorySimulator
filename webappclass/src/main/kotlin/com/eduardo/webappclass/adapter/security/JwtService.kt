package com.eduardo.webappclass.adapter.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${spring.jwt.secretKey}")
    private val secretKey: String
) {

    fun generateToken(userdetails: UserDetails): String {
        val now = System.currentTimeMillis()
        val roles = userdetails.authorities.map { it.toString() }

        return Jwts
            .builder()
            .subject(userdetails.username)
            .claim("roles", roles)
            .issuedAt(Date(now))
            .expiration(Date(now + 1000 * 60 * 60))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
    }

    private fun extractClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
    }
    public fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        return getExpirationDate(token).after(Date(System.currentTimeMillis())) && getSubject(token) == userDetails.username
    }
    public fun getSubject(token: String) = extractClaims(token).subject
    private fun getExpirationDate(token: String) = extractClaims(token).expiration
    private fun getIssuedDate(token: String) = extractClaims(token).issuedAt

    public fun getRoles(token: String): List<String> {
        val roleClaims = extractClaims(token)["roles"]

        return if(roleClaims is List<*>) {
            roleClaims.filterIsInstance<String>()
        }
        else emptyList()
    }
}