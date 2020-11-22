package dev.alansantos.rentx.config.security.utils

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class TokenAuthenticationUtils {

    private val TOKEN_PREFIX = "Bearer"
    private val HEADER_STRING = "Authorization"

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun getAuthentication(request: HttpServletRequest): Authentication? {
        val token = request.getHeader(HEADER_STRING)

        if (token != null) {
            val userEmail = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .subject

            if (userEmail != null) {
                return UsernamePasswordAuthenticationToken(userEmail, null, Collections.emptyList())
            }

        }

        return null
    }
}