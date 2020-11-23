package dev.alansantos.rentx.modules.users.controllers.dtos.response

import dev.alansantos.rentx.modules.users.domains.Session
import dev.alansantos.rentx.modules.users.domains.User
import java.time.LocalDateTime
import java.util.*

class CreateSessionResponseDTO {

    val userDTO: UserDTO
    val token: String

    constructor(session: Session) {
        this.userDTO = UserDTO(session.user)
        this.token = session.token
    }

    class UserDTO {
        val id: UUID
        val name: String
        val image: String?
        val email: String
        val admin: Boolean
        val createdAt: LocalDateTime
        val updatedAt: LocalDateTime

        constructor(user: User) {
            this.id = user.id!!
            this.name = user.name
            this.image = user.image
            this.email = user.email
            this.admin = false
            this.createdAt = user.createdAt!!
            this.updatedAt = user.updatedAt!!
        }

    }

}
