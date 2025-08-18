package com.eduardo.webappclass.adapter.security

import com.eduardo.webappclass.domain.entity.Shooter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserDetailsImp(private val shooter: Shooter): UserDetails {
    override fun getAuthorities() = shooter.roles.map { SimpleGrantedAuthority(it.name) }

    override fun getPassword() = shooter.password

    override fun getUsername() = shooter.email
}