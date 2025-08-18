package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.PistolDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.PistolMapper
import com.eduardo.webappclass.application.service.PistolService
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
@RequestMapping("api/v1/armory/pistol")
class PistolController(
    private val pistolService: PistolService,
    private val pistolMapper: PistolMapper
) {

    @PostMapping
    fun register(@RequestBody @Valid pistolDto: PistolDto): ResponseEntity<PistolDto> {
        val newPistol = pistolService.aquire(pistolMapper.toPistol(pistolDto))

        return ResponseEntity.ok(pistolMapper.toPistolDto(newPistol))
    }

    @GetMapping
    fun list() = ResponseEntity.ok(pistolMapper.toPistolDtoList(pistolService.getAll()))

    @PatchMapping("{id}/load")
    fun load(@PathVariable("id") pistolId: Long, @RequestParam ammoClipId: Long): ResponseEntity<PistolDto> {
        val loadedPistol = pistolService.insertAmmoClipById(pistolId, ammoClipId)

        return ResponseEntity.ok(pistolMapper.toPistolDto(loadedPistol))
    }

    @PatchMapping("{id}/unload")
    fun unload(@PathVariable("id") pistolId: Long): ResponseEntity<PistolDto> {
        val unloadedPistol = pistolService.unloadPistolById(pistolId)

        return ResponseEntity.ok(pistolMapper.toPistolDto(unloadedPistol))
    }

    @PatchMapping("{id}/shoot")
    fun shoot(@PathVariable("id") pistolId: Long): ResponseEntity<PistolDto> {
        val pistol = pistolService.shootPistolOfId(pistolId)

        return ResponseEntity.ok(pistolMapper.toPistolDto(pistol))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        pistolService.discardById(id)

        return ResponseEntity.ok(mapOf("message" to "pistol $id deleted"))
    }

}