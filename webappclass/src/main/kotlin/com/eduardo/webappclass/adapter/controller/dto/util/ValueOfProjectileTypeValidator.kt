package com.eduardo.webappclass.adapter.controller.dto.util

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ValueOfProjectileTypeValidator: ConstraintValidator<ValueOfProjectileType, String> {
    private lateinit var acceptedValues: List<String>

    override fun initialize(annotation: ValueOfProjectileType) {
        acceptedValues = annotation.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || acceptedValues.contains(value)
    }
}
