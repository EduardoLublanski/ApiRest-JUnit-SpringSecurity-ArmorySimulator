package com.eduardo.webappclass

import com.eduardo.webappclass.application.persistence.ShooterDataManager
import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val shooterDataManager: ShooterDataManager,
    private val passwordEncoder: PasswordEncoder,
): CommandLineRunner {

    override fun run(vararg args: String?) {
        val admin = shooterDataManager.getFirstByRole(Role.ROLE_ADM)
        if (admin == null){
            shooterDataManager.register(
                Shooter(
                    name = "Eduardo Lublanski",
                    cpf = "32958616843",
                    email = "lublanskiedu@gmail.com",
                    password = passwordEncoder.encode("1421278")
                ).apply { roles.add(Role.ROLE_ADM) }
            )
        }
    }


}