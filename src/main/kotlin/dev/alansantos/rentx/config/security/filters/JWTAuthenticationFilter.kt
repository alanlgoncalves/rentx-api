package dev.alansantos.rentx.config.security.filters

import dev.alansantos.rentx.config.security.utils.TokenAuthenticationUtils
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JWTAuthenticationFilter(val tokenAuthenticationUtils: TokenAuthenticationUtils) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        try {
            val authentication = tokenAuthenticationUtils.getAuthentication(request as HttpServletRequest)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: MalformedJwtException) {
            response.sendError(400, "Invalid token!")

            return
        } catch (e: ExpiredJwtException) {
            response.sendError(401, "Expired token!")

            return
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}