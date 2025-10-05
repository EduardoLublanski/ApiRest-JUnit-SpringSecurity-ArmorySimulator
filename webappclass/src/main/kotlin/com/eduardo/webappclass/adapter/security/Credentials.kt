package com.eduardo.webappclass.adapter.security

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Value

data class Credentials(
    @field: NotEmpty
    @field: NotNull
    @field: NotBlank
    val username: String,
    @field: NotEmpty
    @field: NotNull
    @field: NotBlank
    val password: String
)