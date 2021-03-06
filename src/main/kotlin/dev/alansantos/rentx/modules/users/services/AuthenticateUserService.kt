package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.Session
import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.AuthenticationException
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticateUserService(
        private val usersGateway: UsersGateway,
        private val passwordEncode: PasswordEncoder,
        @Value("\${jwt.secret}") private var secret: String,
        @Value("\${jwt.expiration-time}") private var expirationTime: Long
) {

    fun execute(email: String, password: String): Session {

        val user = getUser(email, password)

        return Session(user, generateToken(user))
    }

    private fun getUser(email: String, password: String): User {
        val userOptional = usersGateway.findUserByEmail(email)

        if (!userOptional.isPresent || !passwordEncode.matches(password, userOptional.get().password)) {
            throw AuthenticationException("Incorrect user email/password combination")
        }

        return userOptional.get()
    }

    private fun generateToken(user: User): String {
        val authorities = user.roles.map { "ROLE_${it.name}" }.toSet()

        return Jwts.builder()
                .setSubject(user.email)
                .claim("authorities", authorities)
                .setExpiration(Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

}