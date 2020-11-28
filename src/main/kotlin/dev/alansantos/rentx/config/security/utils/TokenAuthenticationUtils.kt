package dev.alansantos.rentx.config.security.utils

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class TokenAuthenticationUtils(
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

            val authorities = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .get("authorities", List::class.java)
                    .map { SimpleGrantedAuthority(it.toString()) }

            return UsernamePasswordAuthenticationToken(userEmail, null, authorities)
        }

        return null
    }
}