package dev.alansantos.rentx.modules.users.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateUserRequestDTO
import dev.alansantos.rentx.modules.users.domains.Role
import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.UserAlreadyExistsException
import dev.alansantos.rentx.modules.users.services.CreateUsersService
import dev.alansantos.rentx.modules.users.services.RetrieveUserByTokenService
import io.kotest.assertions.json.shouldContainJsonKey
import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.LocalDateTime
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTest : FreeSpec({

}) {
    override fun listeners() = listOf(SpringListener)

    @MockkBean
    private lateinit var createUsersService: CreateUsersService

    @MockkBean
    private lateinit var retrieveUserByTokenService: RetrieveUserByTokenService

    @Autowired
    private lateinit var mockMvc: MockMvc

    init {
        "Feature: Users API" - {

            val name = "John Doe"
            val email = "john.doe@teste.com"
            val password = "123456"
            val role = Role(name = "USER", description = "User Role")
            val createdAt = LocalDateTime.now()
            val user = User(id = UUID.randomUUID(), name = name, email = email, password = password,
                    roles = setOf(role), createdAt = createdAt, updatedAt = createdAt)

            "Scenario: should create user with success" - {
                "Given: a valid new user request" - {

                    val createUserRequestDTO = CreateUserRequestDTO(name, email, password)

                    every { createUsersService.execute(name, email, password, false) } returns user

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the session must be created with HTTP status 201" - {
                            result.response.status shouldBe 201
                        }

                        "And: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "And: the user information must be set on response JSON" - {
                            result.response.contentAsString.shouldContainJsonKeyValue("$.id", user.id.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.email", email)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.roles[0]", role.name)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.createdAt", createdAt.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.updatedAt", createdAt.toString())

                        }
                    }
                }
            }

            "Scenario: should not create an user with an existing email" - {
                "Given: a valid new user request" - {

                    val createUserRequestDTO = CreateUserRequestDTO(name, email, password)

                    every { createUsersService.execute(name, email, password, false) } throws UserAlreadyExistsException("")

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the session must be created with HTTP status 422" - {
                            result.response.status shouldBe 422
                        }

                        "And: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "And: the error message must be on response payload" - {
                            result.response.contentAsString.shouldContainJsonKey("$.message")
                        }
                    }
                }
            }

            "Scenario: should not create an user with an invalid email" - {
                "Given: an invalid new user request with invalid email" - {

                    val createUserRequestDTO = CreateUserRequestDTO(name, "john.doe", password)

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not create an user with an invalid password size" - {
                "Given: an invalid new user request with size lass than 6 characters" - {

                    val createUserRequestDTO = CreateUserRequestDTO(name, email, "12345")

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not create an user with an invalid name size" - {
                "Given: an invalid new user request with size lass than 4 characters" - {

                    val createUserRequestDTO = CreateUserRequestDTO("abc", email, password)

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

        }
    }

    private fun callCreateUsersApi(createUserRequestDTO: CreateUserRequestDTO): MvcResult {
        return mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createUserRequestDTO))
        ).andReturn()
    }
}