package dev.alansantos.rentx.modules.users.controllers

import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateSessionRequestDTO
import dev.alansantos.rentx.modules.users.controllers.dtos.response.CreateSessionResponseDTO
import dev.alansantos.rentx.modules.users.services.AuthenticateUserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/sessions"])
class SessionController(val authenticateUserService: AuthenticateUserService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody createSessionRequestDTO: CreateSessionRequestDTO): CreateSessionResponseDTO {
        val (email, password) = createSessionRequestDTO

        val session = authenticateUserService.execute(email, password)

        return CreateSessionResponseDTO(session)
    }

}