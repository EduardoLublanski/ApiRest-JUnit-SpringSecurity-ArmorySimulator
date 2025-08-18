package com.eduardo.webappclass.domain.entity

import com.eduardo.webappclass.domain.util.Role
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn

@Entity
class Shooter() {

    @Id
    var cpf = ""

    @Column(nullable = false)
    var name = ""

    @Column(nullable = false, unique = true)
    var email = ""

    @Column(nullable = false)
    var password = ""

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val roles: MutableList<Role> = mutableListOf(Role.ROLE_GUEST)

    constructor(cpf: String, name: String, email: String, password: String): this() {
        this.cpf = cpf
        this.name = name
        this.email = email
        this.password = password
    }
}