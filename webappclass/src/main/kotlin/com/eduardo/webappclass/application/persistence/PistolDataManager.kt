package com.eduardo.webappclass.application.persistence

import com.eduardo.webappclass.domain.entity.Pistol
import java.util.Optional

interface PistolDataManager {
    fun register(pistol: Pistol): Pistol
    fun getAll(): List<Pistol>
    fun getById(id: Long): Optional<Pistol>
    fun deleteById(id: Long): Unit
    fun existsById(id: Long): Boolean
    fun getByIdOrNull(id: Long): Pistol?

}