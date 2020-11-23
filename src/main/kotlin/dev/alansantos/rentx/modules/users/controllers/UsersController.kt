package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateUserRequestDTO
import dev.alansantos.rentx.modules.users.controllers.dtos.response.UserResponseDTO
import dev.alansantos.rentx.modules.users.services.CreateUsersService
import dev.alansantos.rentx.modules.users.services.RetrieveUserByTokenService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Validated
@RestController
@RequestMapping(path = ["/api/v1/users"])
@Api(value = "Users endpoints", tags = ["Users REST operations"])
class UsersController(val createUsersService: CreateUsersService,
                      val retrieveUserByTokenService: RetrieveUserByTokenService) {

    @ApiOperation(value = "Get user by token")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Ok"),
        ApiResponse(code = 400, message = "Bad Request"),
        ApiResponse(code = 404, message = "Not found")
    ])
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestHeader("Authorization") token: String): UserResponseDTO? {
        val user = retrieveUserByTokenService.execute(token)

        return UserResponseDTO(user)
    }

    @ApiOperation(value = "Create user")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "User created"),
        ApiResponse(code = 400, message = "Bad Request"),
        ApiResponse(code = 409, message = "User already exists")
    ])
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid createUserDTO: CreateUserRequestDTO): UserResponseDTO? {
        val (name, email, password) = createUserDTO

        val user = createUsersService.execute(name, email, password)

        return UserResponseDTO(user)
    }

}