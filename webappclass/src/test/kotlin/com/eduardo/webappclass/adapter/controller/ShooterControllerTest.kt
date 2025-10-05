package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.ShooterDto
import com.eduardo.webappclass.adapter.controller.dto.mapper.ShooterMapper
import com.eduardo.webappclass.adapter.security.JwtService
import com.eduardo.webappclass.adapter.security.UserDetailsServiceImp
import com.eduardo.webappclass.application.service.ShooterService
import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.*
import java.lang.IllegalArgumentException


@WebMvcTest(ShooterController::class)
@AutoConfigureMockMvc(addFilters = false)
class ShooterControllerTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 @Autowired
 private lateinit var objectMapper: ObjectMapper

 @MockitoBean
 private lateinit var shooterService: ShooterService

 @MockitoBean
 private lateinit var shooterMapper: ShooterMapper

 @MockitoBean
 private lateinit var jwtService: JwtService

 @MockitoBean
 private lateinit var authenticationManager: AuthenticationManager

 @MockitoBean
 private lateinit var userDetailsService: UserDetailsServiceImp

 private lateinit var testShooter: Shooter

 private lateinit var invalidTestShooterDto: ShooterDto

 private lateinit var testShooterDto: ShooterDto

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

  invalidTestShooterDto = ShooterDto(
   cpf = "30967846783",
   email = "testShooterinvalidemail",
   name = "",
   password = ""
  )
 }

@Test
 fun shouldReturnShooterDto_whenShooterIsRegistered() {
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
 fun shouldReturnBadRequest_whenInvalidShooterDataIsRegistered(){

  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(invalidTestShooterDto)
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.status") { value(400) }
    jsonPath("$.message") { value("fields validation error") }
   }

 }

 @Test
 fun shouldReturnBadRequest_whenRegisterWithDuplicatedCpf(){

  whenever(shooterMapper.toShooter(any())).thenReturn(testShooter)
  whenever(shooterService.register(any())).thenThrow(IllegalArgumentException("Shooter ${testShooter.cpf} is already registered"))

  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto)
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.status") { value(400) }
    jsonPath("$.message") { value("Shooter ${testShooter.cpf} is already registered") }
   }

 }

@Test
 fun shouldReturnAnList() {

  whenever(shooterService.getAll()).thenReturn(listOf<Shooter>(testShooter, testShooter))
  whenever(shooterMapper.toShooterDtoList(any())).thenReturn(listOf<ShooterDto>(testShooterDto, testShooterDto))

 mockMvc.get(baseUrl)
  .andExpect {
    status{ isOk() }
    jsonPath("$[0].cpf") { value("${testShooter.cpf}") }
    jsonPath("$[0].email") { value("${testShooter.email}") }
   jsonPath("$[0].name") { value("${testShooter.name}") }

   jsonPath("$[1].cpf") { value("${testShooter.cpf}") }
   jsonPath("$[1].email") { value("${testShooter.email}") }
   jsonPath("$[1].name") { value("${testShooter.name}") }
  }

 }

@Test
 fun shouldReturnShooterDto_whenGetByCpfWithValidCpf() {
 whenever(shooterService.getByCpf(any())).thenReturn(testShooter)
 whenever(shooterMapper.toShooterDto(any())).thenReturn(testShooterDto)

 mockMvc.get("$baseUrl/${testShooter.cpf}")
  .andExpect {
     status { isOk() }
     jsonPath("$.cpf") { value("${testShooter.cpf}") }
     jsonPath("$.name") { value("${testShooter.name}") }
     jsonPath("$.email") { value("${testShooter.email}") }
     jsonPath("$.password") { exists() }
     jsonPath("$.roles") { exists() }
  }

 }

 @Test
 fun shouldReturnBadRequest_whenGetByCpfWithInvalidCpf() {
whenever(shooterService.getByCpf(any())).thenThrow(IllegalArgumentException("shooter ${testShooter.cpf} not found"))

  mockMvc.get("$baseUrl/${testShooter.cpf}")
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} not found") }
   }
 }

@Test
 fun shouldReturnBanMessage_whenBanByCpfWithValidCpf() {

  mockMvc.delete("$baseUrl/${testShooter.cpf}")
   .andExpect {
    status { isOk() }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} banned") }
   }

 }

 @Test
 fun shouldReturnBadRequest_whenBanByCpfWithInvalidCpf() {

  whenever(shooterService.banByCpf(any())).thenThrow(NoSuchElementException("shooter ${testShooter.cpf} not found"))

  mockMvc.delete("$baseUrl/${testShooter.cpf}")
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} not found") }
   }
 }

