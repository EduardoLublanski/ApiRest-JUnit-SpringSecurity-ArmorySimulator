package com.eduardo.webappclass.adapter.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val authenticationManeger: AuthenticationManager
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val requestHeader = request.getHeader("Authorization")

        if(requestHeader == null || !requestHeader.startsWith("Bearer ") || SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }

        val token = requestHeader.removePrefix("Bearer ")
        val userDetails = userDetailsService.loadUserByUsername(jwtService.getSubject(token))

        if(!jwtService.isTokenValid(token, userDetails)) throw BadCredentialsException("invalid or expired token")

        val roles = jwtService.getRoles(token)
        val jwtAuthenticationToken = JwtAuthenticationToken(userDetails.username, token, roles)
        val authentication = authenticationManeger.authenticate(jwtAuthenticationToken)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)

    }


}


