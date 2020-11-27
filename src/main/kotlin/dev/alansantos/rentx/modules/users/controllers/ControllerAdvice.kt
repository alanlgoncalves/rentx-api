package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.exceptions.AuthenticationException
import dev.alansantos.rentx.shared.dto.AppError
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = [AuthenticationException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(ex: Throwable): AppError {
        return AppError(ex.message)
    }

}