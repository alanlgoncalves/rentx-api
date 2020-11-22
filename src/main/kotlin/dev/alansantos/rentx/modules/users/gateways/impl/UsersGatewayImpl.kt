package dev.alansantos.rentx.modules.users.gateways.impl

import dev.alansantos.rentx.modules.users.domains.User
import dev.alansantos.rentx.modules.users.gateways.UsersGateway
import dev.alansantos.rentx.modules.users.repositories.UsersRepository
import java.util.*

class UsersGatewayImpl(private val usersRepository: UsersRepository) : UsersGateway {

    override fun save(user: User): User {
        return usersRepository.save(user)
    }

    override fun findUserByEmail(email: String): Optional<User> {
        return usersRepository.findByEmail(email)
    }

}