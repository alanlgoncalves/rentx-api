package dev.alansantos.rentx.modules.users.repositories

import dev.alansantos.rentx.modules.users.domains.Role
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RolesRepository : CrudRepository<Role, UUID> {

    fun findByName(name: String): Optional<Role>

}