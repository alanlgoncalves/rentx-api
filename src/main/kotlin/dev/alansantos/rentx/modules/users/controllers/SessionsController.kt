package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateSessionRequestDTO
import dev.alansantos.rentx.modules.users.controllers.dtos.response.CreateSessionResponseDTO
import dev.alansantos.rentx.modules.users.services.AuthenticateUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Validated
@RestController
@RequestMapping(path = ["/api/v1/sessions"])
@Api(value = "Session endpoints", tags = ["Session REST operations"])
class SessionsController(val authenticateUserService: AuthenticateUserService) {

    @ApiOperation(value = "Create session token")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Created"),
        ApiResponse(code = 400, message = "Bad Request"),
        ApiResponse(code = 401, message = "Invalid email/password combination")
    ])
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid createSessionRequestDTO: CreateSessionRequestDTO): CreateSessionResponseDTO {
        val (email, password) = createSessionRequestDTO

        val session = authenticateUserService.execute(email, password)

        return CreateSessionResponseDTO(session)
    }

}