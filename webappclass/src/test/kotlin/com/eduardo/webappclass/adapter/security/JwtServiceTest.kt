package com.eduardo.webappclass.adapter.security

import com.eduardo.webappclass.domain.entity.Shooter
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private lateinit var userDetails: UserDetailsImp
    private lateinit var secretKey: String

    @BeforeEach
    fun setUp(){
        secretKey = """QsXliu88azValvPzvQXtTZ3S+Bvv+EYGfFCSU1g/jLmapyiofslfe5kTwAKdxlMK"""
        jwtService = JwtService(secretKey)
        userDetails = UserDetailsImp(
            Shooter(
                cpf = "24509749834",
                email = "testemail@email.com",
                name = "Test User",
                password = "sjab&$45%814yaswf"
            )
        )
    }

    @Test
    fun `should generate a non-empty token`(){
        val serviceResult = jwtService.generateToken(userDetails)

        assertNotNull(serviceResult)
        assertTrue(serviceResult.isNotBlank())
    }

    @Test
    fun `generated token should contain correct subject`(){
        val token = jwtService.generateToken(userDetails)
        val subject = jwtService.getSubject(token)

        assertEquals(userDetails.username, subject)
    }

    @Test
    fun `getSubject should return the subject with valid token`(){
        val token = jwtService.generateToken(userDetails)
        val subject = jwtService.getSubject(token)

        assertEquals(userDetails.username, subject)
    }

    @Test
    fun `getRoles should return empty list if roles claim is missing`() {
        val now = System.currentTimeMillis()
        val tokenWithoutRoles = io.jsonwebtoken.Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date(now))
            .expiration(Date(now + 10000))
            .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()

        val roles = jwtService.getRoles(tokenWithoutRoles)
        assertTrue(roles.isEmpty())
    }

    @Test
    fun `extractClaims should throw ExpiredJwtException for expired token`() {
        val now = System.currentTimeMillis()
        val expiredToken = io.jsonwebtoken.Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date(now - 10000))
            .setExpiration(Date(now - 5000))
            .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()

        assertThrows(ExpiredJwtException::class.java) {
            jwtService.getSubject(expiredToken)
        }
    }

    @Test
    fun `extractClaims should throw MalformedJwtException for invalid token`() {
        val malformedToken = "this.is.not.a.jwt"
        assertThrows(MalformedJwtException::class.java) {
            jwtService.getSubject(malformedToken)
        }
    }

    @Test
    fun `extractClaims should throw SignatureException for token with wrong secret`() {
        val token = jwtService.generateToken(userDetails)
        val jwtServiceWithWrongSecret = JwtService("QsXliu88azValvPzvQXtTZ3S+Bvv+EYGfFCSU1g/jLmapyiofslfe5kTwAKdxlMKQsXliu88azValvPzvQXtTZ3S+Bvv+EYGfFCSU1g/jLmapyiofslfe5kTwAKdxlMKQsXliu88azValvPzvQXtTZ3S+Bvv+EYGfFCSU1g/jLmapyiofslfe5kTwAKdxlMK")

        assertThrows(SignatureException::class.java) {
            jwtServiceWithWrongSecret.getSubject(token)
        }
    }
}