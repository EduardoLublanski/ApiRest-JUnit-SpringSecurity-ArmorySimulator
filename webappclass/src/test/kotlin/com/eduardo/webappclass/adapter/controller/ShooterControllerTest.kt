package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.ShooterDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.ShooterMapper
import com.eduardo.webappclass.adapter.security.JwtService
import com.eduardo.webappclass.application.service.ShooterService
import com.eduardo.webappclass.domain.entity.Shooter
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@WebMvcTest(ShooterController::class)
@AutoConfigureMockMvc(addFilters = false)
class ShooterControllerTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 @MockitoBean
 private lateinit var shooterService: ShooterService

 @MockitoBean
 private lateinit var shooterMapper: ShooterMapper

 private lateinit var testShooter: Shooter

 private lateinit var testShooterDto: ShooterDto

 @Autowired
 private lateinit var objectMapper: ObjectMapper

 @MockitoBean
 private lateinit var jwtService: JwtService

 @MockitoBean
 private lateinit var authenticationManager: AuthenticationManager

 private val baseUrl = "/api/v1/armory/shooter"
 @BeforeEach
 fun setUpShooter(){
  testShooter = Shooter(
   cpf = "32958616843",
   email = "email@email.com",
   name = "User",
   password = "sjab&$45%814yaswf"
  )

  testShooterDto = ShooterDto(
   cpf = testShooter.cpf,
   email = testShooter.email,
   name = testShooter.name,
   password = testShooter.password
  )
 }

@Test
 fun register() {
  whenever(shooterMapper.toShooter(any())).thenReturn(testShooter)
  whenever(shooterService.register(any())).thenReturn(testShooter)
  whenever(shooterMapper.toShooterDto(any())).thenReturn(testShooterDto)

 mockMvc.post(baseUrl) {
  contentType = MediaType.APPLICATION_JSON
  content = objectMapper.writeValueAsString(testShooterDto)
 }
  .andExpect {
    status { isOk() }
    content { json(objectMapper.writeValueAsString(testShooterDto)) }
  }

 }

@Test
 fun list() {}

@Test
 fun getShooter() {}

@Test
 fun delete() {}

@Test
 fun addRole() {}

@Test
 fun removeRole() {}
}