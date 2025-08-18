package com.eduardo.webappclass.adapter.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val username: String,
    private val token: String,
    private val authorities: List<String>
): AbstractAuthenticationToken(authorities.map { GrantedAuthority { it } }) {
    override fun getCredentials() = token

    override fun getPrincipal() = username
}