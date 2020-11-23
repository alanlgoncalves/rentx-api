package dev.alansantos.rentx.modules.users.domains

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
        @Id
        @Column(nullable = false, columnDefinition = "uuid")
        var id: UUID? = null,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var description: String,

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