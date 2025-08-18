package com.eduardo.webappclass.adapter.security

import com.eduardo.webappclass.adapter.persistence.repository.mysql.ShooterJpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImp(private val shooterRepository: ShooterJpaRepository): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val shooter = shooterRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException("Username $username not found") }

        return UserDetailsImp(shooter)
    }
}