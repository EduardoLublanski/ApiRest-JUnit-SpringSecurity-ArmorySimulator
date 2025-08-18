package com.eduardo.webappclass.adapter.security

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/armory/auth")
class AuthController(
   private val authenticationManeger: AuthenticationManager,
   private val jwtService: JwtService,
   private val userDetailsServiceImp: UserDetailsServiceImp
){
    @PostMapping
    fun authenticate(@RequestBody @Validated credentials: Credentials): ResponseEntity<Map<String,String>> {
        val authentication = UsernamePasswordAuthenticationToken(
            credentials.username,
            credentials.password
        )
        authenticationManeger.authenticate(authentication)

       val token = jwtService.generateToken(userDetailsServiceImp.loadUserByUsername(credentials.username))

        return ResponseEntity.ok(mapOf("token" to token))
    }

}