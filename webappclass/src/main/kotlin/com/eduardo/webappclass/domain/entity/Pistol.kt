package com.eduardo.webappclass.domain.entity

import com.eduardo.webappclass.domain.exception.AmmoClipException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Pistol() {

    @Id @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var model = ""

    @Column(nullable = false)
    var caliber: Double = 0.0

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "ammo_clip_id", foreignKey = ForeignKey(name = "fk_pistol_ammo_clip"))
    var ammoClip: AmmoClip? = null

    constructor(model: String, caliber: Double): this() {
        this.model = model
        this.caliber = caliber
    }

    fun load(ammoClip: AmmoClip) {
        when {
            this.ammoClip == null -> this.ammoClip = ammoClip
            this.caliber != ammoClip.caliber -> throw AmmoClipException("pistol's caliber ${this.caliber} is incompatible with ammo clip's caliber ${ammoClip.caliber}")
                else -> throw AmmoClipException("unload the ${this.id} ${this.model} before inserting ${ammoClip.id} ammo clip")
        }
    }


    fun unload(): AmmoClip? {
        val ammoClip = this.ammoClip
        if(this.ammoClip == null) throw AmmoClipException("pistol ${this.id} ${this.model} is already unloaded")
        else this.ammoClip = null

        return ammoClip
    }
}