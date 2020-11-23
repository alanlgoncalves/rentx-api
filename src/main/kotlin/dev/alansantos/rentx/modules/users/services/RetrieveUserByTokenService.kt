package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.naming.AuthenticationException


@Service
class RetrieveUserByTokenService(
        val usersGateway: UsersGateway,
        @Value("\${jwt.secret}") private var secret: String,
) {
    companion object {
        const val TOKEN_PREFIX = "Bearer"
    }

    fun execute(token: String): User {
        val email: String = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .body
                .subject

        val userOptional = usersGateway.findUserByEmail(email)

        if (!userOptional.isPresent) {
            throw AuthenticationException("Invalid user token")
        }

        return userOptional.get()
    }

}