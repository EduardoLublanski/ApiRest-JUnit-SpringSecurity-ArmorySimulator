package com.eduardo.webappclass.adapter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthenticationManegerConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsServiceImp: UserDetailsServiceImp
) {
    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider(userDetailsServiceImp)
            .apply { setPasswordEncoder(passwordEncoder) }

        return ProviderManager(daoAuthenticationProvider, JwtAuthenticationProvider())
    }
}