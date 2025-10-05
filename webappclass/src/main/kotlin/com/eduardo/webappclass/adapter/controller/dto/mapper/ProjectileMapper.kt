package com.eduardo.webappclass.adapter.controller.dto.mapper

import com.eduardo.webappclass.adapter.controller.dto.ProjectileDto
import com.eduardo.webappclass.application.persistence.AmmoClipDataManager
import com.eduardo.webappclass.application.service.AmmoClipService
import com.eduardo.webappclass.domain.entity.Projectile
import org.springframework.stereotype.Component

@Component
class ProjectileMapper(
    val ammoClipService: AmmoClipService
) {

        fun toProjectile(projectileDto: ProjectileDto): Projectile {

            val projectile = Projectile(caliber = projectileDto.caliber, type = projectileDto.type).apply {

                ammoClip = ammoClipService.getById(projectileDto.ammoClipId ?: -1).orElse(null)

            }
            return projectile
        }

        fun toProjectileDto(projectile: Projectile): ProjectileDto {

            val projectileDto = ProjectileDto(
                id = projectile.id,
                type = projectile.type,
                caliber = projectile.caliber,
                ammoClipId = projectile.ammoClip?.id
            )

            return projectileDto

        }

    fun toProjectileList(projectilesDtos: List<ProjectileDto>): List<Projectile> = projectilesDtos.map {toProjectile(it)}
    fun toProjectileDtoList(projectiles: List<Projectile>) = projectiles.map { toProjectileDto(it) }


}