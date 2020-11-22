package dev.alansantos.rentx.modules.users.domains

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @Column(nullable = false, columnDefinition = "uuid")
        var id: UUID? = null,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = true)
        var image: String? = null,

        @Column(nullable = false)
        var email: String,

        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        var admin: Boolean,

        @Column(name = "created_at", nullable = false)
        var createdAt: LocalDateTime? = null,

        @Column(name = "updated_at", nullable = false)
        var updatedAt: LocalDateTime? = null

) {

    @PrePersist
    fun prePersist() {
        this.id = UUID.randomUUID()
        this.createdAt = LocalDateTime.now()
        this.updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}