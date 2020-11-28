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
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.mockk.clearAllMocks
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.LocalDateTime
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTest : FreeSpec({

}) {

    @MockkBean
    private lateinit var createUsersService: CreateUsersService

    @MockkBean
    private lateinit var retrieveUserByTokenService: RetrieveUserByTokenService

    @Autowired
    private lateinit var mockMvc: MockMvc

    override fun listeners() = listOf(SpringListener)

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)

        clearAllMocks()
    }

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

                    "When: call create users serice should return user" - {
                        every { createUsersService.execute(name, email, password, false) } returns user
                    }

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the response status should be 201" - {
                            result.response.status shouldBe 201
                        }

                        "Then: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "Then: the user information must be set on response JSON" - {
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

                    "When: call create users serice should throw UserAlreadyExistsException" - {
                        every { createUsersService.execute(name, email, password, false) } throws UserAlreadyExistsException("")
                    }

                    "When: call create users endpoint" - {

                        val result = callCreateUsersApi(createUserRequestDTO)

                        "Then: the response status should be 422" - {
                            result.response.status shouldBe 422
                        }

                        "Then: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "Then: the error message must be on response payload" - {
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

                        "Then: the response status should be 400" - {
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

                        "Then: the response status should be 400" - {
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

                        "Then: the response status should be 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should return user with valid token" - {
                "Given: valid user token" - {
                    val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUB0ZXN0LmNvbSIsImF1dGhvcml0aWV" +
                            "zIjpbIlJPTEVfVVNFUiJdfQ.NInJgeZVp7797g_ULbF2fwWgMZB6be62zoSRYEWINm9hQdOuJA080llHdQl" +
                            "psSFM1f85cwZ3LAcB7O6hr_fh6w"

                    "When: call create users service should return user" - {
                        every { retrieveUserByTokenService.execute(token) } returns user
                    }

                    "When: call get user by token endpoint" - {
                        val result = callGetUserByToken(token)

                        "Then: the response status should be 200" - {
                            result.response.status shouldBe 200
                        }

                        "Then: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "Then: the user information must be set on response JSON" - {
                            result.response.contentAsString.shouldContainJsonKeyValue("$.id", user.id.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.email", email)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.roles[0]", role.name)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.createdAt", createdAt.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.updatedAt", createdAt.toString())
                        }
                    }
                }
            }

            "Scenario: should not return user with an invalid token" - {
                "Given: valid user token" - {
                    val token = "asdfasdf"

                    "When: call get user by token endpoint" - {
                        val result = callGetUserByToken(token)

                        "Then: the response status should be 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not return user with an expired token" - {
                "Given: valid user token" - {
                    val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUB0ZXN0LmNvbSIsImF1dGhvcml0a" +
                            "WVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2MDY1MjI2MjJ9.RM8h6HtZGR0BCxyNhlUbLQtkaph2bDSe_" +
                            "5w0Tdol__DNKBpEDNIwCqk3NnLZhPAC-wfevxV1kmngQqhy5mnTlg"

                    "When: call get user by token endpoint" - {
                        val result = callGetUserByToken(token)

                        "Then: the response status should be 401" - {
                            result.response.status shouldBe 401
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

    private fun callGetUserByToken(token: String): MvcResult {
        return mockMvc.perform(get("/api/v1/users")
                .header("Authorization", token)
        ).andReturn()
    }
}