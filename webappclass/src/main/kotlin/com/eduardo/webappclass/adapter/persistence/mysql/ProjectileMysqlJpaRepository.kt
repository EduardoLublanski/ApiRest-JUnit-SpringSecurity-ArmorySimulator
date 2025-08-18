package com.eduardo.webappclass.adapter.persistence.mysql

import com.eduardo.webappclass.adapter.persistence.repository.mysql.ProjectileJpaRepository
import com.eduardo.webappclass.application.persistence.ProjectileDataManager
import com.eduardo.webappclass.domain.entity.Projectile
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class ProjectileMysqlJpaRepository(private val projectileRepository: ProjectileJpaRepository): ProjectileDataManager {
    override fun register(projectile: Projectile): Projectile {
        return projectileRepository.save(projectile)
    }

    override fun getAll(): List<Projectile> {
        return projectileRepository.findAll()
    }

    override fun getById(id: Long): Optional<Projectile> {
        return projectileRepository.findById(id)
    }

    override fun deleteById(id: Long) {
        projectileRepository.deleteById(id)
    }

    override fun existsById(id: Long): Boolean {
        return projectileRepository.existsById(id)
    }

    override fun getByIdOrNull(id: Long): Projectile? {
        return projectileRepository.findByIdOrNull(id)
    }

}