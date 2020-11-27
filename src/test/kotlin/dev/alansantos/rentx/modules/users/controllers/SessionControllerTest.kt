package dev.alansantos.rentx.modules.users.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.alansantos.rentx.modules.users.controllers.dtos.request.CreateSessionRequestDTO
import dev.alansantos.rentx.modules.users.domains.Role
import dev.alansantos.rentx.modules.users.domains.Session
import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.AuthenticationException
import dev.alansantos.rentx.modules.users.services.AuthenticateUserService
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
class EntranceServiceWithAutowireTest : FreeSpec({

}) {
    override fun listeners() = listOf(SpringListener)

    @MockkBean
    private lateinit var authenticateUserService: AuthenticateUserService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    init {
        "Feature: Session API" - {

            val name = "John Doe"
            val email = "john.doe@teste.com"
            val password = "123456"
            val token = "asdfasdfsadfasdf"
            val avatar = "https://alansantos.dev/avatar.png"
            val role = Role(name = "USER", description = "User Role")
            val createdAt = LocalDateTime.now()
            val user = User(id = UUID.randomUUID(), name = name, email = email, password = password, avatar = avatar,
                    roles = setOf(role), createdAt = createdAt, updatedAt = createdAt)

            "Scenario: should create session with success" - {
                "Given: a valid new session request" - {

                    val createSessionRequestDTO = CreateSessionRequestDTO(email, password)

                    val session = Session(user, token)
                    every { authenticateUserService.execute(email, password) } returns session

                    "When: call create session endpoint" - {

                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 200" - {
                            result.response.status shouldBe 201
                        }

                        "And: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }

                        "And: the JWT token must be created" - {
                            result.response.contentAsString.shouldContainJsonKey("$.token")
                        }

                        "And: the user information must be set on response JSON" - {
                            result.response.contentAsString.shouldContainJsonKey("$.user")

                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.id", user.id.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.email", email)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.avatar", avatar)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.roles[0]", role.name)
                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.createdAt", createdAt.toString())
                            result.response.contentAsString.shouldContainJsonKeyValue("$.user.updatedAt", createdAt.toString())

                        }
                    }
                }
            }

            "Scenario: should not create session with invalid email and/or password" - {
                "Given: a new session request with invalid email and/or password" - {
                    val createSessionRequestDTO = CreateSessionRequestDTO(email, password)

                    every { authenticateUserService.execute(email, password) } throws AuthenticationException("")

                    "When: call create session endpoint" - {
                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 401" - {
                            result.response.status shouldBe 401
                        }

                        "And: the response content must be application/json" - {
                            result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
                        }
                    }
                }
            }

            "Scenario: should not create a session with an empty password" - {
                "Given: an invalid new session request with only the email" - {
                    val createSessionRequestDTO = CreateSessionRequestDTO(email, "")

                    "When: call create session endpoint" - {
                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 401" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not create a session with an empty email" - {
                "Given: an invalid new session request with only the password " - {
                    val createSessionRequestDTO = CreateSessionRequestDTO("", "123456")

                    "When: call create session endpoint" - {
                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not create a session with an invalid password size" - {
                "Given: an invalid new session request with the password lass than 6 characters" - {
                    val createSessionRequestDTO = CreateSessionRequestDTO(email, "12345")

                    "When: call create session endpoint" - {
                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }

            "Scenario: should not create a session with an invalid email" - {
                "Given: an invalid new session request with invalid email" - {
                    val createSessionRequestDTO = CreateSessionRequestDTO("john.doe", "123456")

                    "When: call create session endpoint" - {
                        val result = callSessionApi(createSessionRequestDTO)

                        "Then: the session must be created with HTTP status 400" - {
                            result.response.status shouldBe 400
                        }
                    }
                }
            }
        }

    }

    private fun callSessionApi(createSessionRequestDTO: CreateSessionRequestDTO): MvcResult {
        return mockMvc.perform(post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSessionRequestDTO))
        ).andReturn()
    }
}