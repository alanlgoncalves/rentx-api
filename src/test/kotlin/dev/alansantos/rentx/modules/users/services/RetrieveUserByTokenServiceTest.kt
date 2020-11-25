package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultJwtParser
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.util.*

class RetrieveUserByTokenServiceTest : StringSpec({

    val usersGateway: UsersGateway = mockk()

    val retrieveUserByTokenService = RetrieveUserByTokenService(usersGateway, "rentx")

    beforeTest {

    }

    "should retrieve user by token with success" {
        // GIVEN
        val email = "john.doe@test.com"
        val token = "Bearer token_example"
        val userMock: User = mockk()

        val defaultJwtParser: DefaultJwtParser = mockk()
        val jws: Jws<Claims> = mockk()
        val claim: Claims = mockk()

        mockkStatic(Jwts::class)

        every { Jwts.parser() } returns defaultJwtParser
        every { defaultJwtParser.setSigningKey("rentx") } returns defaultJwtParser
        every {
            defaultJwtParser.parseClaimsJws(" token_example")
        } returns jws
        every { jws.body } returns claim
        every { claim.subject } returns email

        every { usersGateway.findUserByEmail(email) } returns Optional.of(userMock)

        // WHEN
        val user = retrieveUserByTokenService.execute(token)

        // THEN
        user shouldBeSameInstanceAs user
        verify { usersGateway.findUserByEmail(email) }
    }

})
