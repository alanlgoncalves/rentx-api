package dev.alansantos.rentx.modules.users.services

import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.exceptions.UserAlreadyExistsException
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUsersService(private val usersGateway: UsersGateway) {

    fun execute(name: String, email: String, password: String, admin: Boolean = false): User {

        validateEmail(email)

        val encryptPassword = encryptPassword(password)

        val user = User(name = name, email = email, password = encryptPassword, admin = admin)

        return usersGateway.save(user)
    }

    private fun encryptPassword(password: String): String {
        //return BCryptPasswordEncoder().encode(password)
        return password
    }

    private fun validateEmail(email: String) {
        val findUserByEmail = usersGateway.findUserByEmail(email)

        if (findUserByEmail.isPresent) {
            throw UserAlreadyExistsException("User already exists with email $email");
        }

    }

}