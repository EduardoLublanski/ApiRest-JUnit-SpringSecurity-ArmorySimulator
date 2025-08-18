package com.eduardo.webappclass.adapter.persistence.repository.mysql

import com.eduardo.webappclass.domain.entity.AmmoClip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AmmoClipJpaRepository: JpaRepository<AmmoClip, Long>