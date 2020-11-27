package dev.alansantos.rentx.modules.users.controllers.dtos.response

import dev.alansantos.rentx.modules.users.domains.Session
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "CreateSessionResponseDTO", description = "Created session data")
class CreateSessionResponseDTO {

    @ApiModelProperty(value = "Users information", dataType = "User")
    val user: UserResponseDTO

    @ApiModelProperty(value = "Users token", dataType = "String")
    val token: String

    constructor(session: Session) {
        this.user = UserResponseDTO(session.user)
        this.token = session.token
    }

}
