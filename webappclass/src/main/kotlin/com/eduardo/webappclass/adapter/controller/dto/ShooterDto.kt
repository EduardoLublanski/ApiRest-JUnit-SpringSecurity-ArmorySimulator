package com.eduardo.webappclass.adapter.controller.dto

import com.eduardo.webappclass.domain.util.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF

data class ShooterDto(
    @field:CPF(message = "invalid cpf format") val cpf: String,

    @field:NotBlank(message = "name field can't be blank")
    @field:NotNull(message = "name must be given")
    @field:NotEmpty(message = "name field can't be empty")
    val name: String,

    @field:Email(message = "invalid email format")
    @field:NotBlank(message = "email can't be blank")
    @field:NotNull(message = "email is mandatory")
    @field:NotEmpty(message = "email can't be empty")
    var email: String,

    @field:Size(min = 8, message = "password must contain the min of 8 characters")
    @field:NotNull(message = "password is mandatory")
    @field:NotBlank(message = "password can't be blank")
    @field:NotEmpty(message = "password can't be empty")
    var password: String,

    val roles: MutableList<Role> = mutableListOf()

    )
