package com.eduardo.webappclass.adapter.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImp,
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

        try {
            val subject = jwtService.getSubject(token)
            val userDetails = userDetailsService.loadUserByUsername(subject)
            val roles = jwtService.getRoles(token)
            val jwtAuthenticationToken = JwtAuthenticationToken(userDetails.username, token, roles)
            val authentication = authenticationManager.authenticate(jwtAuthenticationToken)

            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)
        } catch(exception: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"error":"Invalid or expired token","message":"${exception.message}"}""")
        }
    }


}


