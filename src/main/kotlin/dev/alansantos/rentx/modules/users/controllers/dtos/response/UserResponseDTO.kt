package dev.alansantos.rentx.modules.users.controllers.dtos.response

import dev.alansantos.rentx.modules.users.domains.User
import java.time.LocalDateTime
import java.util.*

class UserResponseDTO {

    val id: UUID
    val name: String
    val image: String?
    val email: String
    val roles: Set<String>
    val createdAt: LocalDateTime
    val updatedAt: LocalDateTime

    constructor(user: User) {
        this.id = user.id!!
        this.name = user.name
        this.image = user.image
        this.email = user.email
        this.roles = user.roles.map { it.name }.toSet()
        this.createdAt = user.createdAt!!
        this.updatedAt = user.updatedAt!!
    }

}