package dev.alansantos.rentx.modules.users.controllers.dtos.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ApiModel(value = "CreateSessionRequestDTO", description = "Create session data")
data class CreateSessionRequestDTO(

        @field:Email
        @field:NotBlank
        @ApiModelProperty(value = "Users email", dataType = "String")
        val email: String,

        @field:NotBlank
        @field:Size(min = 6)
        @ApiModelProperty(value = "Users password", dataType = "String")
        val password: String)