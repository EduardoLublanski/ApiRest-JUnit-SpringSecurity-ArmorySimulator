package com.eduardo.webappclass.application.persistence

import com.eduardo.webappclass.domain.entity.AmmoClip
import java.util.Optional

interface AmmoClipDataManager {
    fun register(ammoClip: AmmoClip): AmmoClip
    fun getAll(): List<AmmoClip>
    fun getById(id: Long): Optional<AmmoClip>
    fun existsById(id: Long): Boolean
    fun deleteById(id: Long): Unit
    fun getByIdOrNull(id: Long): AmmoClip?
}