@Test
 fun shouldReturnUpdatedShooterDto_whenAddRoleToShooterByCpfWithValidRoleAndValidCpf() {
val newRole = Role.ROLE_USER

 whenever(shooterService.addRoleToShooterByCpf(testShooter.cpf, newRole))
  .thenReturn(testShooter.apply { roles.addFirst(newRole) })
 whenever(shooterMapper.toShooterDto(any())).thenReturn(testShooterDto.apply { roles.addFirst(newRole) })

 mockMvc.patch("$baseUrl/${testShooter.cpf}/add-role") {
  contentType = MediaType.APPLICATION_JSON
  content = objectMapper.writeValueAsString(mapOf("role" to "ROLE_USER" ))
 }
  .andExpect {
   status { isOk() }
   jsonPath("$.cpf") { value("${testShooter.cpf}") }
   jsonPath("$.name") { value("${testShooter.name}") }
   jsonPath("$.email") { value("${testShooter.email}") }
   jsonPath("$.password") { exists() }
   jsonPath("$.roles") { value(hasItem("ROLE_USER")) }

  }
 }

 @Test
 fun shouldReturnBadRequest_WhenAddRoleToShooterByCpfWithInvalidCpf() {
  val newRole = Role.ROLE_USER

  whenever(shooterService.addRoleToShooterByCpf(testShooter.cpf, newRole)).thenThrow(IllegalArgumentException("shooter ${testShooter.cpf} not found"))

  mockMvc.patch("$baseUrl/${testShooter.cpf}/add-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} not found") }
   }
 }

 @Test
 fun shouldReturnBadRequest_whenAddRoleToShooterByCpfWithDuplicatedRole(){
  val newRole = Role.ROLE_USER

  whenever(shooterService.addRoleToShooterByCpf(any(), any())).thenThrow(IllegalArgumentException("shooter ${testShooter.cpf} already has the role $newRole"))

  mockMvc.patch("$baseUrl/${testShooter.cpf}/add-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} already has the role $newRole") }
   }
 }

 @Test
 fun shouldReturnBadRequest_whenAddRoleToShooterByCpfWithInvalidRole() {
  val invalidRole = "ROLE_INVALID"
  mockMvc.patch("$baseUrl/${testShooter.cpf}/add-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to invalidRole))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value(containsString("not one of the values accepted for Enum")) }
   }
 }

@Test
 fun shouldReturnUpdatedShooterDto_whenRemoveRoleFromShooterByCpfWithValidCpfAndValidRole() {

 val newRole = Role.ROLE_USER
 testShooter.roles.addFirst(newRole)

 whenever(shooterService.removeRoleFromShooterByCpf(testShooter.cpf, newRole))
  .thenReturn(testShooter.apply { roles.remove(newRole) })
 whenever(shooterMapper.toShooterDto(any())).thenReturn(testShooterDto)

 mockMvc.patch("$baseUrl/${testShooter.cpf}/remove-role") {
  contentType = MediaType.APPLICATION_JSON
  content = objectMapper.writeValueAsString(mapOf("role" to newRole ))
 }
  .andExpect {
   status { isOk() }
   jsonPath("$.cpf") { value("${testShooter.cpf}") }
   jsonPath("$.name") { value("${testShooter.name}") }
   jsonPath("$.email") { value("${testShooter.email}") }
   jsonPath("$.password") { exists() }
   jsonPath("$.roles") { value(not(hasItem("ROLE_USER"))) }

  }

 }

 @Test
 fun shouldReturnBadRequest_whenRemoveRoleFromShooterByCpfWithInvalidCpf() {

  val role = Role.ROLE_USER

  whenever(shooterService.removeRoleFromShooterByCpf(testShooter.cpf, role)).thenThrow(IllegalArgumentException("shooter ${testShooter.cpf} not found"))

  mockMvc.patch("$baseUrl/${testShooter.cpf}/remove-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to role))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} not found") }
   }

 }

 @Test
 fun shouldReturnBadRequest_whenRemoveRoleFromShooterByCpfWithInvalidRole() {

  val invalidRole = "ROLE_INVALID"
  mockMvc.patch("$baseUrl/${testShooter.cpf}/remove-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to invalidRole))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value(containsString("not one of the values accepted for Enum")) }
   }

 }

 @Test
 fun shouldReturnBadRequest_whenRemoveRoleFromShooterByCpfWithDuplicatedRole() {

  val newRole = Role.ROLE_USER

  whenever(shooterService.removeRoleFromShooterByCpf(any(), any())).thenThrow(IllegalArgumentException("shooter ${testShooter.cpf} already hasn't the role $newRole"))

  mockMvc.patch("$baseUrl/${testShooter.cpf}/remove-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isBadRequest() }
    jsonPath("$.error") { value("Bad Request") }
    jsonPath("$.message") { value("shooter ${testShooter.cpf} already hasn't the role $newRole") }
   }

 }
}
