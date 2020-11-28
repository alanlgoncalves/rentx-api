package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.Role
import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.AuthenticationException
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class AuthenticateUserServiceTest : StringSpec() {

    private val usersGateway: UsersGateway = mockk()

    private val passwordEncode: PasswordEncoder = mockk()

    private val authenticateUserService = AuthenticateUserService(
            usersGateway, passwordEncode, "rentx", 1800)

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)

        clearAllMocks()
    }

    init {
        "should authenticate user with success" {
            // GIVEN
            val email = "john.doe@test.com"
            val password = "123456"
            val userRole = Role(name = "USER", description = "User role")
            val user = User(name = "John Doe", email = email, password = password, roles = setOf(userRole))

            every { usersGateway.findUserByEmail(email) } returns Optional.of(user)
            every { passwordEncode.matches(password, password) } returns true

            //WHEN
            val session = authenticateUserService.execute(email, password)

            // THEN
            verify { usersGateway.findUserByEmail(email) }
            verify { passwordEncode.matches(password, password) }

            user.name shouldBeEqualComparingTo session.user.name
            user.email shouldBeEqualComparingTo session.user.email
            user.password shouldBeEqualComparingTo session.user.password
            setOf("USER") shouldBe session.user.roles.map { it.name }.toSet()
        }

        "should not authenticate not found user" {
            // GIVEN
            val email = "john.doe@test.com"
            val password = "123456"

            every { usersGateway.findUserByEmail(email) } returns Optional.empty()

            // WHEN / THEN
            shouldThrow<AuthenticationException> {
                authenticateUserService.execute(email, password)
            }

            verify { usersGateway.findUserByEmail(email) }
        }

        "should not authenticate user with invalid password" {
            // GIVEN
            val email = "john.doe@test.com"
            val password = "123456"
            val userRole = Role(name = "USER", description = "User role")
            val user = User(name = "John Doe", email = email, password = "12345678", roles = setOf(userRole))

            every { usersGateway.findUserByEmail(email) } returns Optional.of(user)
            every { passwordEncode.matches(password, user.password) } returns false

            // WHEN / THEN
            shouldThrow<AuthenticationException> {
                authenticateUserService.execute(email, password)
            }

            verify { usersGateway.findUserByEmail(email) }
            verify { passwordEncode.matches(password, password) }
        }
    }

}