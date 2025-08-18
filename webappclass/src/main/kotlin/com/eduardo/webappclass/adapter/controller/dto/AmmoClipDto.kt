package com.eduardo.webappclass.adapter.controller.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class AmmoClipDto(

    val id: Long?,

    @field:Positive(message = "caliber must be a positive value") val caliber: Double,

    @field:Positive(message = "ammo clip's capacity must be a positive value") val rounds: Int,

    val pistolId: Long?,

    val projectilesId: MutableList<Long?> = mutableListOf()
)

