package com.eduardo.webappclass.adapter.controller.dto.util

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValueOfProjectileTypeValidator::class])
annotation class ValueOfProjectileType(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "Valor inválido. Deve ser um dos valores do enum.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

