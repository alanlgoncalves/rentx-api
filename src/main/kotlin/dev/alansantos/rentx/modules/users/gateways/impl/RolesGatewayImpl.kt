package dev.alansantos.rentx.modules.users.gateways.impl

import dev.alansantos.rentx.modules.users.domains.Role
import dev.alansantos.rentx.modules.users.gateways.RolesGateway
import dev.alansantos.rentx.modules.users.repositories.RolesRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class RolesGatewayImpl(val rolesRepository: RolesRepository) : RolesGateway {

    override fun findByName(name: String): Optional<Role> {
        return rolesRepository.findByName(name);
    }

}