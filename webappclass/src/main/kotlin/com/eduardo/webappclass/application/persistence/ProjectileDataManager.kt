package com.eduardo.webappclass.application.persistence

import com.eduardo.webappclass.domain.entity.Projectile
import java.util.Optional

interface ProjectileDataManager {
    fun register(projectile: Projectile): Projectile
    fun getAll(): List<Projectile>
    fun getById(id: Long): Optional<Projectile>
    fun deleteById(id: Long): Unit
    fun existsById(id: Long): Boolean
    fun getByIdOrNull(id: Long): Projectile?
}