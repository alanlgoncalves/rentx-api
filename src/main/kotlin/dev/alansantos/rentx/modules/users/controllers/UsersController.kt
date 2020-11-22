package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateUserRequestDTO
import dev.alansantos.rentx.modules.users.controllers.dtos.response.CreateUserResponseDTO
import dev.alansantos.rentx.modules.users.services.CreateUsersService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/users"])
class UsersController(val createUsersService: CreateUsersService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createUserDTO: CreateUserRequestDTO): CreateUserResponseDTO? {

        val (name, email, password) = createUserDTO

        val user = createUsersService.execute(name, email, password)

        return CreateUserResponseDTO(user)
    }

}