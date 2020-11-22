package dev.alansantos.rentx.modules.users.domains

data class Session(val user: User, val token: String)