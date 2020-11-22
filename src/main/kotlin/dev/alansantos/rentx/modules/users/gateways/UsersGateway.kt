package dev.alansantos.rentx.modules.users.gateways

import dev.alansantos.rentx.modules.users.domains.User
import java.util.*

interface UsersGateway {

    fun save(user: User): User

    fun findUserByEmail(email: String): Optional<User>

}