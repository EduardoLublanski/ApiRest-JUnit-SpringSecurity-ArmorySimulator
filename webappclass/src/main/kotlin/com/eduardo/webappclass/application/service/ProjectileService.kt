package com.eduardo.webappclass.application.service

import com.eduardo.webappclass.application.persistence.ProjectileDataManager
import com.eduardo.webappclass.domain.entity.Projectile
import org.springframework.stereotype.Service

@Service
class ProjectileService(private val projectileDataManager: ProjectileDataManager) {

    fun aquire(projectile: Projectile) = projectileDataManager.register(projectile)
    fun getAll() = projectileDataManager.getAll()
    fun discardById(projectileId: Long) = if(projectileDataManager.existsById(projectileId)) projectileDataManager.deleteById(projectileId)
    else throw NoSuchElementException("projectile $projectileId not found")


}
