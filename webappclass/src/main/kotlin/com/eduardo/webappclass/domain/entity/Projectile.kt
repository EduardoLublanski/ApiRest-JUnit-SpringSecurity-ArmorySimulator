package com.eduardo.webappclass.domain.entity

import com.eduardo.webappclass.domain.util.ProjectileType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Projectile() {

    @Id
    @GeneratedValue
    var id: Long? = null

    var caliber: Double = 0.0

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: ProjectileType = ProjectileType.MJ

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "ammo_clip_id", nullable = true, updatable = true, foreignKey = ForeignKey(name = "fk_projectile_ammo_clip"))
    var ammoClip: AmmoClip? = null

    constructor(caliber: Double, type: ProjectileType) : this() {
       this.caliber = caliber
       this.type = type
    }

}