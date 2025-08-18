package com.eduardo.webappclass.application.service

import com.eduardo.webappclass.application.persistence.ShooterDataManager
import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ShooterService(
    private val shooterDataManager: ShooterDataManager,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(newShooter: Shooter): Shooter {
        newShooter.password = passwordEncoder.encode(newShooter.password)

        return shooterDataManager.register(newShooter)
    }
    fun getAll() = shooterDataManager.getAll()

    fun getByCpf(cpf: String) = shooterDataManager.getByCpf(cpf)
        .orElseThrow { IllegalArgumentException("shooter $cpf not found") }

    fun banByCpf(cpf: String) {
        if(shooterDataManager.existsByCpf(cpf)) shooterDataManager.banByCpf(cpf)
        else throw NoSuchElementException("shooter $cpf not found")
    }

    @Transactional
    fun addRoleToShooterByCpf(cpf: String, newRole: Role): Shooter {
        val shooter = getByCpfOrThrowsError(cpf)

        if(shooter.roles.contains(newRole)) throw IllegalArgumentException("shooter $cpf already has the role $newRole")
        else shooter.roles.add(newRole)

        return shooter
    }

    @Transactional
    fun removeRoleFromShooterByCpf(cpf: String, role: Role): Shooter {
        val shooter = getByCpfOrThrowsError(cpf)

        if(shooter.roles.contains(role)) shooter.roles.remove(role)
        else throw IllegalArgumentException("shooter $cpf hasn't the role $role")

        return shooter
    }
    private fun getByCpfOrThrowsError(cpf: String): Shooter {
        return shooterDataManager.getByCpf(cpf)
            .orElseThrow { IllegalArgumentException("shooter $cpf not found") }
    }
}
