package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.ProjectileDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.ProjectileMapper
import com.eduardo.webappclass.application.service.ProjectileService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/armory/projectile")
class ProjectileController(
    private val projectileMapper: ProjectileMapper,
    private val projectileService: ProjectileService
) {

    @PostMapping
    fun register(@RequestBody @Valid projectileDto: ProjectileDto) : ResponseEntity<ProjectileDto> {
        val newProjectile = projectileMapper.toProjectile(projectileDto)
        val registeredProjectile = projectileService.aquire(newProjectile)

        return ResponseEntity
            .ok(projectileMapper.toProjectileDto(registeredProjectile))
    }
    @GetMapping
    fun list(): ResponseEntity<List<ProjectileDto>> {
        val projectiles = projectileService.getAll()

        return ResponseEntity.ok(projectileMapper.toProjectileDtoList(projectiles))
    }

    @DeleteMapping("/{id}")
    fun discard(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
         projectileService.discardById(id)

        return ResponseEntity.ok(mapOf("message" to "projectile ${id} deleted"))
    }

}