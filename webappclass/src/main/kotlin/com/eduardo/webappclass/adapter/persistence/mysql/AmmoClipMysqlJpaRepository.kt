package com.eduardo.webappclass.adapter.persistence.mysql

import com.eduardo.webappclass.adapter.persistence.repository.mysql.AmmoClipJpaRepository
import com.eduardo.webappclass.application.persistence.AmmoClipDataManager
import com.eduardo.webappclass.domain.entity.AmmoClip
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class AmmoClipMysqlJpaRepository(private val ammoClipRepository: AmmoClipJpaRepository): AmmoClipDataManager {
    override fun register(ammoClip: AmmoClip): AmmoClip {
        return ammoClipRepository.save(ammoClip)
    }

    override fun getAll(): List<AmmoClip> {
        return ammoClipRepository.findAll()
    }

    override fun getById(id: Long): Optional<AmmoClip> {
        return ammoClipRepository.findById(id)
    }

    override fun existsById(id: Long): Boolean {
        return ammoClipRepository.existsById(id)
    }

    override fun deleteById(id: Long) {
        return ammoClipRepository.deleteById(id)
    }

    override fun getByIdOrNull(id: Long): AmmoClip? {
        return ammoClipRepository.findByIdOrNull(id)
    }
}