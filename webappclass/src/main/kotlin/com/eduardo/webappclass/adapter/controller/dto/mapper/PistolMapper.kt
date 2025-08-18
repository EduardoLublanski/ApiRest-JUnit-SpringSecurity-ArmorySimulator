package com.eduardo.webappclass.adapter.controller.dto.mapper

import com.eduardo.webappclass.adapter.controller.dto.PistolDto
import com.eduardo.webappclass.application.persistence.AmmoClipDataManager
import com.eduardo.webappclass.domain.entity.Pistol
import org.springframework.stereotype.Component

@Component
class PistolMapper(
    val ammoClipDataManager: AmmoClipDataManager
) {

    fun toPistol(pistolDto: PistolDto): Pistol {

        val pistol = Pistol(caliber = pistolDto.caliber, model = pistolDto.model). apply {
            ammoClip = ammoClipDataManager.getByIdOrNull(pistolDto.ammoClipIdProjectilesId?.keys?.firstOrNull() ?: -1)
        }

        return pistol
    }

    fun toPistolDto(pistol: Pistol): PistolDto {
        val pistolAmmoClip = pistol.ammoClip
        val pistolDto = PistolDto(
            id = pistol.id,
            model = pistol.model,
            caliber = pistol.caliber,
            ammoClipIdProjectilesId = mapOf((pistolAmmoClip?.id ?: -1L) to pistolAmmoClip?.stack?.map { it?.id })
        )

        return pistolDto
    }

    fun toPistolList(pistolsDto: List<PistolDto>) = pistolsDto.map {toPistol(it)}
    fun toPistolDtoList(pistols: List<Pistol>) = pistols.map {toPistolDto(it)}

}