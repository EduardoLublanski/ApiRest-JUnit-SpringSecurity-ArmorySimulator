package com.eduardo.webappclass.adapter.controller.dto.mapper

import com.eduardo.webappclass.adapter.controller.dto.ShooterDto
import com.eduardo.webappclass.domain.entity.Shooter
import org.springframework.stereotype.Component

@Component
class ShooterMapper() {

    fun toShooter(shooterDto: ShooterDto): Shooter {

        val armoryUser = Shooter(
            cpf = shooterDto.cpf,
            name = shooterDto.name,
            email = shooterDto.email,
            password = shooterDto.password
        ). apply { roles = shooterDto.roles.toMutableList() }

        return armoryUser
    }

    fun toShooterDto(shooter: Shooter): ShooterDto {

        val armoryUserDto = ShooterDto(
            cpf = shooter.cpf,
            name = shooter.name,
            email = shooter.email,
            password = shooter.password,
            roles = shooter.roles.toMutableList()
        )

        return armoryUserDto
    }

    fun toShooterList(armoryUsersDto: List<ShooterDto>) = armoryUsersDto.map { toShooter(it) }
    fun toShooterDtoList(armoryUsers: List<Shooter>) = armoryUsers.map { toShooterDto(it) }

}