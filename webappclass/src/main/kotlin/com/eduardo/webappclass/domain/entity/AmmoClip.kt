package com.eduardo.webappclass.domain.entity

import com.eduardo.webappclass.domain.exception.AmmoClipException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.OrderColumn
import jakarta.persistence.PostLoad

@Entity
class AmmoClip() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    var caliber: Double = 0.0

    @Column(nullable = false)
    var rounds: Int = 0

    @OneToMany(mappedBy = "ammoClip", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @OrderColumn(name = "position")
    var projectiles = mutableListOf<Projectile?>()

    @OneToOne(mappedBy = "ammoClip")
    var pistol: Pistol? = null

    @Transient
    val stack: ArrayDeque<Projectile?> = ArrayDeque()

    constructor(caliber: Double, rounds: Int): this() {
        this.caliber = caliber
        this.rounds = rounds
    }

    fun load(projectile: Projectile) {
        if(projectiles.size == rounds) throw AmmoClipException("ammo clip is full")
            else if(projectile.caliber != this.caliber) throw AmmoClipException("projectile's caliber ${projectile.caliber} incompatible with ammo cli's caliber ${this.caliber} ")
        else {
            projectiles.add(0,projectile)
            projectile.ammoClip = this
            stack.addFirst(projectile)
        }
    }

    fun unload(): Projectile? {
        if (stack.isNotEmpty()) {
            val projectile = stack.removeFirst()
            projectile?.ammoClip = null
            projectiles.remove(projectile)
            return projectile
        } else throw AmmoClipException("ammo clip is empty")
    }


    fun listStack() = stack.toList()

    @PostLoad
    private fun asyncStackAfterLoad(){
        stack.clear()
        projectiles.forEach { stack.addLast(it) }

        repeat(stack.count { it == null }) { stack.addLast(null)}
    }
}