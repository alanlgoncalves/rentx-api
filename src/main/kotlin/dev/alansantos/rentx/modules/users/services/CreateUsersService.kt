package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.UserAlreadyExistsException
import dev.alansantos.rentx.modules.users.gateways.RolesGateway
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUsersService(private val usersGateway: UsersGateway,
                         private val rolesGateway: RolesGateway,
                         private val passwordEncoder: PasswordEncoder) {

    fun execute(name: String, email: String, password: String, admin: Boolean = false): User {

        validateEmail(email)

        val encryptPassword = encryptPassword(password)

        val role = rolesGateway.findByName("USER").get()

        val user = User(name = name, email = email, password = encryptPassword, roles = setOf(role))

        return usersGateway.save(user)
    }

    private fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    private fun validateEmail(email: String) {
        val findUserByEmail = usersGateway.findUserByEmail(email)

        if (findUserByEmail.isPresent) {
            throw UserAlreadyExistsException("User already exists with email $email");
        }

    }

}