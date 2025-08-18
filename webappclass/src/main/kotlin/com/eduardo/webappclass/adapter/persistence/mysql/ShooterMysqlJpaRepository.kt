package com.eduardo.webappclass.adapter.persistence.mysql

import com.eduardo.webappclass.adapter.persistence.repository.mysql.ShooterJpaRepository
import com.eduardo.webappclass.application.persistence.ShooterDataManager
import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class ShooterMysqlJpaRepository(private val shooterRepository: ShooterJpaRepository): ShooterDataManager {
    override fun register(newShooter: Shooter): Shooter {
        return shooterRepository.save(newShooter)
    }

    override fun getAll(): List<Shooter> {
        return shooterRepository.findAll()
    }

    override fun getByCpf(cpf: String): Optional<Shooter> {
        return shooterRepository.findById(cpf)
    }

    override fun banByCpf(cpf: String) {
        return shooterRepository.deleteById(cpf)
    }

    override fun getByEmail(email: String): Optional<Shooter> {
        return shooterRepository.findByEmail(email)
    }

    override fun getFirstByRole(role: Role): Shooter? {
        return shooterRepository.findFirstByRolesContaining(role)
    }

    override fun existsByCpf(cpf: String): Boolean {
        return shooterRepository.existsById(cpf)
    }
}