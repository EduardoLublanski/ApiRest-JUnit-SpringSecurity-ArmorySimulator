package com.eduardo.webappclass.adapter.persistence.repository.mysql

import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ShooterJpaRepository: JpaRepository<Shooter, String> {
    fun findByEmail(email: String?): Optional<Shooter>
    fun findFirstByRolesContaining(role: Role): Shooter?

}