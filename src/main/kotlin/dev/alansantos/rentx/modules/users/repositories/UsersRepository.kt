package dev.alansantos.rentx.modules.users.repositories

import dev.alansantos.rentx.modules.users.domains.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UsersRepository : CrudRepository<UUID, User> {}