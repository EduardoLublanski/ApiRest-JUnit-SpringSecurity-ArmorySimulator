package com.eduardo.webappclass.adapter.controller

import com.eduardo.webappclass.adapter.controller.dto.ShooterDto
import com.eduardo.webappclass.domain.util.Role
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@Transactional
class ShooterControllerIntegrationTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 private lateinit var testShooterDto: ShooterDto

 @Autowired
 private lateinit var objectMapper: ObjectMapper

 private val baseUrl = "/api/v1/armory/shooter"

 private fun registerShooter(){
  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto)
  }
 }

 @BeforeEach
 fun setUpShooter(){
  testShooterDto = ShooterDto(
   cpf = "04163111808",
   email = "email@email.com",
   name = "User",
   password = "sjab&$45%814yaswf"
  )
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should register a shooter`(){
  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto)
  }
   .andExpect {
    status { isOk() }
    jsonPath("$.cpf") { value(testShooterDto.cpf) }
    jsonPath("$.email") { value(testShooterDto.email) }
    jsonPath("$.name") { value(testShooterDto.name) }
    jsonPath("$.password") { isNotEmpty() }
   }
 }

 @Test
 @WithMockUser(roles = ["USER","GUEST"])
 fun `should return 403 when register with non-adm role`(){
  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto)
  }
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should return lisr of shooters`(){
  mockMvc.get(baseUrl)
   .andExpect {
    status { isOk() }
   }
 }

 @Test
 @WithMockUser(roles = ["USER","GUEST"])
 fun `should return 403 when list with non-adm role`(){
  mockMvc.get(baseUrl)
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should return shooter by cpf`(){
  registerShooter()

  mockMvc.get("$baseUrl/${testShooterDto.cpf}")
   .andExpect {
    status { isOk() }
    jsonPath("$.cpf") { value(testShooterDto.cpf) }
    jsonPath("$.email") { value(testShooterDto.email) }
    jsonPath("$.name") { value(testShooterDto.name) }
    jsonPath("$.password") { isNotEmpty() }
   }
 }

 @Test
 @WithMockUser(roles = ["USER", "GUEST"])
 fun `should return 403 when getShooter with non-adm role`(){
  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto)
  }

  mockMvc.get("$baseUrl/${testShooterDto.cpf}")
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should return delete message`(){
  registerShooter()

  mockMvc.delete("$baseUrl/${testShooterDto.cpf}")
   .andExpect {
    status { isOk() }
    jsonPath("$.message") { value("shooter ${testShooterDto.cpf} banned") }
   }
 }

 @Test
 @WithMockUser(roles = ["USER", "GUEST"])
 fun `should return 403 when delete with non-adm role`(){
  registerShooter()

  mockMvc.delete("$baseUrl/${testShooterDto.cpf}")
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should return updated shooter`(){
  registerShooter()
  val newRole = Role.ROLE_USER

  mockMvc.patch("$baseUrl/${testShooterDto.cpf}/add-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isOk() }
    jsonPath("$.cpf") { value(testShooterDto.cpf) }
    jsonPath("$.email") { value(testShooterDto.email) }
    jsonPath("$.name") { value(testShooterDto.name) }
    jsonPath("$.password") { isNotEmpty() }
    jsonPath("$.roles") { value(hasItem("ROLE_USER")) }

   }
 }

 @Test
 @WithMockUser(roles = ["USER", "GUEST"])
 fun `should return 403 when addRole with non-adm role`(){
  registerShooter()
  val newRole = Role.ROLE_USER

  mockMvc.patch("$baseUrl/${testShooterDto.cpf}/add-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }

 @Test
 @WithMockUser(roles = ["ADM"])
 fun `should return updated shooter without the role`() {
  val newRole = Role.ROLE_USER

  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto.apply { roles.addFirst(newRole) })
  }

   mockMvc.patch("$baseUrl/${testShooterDto.cpf}/remove-role") {
    contentType = MediaType.APPLICATION_JSON
    content = objectMapper.writeValueAsString(mapOf("role" to newRole))
   }
    .andExpect {
     status { isOk() }
     jsonPath("$.cpf") { value(testShooterDto.cpf) }
     jsonPath("$.email") { value(testShooterDto.email) }
     jsonPath("$.name") { value(testShooterDto.name) }
     jsonPath("$.password") { isNotEmpty() }
     jsonPath("$.roles") { value(not(hasItem("ROLE_USER"))) }
    }
 }

 @Test
 @WithMockUser(roles = ["USER", "GUEST"])
 fun `should return 403 when removeRole with non-adm role`(){
  val newRole = Role.ROLE_USER

  mockMvc.post(baseUrl) {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(testShooterDto.apply { roles.addFirst(newRole) })
  }

  mockMvc.patch("$baseUrl/${testShooterDto.cpf}/remove-role") {
   contentType = MediaType.APPLICATION_JSON
   content = objectMapper.writeValueAsString(mapOf("role" to newRole))
  }
   .andExpect {
    status { isForbidden() }
    jsonPath("$.error") { value("Insufficient authorities") }
   }
 }
}