package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.AmmoClipDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.AmmoClipMapper
import com.eduardo.webappclass.application.service.AmmoClipService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/armory/ammo-clip")
class AmmoClipController(
    private val ammoClipMapper: AmmoClipMapper,
    private val ammoclipService: AmmoClipService
) {

    @PostMapping
    fun register(@RequestBody @Valid ammoClipDto: AmmoClipDto): ResponseEntity<AmmoClipDto> {
        val ammoClip = ammoclipService.aquire(ammoClipMapper.toAmmoClip(ammoClipDto))

        return ResponseEntity.ok(ammoClipMapper.toAmmoClipDto(ammoClip))
    }


    @GetMapping
    fun list() = ResponseEntity.ok(ammoClipMapper.toAmmoClipDtoList(ammoclipService.getAll()))


    @PatchMapping("{id}/load")
    fun load(
        @RequestParam(required = true) projectileId: Long,
        @PathVariable("id") id: Long
    ): ResponseEntity<AmmoClipDto> {
        val updatedAmmoClip = ammoclipService.loadAmmoClipByIdWithProjectileOfId(id, projectileId)

        return ResponseEntity.ok(ammoClipMapper.toAmmoClipDto(updatedAmmoClip))
    }


    @PatchMapping("{id}/unload")
    fun unload(@PathVariable("id") ammoClipId: Long): ResponseEntity<AmmoClipDto> {
        val updatedAmmoClip = ammoclipService.unloadProjectileFromAmmoClipById(ammoClipId)

        return ResponseEntity.ok(ammoClipMapper.toAmmoClipDto(updatedAmmoClip))
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") ammoClipId: Long): ResponseEntity<Map<String, String>> {
        ammoclipService.discardById(ammoClipId)

        return ResponseEntity.ok(mapOf("message" to "ammo clip $ammoClipId deleted"))
    }
}