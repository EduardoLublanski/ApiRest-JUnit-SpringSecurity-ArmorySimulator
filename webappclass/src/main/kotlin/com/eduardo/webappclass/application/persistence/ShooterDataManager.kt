package com.eduardo.webappclass.application.persistence

import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import java.util.Optional

interface ShooterDataManager {
    fun register(newShooter: Shooter): Shooter
    fun getAll(): List<Shooter>
    fun getByCpf(cpf: String): Optional<Shooter>
    fun banByCpf(cpf: String): Unit
    fun getByEmail(email: String): Optional<Shooter>
    fun getFirstByRole(role: Role): Shooter?
    fun existsByCpf(cpf: String): Boolean

}