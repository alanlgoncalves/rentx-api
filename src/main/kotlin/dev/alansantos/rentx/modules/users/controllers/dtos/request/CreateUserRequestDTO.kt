package dev.alansantos.rentx.modules.users.controllers.dtos.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@ApiModel(value = "CreateUserRequestDTO", description = "Create user data")
data class CreateUserRequestDTO(

        @field:NotBlank
        @field:Size(min = 4)
        @ApiModelProperty(value = "Users name", dataType = "String")
        val name: String,

        @field:Email
        @field:NotBlank
        @ApiModelProperty(value = "Users email", dataType = "String")
        val email: String,

        @field:NotBlank
        @field:Size(min = 6)
        @ApiModelProperty(value = "Users password", dataType = "String")
        val password: String
)