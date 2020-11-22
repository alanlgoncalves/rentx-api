package dev.alansantos.rentx.modules.users.domains

import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: UUID,

        @Column(nullable = false)
        private val name: String,

        @Column(nullable = false)
        private val image: String,

        @Column(nullable = false)
        private val email: String,

        @Column(nullable = false)
        private val password: String,

        @Column(nullable = false)
        private val admin: Boolean,

        @Column(name = "created_at", nullable = false)
        private val createdAt: LocalDate,

        @Column(name = "updated_at", nullable = false)
        private val updatedAt: LocalDate
) {
    constructor() {}
}