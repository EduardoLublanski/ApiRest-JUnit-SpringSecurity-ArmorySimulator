package com.eduardo.webappclass.adapter.persistence.repository.mysql

import com.eduardo.webappclass.domain.entity.Projectile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectileJpaRepository: JpaRepository<Projectile, Long>