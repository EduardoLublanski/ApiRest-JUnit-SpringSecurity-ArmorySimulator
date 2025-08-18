package com.eduardo.webappclass.adapter.controller.dto.mapper

import com.eduardo.webappclass.adapter.controller.dto.AmmoClipDto
import com.eduardo.webappclass.application.persistence.AmmoClipDataManager
import com.eduardo.webappclass.application.persistence.PistolDataManager
import com.eduardo.webappclass.application.persistence.ProjectileDataManager
import com.eduardo.webappclass.domain.entity.AmmoClip
import org.springframework.stereotype.Component

@Component
class AmmoClipMapper(
    val ammoClipDataManager: AmmoClipDataManager,
    val pistolDataManager: PistolDataManager,
    val projectileDataManager: ProjectileDataManager
) {

    fun toAmmoClip(ammoClipDto: AmmoClipDto): AmmoClip {

//        val ammoClipIfExists = ammoClipDataManager.getById(ammoClipDto.id ?: -1)
//            .orElseThrow { EntityNotFoundException("ammo clip ${ammoClipDto.id} not found") }

        val ammoClip = AmmoClip(caliber = ammoClipDto.caliber, rounds = ammoClipDto.rounds)
            .apply {
                pistol = pistolDataManager.getByIdOrNull(ammoClipDto.pistolId ?: -1)
                projectiles = ammoClipDto.projectilesId.map { projectileDataManager.getByIdOrNull(it ?: -1) }.toMutableList()

            }

        return ammoClip
    }

    fun toAmmoClipDto(ammoClip: AmmoClip): AmmoClipDto {

        val ammoClipDto = AmmoClipDto(
            id = ammoClip.id,
            caliber = ammoClip.caliber,
            rounds = ammoClip.rounds,
            pistolId = ammoClip.pistol?.id,
            projectilesId = ammoClip.stack.map { it?.id }.toMutableList()

        )

        return ammoClipDto
    }

    fun toAmmoClipList(ammoClipsDto: List<AmmoClipDto>) = ammoClipsDto.map { toAmmoClip(it) }
    fun toAmmoClipDtoList(ammoClips: List<AmmoClip>) = ammoClips.map { toAmmoClipDto(it) }


}