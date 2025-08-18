package com.eduardo.webappclass.adapter.controller.dto

import com.eduardo.webappclass.domain.util.ProjectileType
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProjectileDto(

    val id: Long?,

    @field:Positive(message = "caliber must be a positive value") val caliber: Double,

    val type: ProjectileType,

    val ammoClipId: Long?
)