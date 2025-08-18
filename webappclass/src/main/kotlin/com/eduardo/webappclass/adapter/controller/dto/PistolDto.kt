package com.eduardo.webappclass.adapter.controller.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PistolDto(

    val id: Long?,

    @field:NotBlank(message = "model can't be blank")
    @field:NotEmpty(message = "model can't be empty")
    @field:NotNull(message = "model must be given")
    val model: String,

    @field:Positive(message = "caliber must be a positive value") val caliber: Double,

    val ammoClipIdProjectilesId: Map<Long, List<Long?>?> = mapOf()
)