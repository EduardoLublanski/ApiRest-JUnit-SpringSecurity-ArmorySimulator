package com.eduardo.webappclass.adapter.persistence.mysql

import com.eduardo.webappclass.adapter.persistence.repository.mysql.PistolJpaRepository
import com.eduardo.webappclass.application.persistence.PistolDataManager
import com.eduardo.webappclass.domain.entity.Pistol
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
@Primary
class PistolMysqlJpaRepository(private val pistolRepository: PistolJpaRepository): PistolDataManager {
    override fun register(pistol: Pistol): Pistol {
        return pistolRepository.save(pistol)
    }

    override fun getAll(): List<Pistol> {
        return pistolRepository.findAll()
    }

    override fun getById(id: Long): Optional<Pistol> {
        return pistolRepository.findById(id)
    }

    override fun deleteById(id: Long) {
        return pistolRepository.deleteById(id)
    }

    override fun existsById(id: Long): Boolean {
        return pistolRepository.existsById(id)
    }

    override fun getByIdOrNull(id: Long): Pistol? {
        return pistolRepository.findByIdOrNull(id)
    }
}