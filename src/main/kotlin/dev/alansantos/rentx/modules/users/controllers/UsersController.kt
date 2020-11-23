package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateUserRequestDTO
import dev.alansantos.rentx.modules.users.controllers.dtos.response.UserResponseDTO
import dev.alansantos.rentx.modules.users.services.CreateUsersService
import dev.alansantos.rentx.modules.users.services.RetrieveUserByTokenService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/users"])
@Api(value = "Users Controller",
        description = "Users REST operations")
class UsersController(val createUsersService: CreateUsersService,
                      val retrieveUserByTokenService: RetrieveUserByTokenService) {

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestHeader("Authorization") token: String): UserResponseDTO? {
        val user = retrieveUserByTokenService.execute(token)

        return UserResponseDTO(user)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createUserDTO: CreateUserRequestDTO): UserResponseDTO? {
        val (name, email, password) = createUserDTO

        val user = createUsersService.execute(name, email, password)

        return UserResponseDTO(user)
    }

}