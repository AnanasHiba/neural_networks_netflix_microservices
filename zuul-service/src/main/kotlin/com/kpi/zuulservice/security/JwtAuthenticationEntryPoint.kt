package com.kpi.zuulservice.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoin: AuthenticationEntryPoint {

    @Throws(IOException::class, ServletException::class)
    override fun commence(httpServletRequest: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED")

        val json = String.format("{\"message\": \"%s\"}", e.message)
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(json)
    }
}