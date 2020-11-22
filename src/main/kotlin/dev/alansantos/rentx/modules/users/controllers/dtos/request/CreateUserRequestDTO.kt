package dev.alansantos.rentx.modules.users.controllers.dtos.request

data class CreateUserRequestDTO(
        val name: String,
        val email: String,
        val password: String
)