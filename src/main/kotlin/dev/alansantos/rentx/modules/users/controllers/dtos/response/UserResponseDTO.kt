package dev.alansantos.rentx.modules.users.controllers.dtos.response

import dev.alansantos.rentx.modules.users.domains.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import java.util.*

@ApiModel(value = "UserResponseDTO", description = "User data")
class UserResponseDTO {

    @ApiModelProperty(value = "User id", dataType = "UUID")
    val id: UUID

    @ApiModelProperty(value = "User name", dataType = "String")
    val name: String

    @ApiModelProperty(value = "User avatar", dataType = "String")
    val image: String?

    @ApiModelProperty(value = "User email", dataType = "String")
    val email: String

    @ApiModelProperty(value = "User roles", dataType = "Set")
    val roles: Set<String>

    @ApiModelProperty(value = "User creation date time", dataType = "Timestamp")
    val createdAt: LocalDateTime

    @ApiModelProperty(value = "Last User update date time", dataType = "Timestamp")
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