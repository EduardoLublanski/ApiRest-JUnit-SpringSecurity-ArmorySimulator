package com.eduardo.webappclass.application.service

import com.eduardo.webappclass.application.persistence.AmmoClipDataManager
import com.eduardo.webappclass.application.persistence.ProjectileDataManager
import com.eduardo.webappclass.domain.entity.AmmoClip
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AmmoClipService(
    private val ammoClipDataManager: AmmoClipDataManager,
    private val projectileDataManager: ProjectileDataManager
) {

    fun aquire(ammoClip: AmmoClip) = ammoClipDataManager.register(ammoClip)
    fun getAll() = ammoClipDataManager.getAll()

    @Transactional
    fun loadAmmoClipByIdWithProjectileOfId(ammoClipId: Long, projectileId: Long): AmmoClip {
        val projectile = projectileDataManager.getById(projectileId)
            .orElseThrow { NoSuchElementException("projectile $projectileId not found") }

        val ammoClip = findAmmoClipByIdOrThrowEx(ammoClipId)
        ammoClip.load(projectile)

        return ammoClip
    }

    @Transactional
    fun unloadProjectileFromAmmoClipById(ammoClipId: Long): AmmoClip {
        val ammoClip = findAmmoClipByIdOrThrowEx(ammoClipId)
        ammoClip.unload()

        return ammoClip
    }

    fun discardById(ammoClipId: Long) = if(ammoClipDataManager.existsById(ammoClipId)) ammoClipDataManager.deleteById(ammoClipId)
    else throw NoSuchElementException("ammo clip $ammoClipId not found")


    private fun findAmmoClipByIdOrThrowEx(ammoClipId: Long) = ammoClipDataManager.getById(ammoClipId)
        .orElseThrow { NoSuchElementException("ammo clip $ammoClipId not found") }


}
