package dev.alansantos.rentx.modules.users.gateways

import dev.alansantos.rentx.modules.users.domains.Role
import java.util.*

interface RolesGateway {

    fun findByName(name: String): Optional<Role>

}