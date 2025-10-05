package com.eduardo.webappclass.application.service

import com.eduardo.webappclass.application.persistence.ShooterDataManager
import com.eduardo.webappclass.domain.entity.Shooter
import com.eduardo.webappclass.domain.util.Role
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import java.lang.IllegalArgumentException
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ShooterServiceTest {
 @Mock
 private lateinit var shooterDataManager: ShooterDataManager
 @Mock
 private lateinit var passwordEncoder: PasswordEncoder
 @InjectMocks
 private lateinit var shooterService: ShooterService

 private lateinit var testShooter: Shooter

 @BeforeEach
 fun setUpShooter(){
   testShooter = Shooter(
    cpf = "24509749834",
    email = "email@email.com",
    name = "User",
    password = "sjab&$45%814yaswf"
   )
 }

@Test
fun shouldReturnPasswordEncodedAndShooterRegistered_whenRegisterWithValidShooter() {

  val shooterToBeRegistered = testShooter

  whenever(passwordEncoder.encode(any()))
   .thenReturn("encoded")
  whenever(shooterDataManager.register(any()))
   .thenReturn(shooterToBeRegistered)

 val registeredShooter = shooterService.register(shooterToBeRegistered)

 assertEquals("encoded", registeredShooter.password)

 }

@Test
 fun shouldReturnAllShooters_whenGetAll() {
  whenever(shooterDataManager.getAll()).thenReturn(listOf(testShooter))

  val shooterList = shooterService.getAll()

 assertTrue(shooterList.containsAll(listOf(testShooter)))
 }

@Test
 fun shouldReturnShooter_whenGetByCpfWithValidCpf() {
  whenever(shooterDataManager.getByCpf(testShooter.cpf))
   .thenReturn(Optional.of(testShooter))

 val shooterResult = shooterService.getByCpf(testShooter.cpf)

 assertEquals(testShooter.cpf, shooterResult.cpf)
 }
 @Test
 fun shouldThrowIllegalArgumentException_whenGetByCpfWithInvalidCpf() {
  whenever(shooterDataManager.getByCpf("fake cpf"))
   .thenReturn(Optional.empty())

  val ex = assertThrows(IllegalArgumentException::class.java) { shooterService.getByCpf("fake cpf") }
  assertTrue(
   ex.message == "shooter fake cpf not found",
   "incompatible error message\n" +
           "expected: shooter fake cpf not found\n" +
           "found: ${ex.message}")
 }

@Test
 fun shouldBanShooter_whenBanByCpfWithValidCpf() {
  val shooterCpf = testShooter.cpf

   whenever(shooterDataManager.existsByCpf(shooterCpf))
    .thenReturn(true)

  shooterService.banByCpf(shooterCpf)

  verify(shooterDataManager).banByCpf(shooterCpf)
 }
 @Test
 fun shouldThrowNoSuchElementException_whenBanByCpfWithInvalidCpf() {
  val nonExistentShooterCpf = testShooter.cpf

  whenever(shooterDataManager.existsByCpf(nonExistentShooterCpf))
   .thenReturn(false)

  val error = assertThrows(NoSuchElementException::class.java) { shooterService.banByCpf(nonExistentShooterCpf) }

  assertTrue(
   error.message == "shooter $nonExistentShooterCpf not found",
   "incompatible error message\n" +
           "expected: shooter $nonExistentShooterCpf not found\n" +
           "found: ${error.message}"
   )
 }

@Test
 fun shouldAddNewRole_whenAddRoleToShooterByCpfWithValidRoleAndValidCpf() {
  val shooterToUpdate = testShooter
  val newRole = Role.ROLE_USER

  whenever(shooterDataManager.getByCpf(shooterToUpdate.cpf))
   .thenReturn(Optional.of(testShooter))

 val serviceResult = shooterService.addRoleToShooterByCpf(testShooter.cpf, newRole)

 assertTrue(serviceResult.roles.contains(newRole))
}
 @Test
 fun shouldThrowIllegalArgumentException_whenAddRoleToShooterByCpfWithInvalidCpf() {
  val nonExistentShooterCpf = testShooter.cpf
  val newRole = Role.ROLE_USER

  whenever(shooterDataManager.getByCpf(nonExistentShooterCpf))
   .thenReturn(Optional.empty())

  val serviceResult = assertThrows(IllegalArgumentException::class.java) { shooterService.addRoleToShooterByCpf(nonExistentShooterCpf, newRole) }

  assertTrue(
   serviceResult.message == "shooter $nonExistentShooterCpf not found",
   "incompatible error message\n" +
           "expected: shooter $nonExistentShooterCpf not found\n" +
           "found: ${serviceResult.message}"
  )
 }
 @Test
 fun shouldThrowIllegalArgumentException_whenAddRoleToShooterByCpfWithDuplicatedRole(){
  val newRole = Role.ROLE_USER
  val shooterToUpdate = testShooter.apply {roles.add(newRole)}

  whenever(shooterDataManager.getByCpf(shooterToUpdate.cpf)).
  thenReturn(Optional.of(shooterToUpdate))

  val serviceResult = assertThrows(IllegalArgumentException::class.java)
  { shooterService.addRoleToShooterByCpf(shooterToUpdate.cpf, newRole) }

  assertTrue(
   serviceResult.message == "shooter ${shooterToUpdate.cpf} already has the role $newRole",
   "incompatible error message\n" +
           "expected: shooter ${shooterToUpdate.cpf} already has the role $newRole\n" +
           "found: ${serviceResult.message}"
  )
 }

@Test
 fun shouldRemoveRole_whenRemoveRoleFromShooterByCpfWithValidCpfAndValidRole() {
   val roleToRemove = Role.ROLE_USER
   val shooterWithRole = testShooter.apply {roles.add(roleToRemove)}

   whenever(shooterDataManager.getByCpf(shooterWithRole.cpf)).
           thenReturn(Optional.of(shooterWithRole))

   val shooterWithRoleRemoved = shooterService.removeRoleFromShooterByCpf(shooterWithRole.cpf, roleToRemove)

   assertFalse(shooterWithRoleRemoved.roles.contains(roleToRemove))
 }
 @Test
 fun shouldThrowIllegalArgumentException_whenRemoveRoleFromShooterByCpfWithInvalidCpf(){
  val roleToRemove = Role.ROLE_USER
  val shooterWithRole = testShooter

  whenever(shooterDataManager.getByCpf(any())).thenReturn(Optional.empty())

  val serviceResult = assertThrows(IllegalArgumentException::class.java) { shooterService.removeRoleFromShooterByCpf(shooterWithRole.cpf, roleToRemove) }

  assertTrue(
   serviceResult.message == "shooter ${shooterWithRole.cpf} not found",
   "incompatible error message\n" +
           "expected: shooter ${shooterWithRole.cpf} not found\n" +
           "found: ${serviceResult.message}"
  )
 }
 @Test
 fun shouldThrowIllegalArgumentException_whenRemoveRoleFromShooterByCpfWithAlreadyMissingRole() {
  val roleToRemove = Role.ROLE_USER
  val shooterWithoutRole = testShooter

  whenever(shooterDataManager.getByCpf(shooterWithoutRole.cpf)).
  thenReturn(Optional.of(shooterWithoutRole))

  val serviceResult = assertThrows(IllegalArgumentException::class.java) { shooterService.removeRoleFromShooterByCpf(shooterWithoutRole.cpf, roleToRemove) }

  assertTrue(
   serviceResult.message == "shooter ${shooterWithoutRole.cpf} already hasn't the role $roleToRemove",
   "incompatible error message\n" +
           "shooter ${shooterWithoutRole.cpf} already hasn't the role $roleToRemove\n" +
           "found: ${serviceResult.message}"
  )
 }
}