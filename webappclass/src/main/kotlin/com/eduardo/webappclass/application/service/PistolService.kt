package com.eduardo.webappclass.application.service

import com.eduardo.webappclass.adapter.persistence.repository.mysql.AmmoClipJpaRepository
import com.eduardo.webappclass.adapter.persistence.repository.mysql.ProjectileJpaRepository
import com.eduardo.webappclass.application.persistence.PistolDataManager
import com.eduardo.webappclass.domain.entity.Pistol
import com.eduardo.webappclass.domain.exception.IncompatiblePistolCaliberException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PistolService(
    private val pistolDataManager: PistolDataManager,
    private val ammoClipRepository: AmmoClipJpaRepository,
    private val projectileRepository: ProjectileJpaRepository
) {
    fun aquire(pistol: Pistol) = pistolDataManager.register(pistol)
    fun getAll() = pistolDataManager.getAll()

    @Transactional
    fun insertAmmoClipById(pistolId: Long, ammoClipId: Long): Pistol {
        val pistol = pistolDataManager.getById(pistolId)
            .orElseThrow { NoSuchElementException("pistol $pistolId not found") }
        val ammoClip = ammoClipRepository.findById(ammoClipId)
            .orElseThrow { NoSuchElementException("ammo clip $ammoClipId not found") }

        if(pistol.caliber == ammoClip.caliber) pistol.load(ammoClip)
        else throw IncompatiblePistolCaliberException("ammo clip ${ammoClip.id}'s caliber ${ammoClip.caliber} is incompatible with pistol ${pistol.id}'s caliber ${pistol.caliber}")

        return pistol
    }

    fun discardById(pistolId: Long) {
        if(pistolDataManager.existsById(pistolId)) pistolDataManager.deleteById(pistolId)
        else throw NoSuchElementException("pistol $pistolId not found")
    }

    @Transactional
    fun unloadPistolById(pistolId: Long): Pistol {
        val pistol = findPistolByIdOrThrowEx(pistolId)

        pistol.unload()

        return pistol
    }

    @Transactional
    fun shootPistolOfId(pistolId: Long): Pistol {
        val pistol = findPistolByIdOrThrowEx(pistolId)
        val shootedProjectile = pistol.ammoClip?.unload()
        projectileRepository.deleteById(shootedProjectile?.id ?: -1)

        return pistol
    }

    private fun findPistolByIdOrThrowEx(pistolId: Long) = pistolDataManager.getById(pistolId)
            .orElseThrow { NoSuchElementException("pistol $pistolId not found") }

}
