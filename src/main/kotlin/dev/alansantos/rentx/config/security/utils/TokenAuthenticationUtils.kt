package dev.alansantos.rentx.config.security.utils

import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class TokenAuthenticationUtils(
        val usersGateway: UsersGateway,
        @Value("\${jwt.secret}") var secret: String
) {

    private val TOKEN_PREFIX = "Bearer"
    private val HEADER_STRING = "Authorization"

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)

        if (token != null) {
            val userEmail = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .subject

            if (userEmail != null) {
                val user = usersGateway.findUserByEmail(userEmail).get()

                val authorities = user.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }.toSet()

                return UsernamePasswordAuthenticationToken(userEmail, null, authorities)
            }
        }

        return null
    }
}