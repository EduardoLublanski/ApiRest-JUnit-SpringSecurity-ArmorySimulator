package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.RoleDto
import com.eduardo.webappclass.adapter.controller.dto.ShooterDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.ShooterMapper
import com.eduardo.webappclass.application.service.ShooterService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/armory/shooter")
class ShooterController(
    private val shooterService: ShooterService,
    private val shooterMapper: ShooterMapper
) {

    @PostMapping
    fun register(@RequestBody @Valid shooterDto: ShooterDto): ResponseEntity<ShooterDto> {
        val newShooter = shooterService.register(shooterMapper.toShooter(shooterDto))

        return ResponseEntity.ok(shooterMapper.toShooterDto(newShooter))
    }

    @GetMapping
    fun list(): ResponseEntity<List<ShooterDto>> {
        val shooters = shooterService.getAll()

        return ResponseEntity.ok(shooterMapper.toShooterDtoList(shooters))
    }

    @GetMapping("/{cpf}")
    fun getShooter(@PathVariable("cpf") shooterCpf: String): ResponseEntity<ShooterDto> {
        val shooter = shooterService.getByCpf(shooterCpf)

        return ResponseEntity.ok(shooterMapper.toShooterDto(shooter))
    }

    @DeleteMapping("/{cpf}")
    fun delete(@PathVariable("cpf") shooterCpf: String): ResponseEntity<Map<String, String>> {
        shooterService.banByCpf(shooterCpf)

        return ResponseEntity.ok(mapOf("message" to "shooter $shooterCpf banned"))
    }
    @PatchMapping("/{cpf}/add-role")
    fun addRole(
        @PathVariable("cpf") shooterCpf: String,
        @RequestBody newRole: RoleDto
    ): ResponseEntity<ShooterDto> {
        val updatedRolesShooter = shooterService.addRoleToShooterByCpf(shooterCpf, newRole.value)

        return ResponseEntity.ok(shooterMapper.toShooterDto(updatedRolesShooter))
    }

    @PatchMapping("/{cpf}/remove-role")
    fun removeRole(
        @PathVariable("cpf") shooterCpf: String,
        @RequestBody role: RoleDto
    ): ResponseEntity<ShooterDto> {
        val updatedRolesShooter = shooterService.removeRoleFromShooterByCpf(shooterCpf, role.value)

        return ResponseEntity.ok(shooterMapper.toShooterDto(updatedRolesShooter))
    }
}