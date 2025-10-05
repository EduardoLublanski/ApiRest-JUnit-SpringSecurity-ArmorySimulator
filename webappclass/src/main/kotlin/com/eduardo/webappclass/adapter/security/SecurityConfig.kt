package com.eduardo.webappclass.adapter.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val jwtLoginFilter: JwtLoginFilter,
) {

    @Bean
    fun securityFilter(http: HttpSecurity): SecurityFilterChain {
        val baseUrl = "api/v1/armory"
        val shooterUrl = "$baseUrl/shooter"
        val ammoClipUrl = "$baseUrl/ammo-clip"
        val projectileUrl = "$baseUrl/projectile"
        val pistolUrl = "$baseUrl/pistol"

        http
            .formLogin { it.disable() }
            .logout { it.disable() }
            .csrf { it.disable() }
            .headers { it.frameOptions { it.disable() } }

            .authorizeHttpRequests {
                it.requestMatchers(
                    HttpMethod.DELETE,"$shooterUrl/{cpf}").hasRole("ADM")
                it.requestMatchers(
                    HttpMethod.POST,shooterUrl).hasRole("ADM")
                it.requestMatchers(
                    "$shooterUrl/{cpf}/add-role",
                    "$shooterUrl/{cpf}/remove-role",
                    ).hasRole("ADM")
                it.requestMatchers(
                    HttpMethod.GET,
                    "$shooterUrl/{cpf}", shooterUrl
                ).hasAnyRole( "ADM")

                it.requestMatchers(
                    HttpMethod.POST,
                    shooterUrl, projectileUrl, ammoClipUrl, pistolUrl
                ).hasAnyRole("USER", "ADM")


                it.requestMatchers(
                    HttpMethod.DELETE,
                    "$projectileUrl/{id}", "$pistolUrl/{id}", "$ammoClipUrl/{id}"
                ).hasAnyRole("USER", "ADM")

                it.requestMatchers(
                    "$pistolUrl/{id}/load",
                    "$pistolUrl/{id}/unload",
                    "$pistolUrl/{id}/shoot",
                    "$ammoClipUrl/{id}/load",
                    "${ammoClipUrl}/{id}/unload"
                ).hasAnyRole("USER", "ADM")


                it.requestMatchers(HttpMethod.GET, projectileUrl, ammoClipUrl, pistolUrl).hasAnyRole("USER", "ADM", "GUEST")
                it.requestMatchers("/h2-console/**").permitAll()
                it.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

            .exceptionHandling {
                it.accessDeniedHandler { _, response, _ ->
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.contentType = "application/json"
                    response.writer.write("""{"error":"Insufficient authorities"}""")
                }
                it.authenticationEntryPoint { request, response, authException ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = "application/json"
                    response.writer.write("""{"error":"Bad credentials"}""")
                }
            }

        return http.build()
    }
}

