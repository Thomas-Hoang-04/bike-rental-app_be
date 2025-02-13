package com.cnpm.bikerentalapp.user.model.entity

import com.cnpm.bikerentalapp.user.model.dto.UserDTO
import com.cnpm.bikerentalapp.user.model.types.UserRole
import com.cnpm.bikerentalapp.user.principal.UserPrincipal
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

@Entity
@Table(name = "user_credentials", indexes = [
    Index(name = "user_credentials_id_password_idx", columnList = "username, password", unique = true)
])
class UserCredential(
    @Column(name = "username", length = 10, unique = true)
    private val username: String,

    @Column(name = "password", nullable = false, unique = true)
    private val password: String,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "role", nullable = false)
    private val role: UserRole
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private lateinit var id: UUID

    @OneToOne(mappedBy = "credential", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    private lateinit var details: UserDetails

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val transactions: MutableList<TransactionsDetails> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val trips: MutableList<TripDetails> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val tickets: MutableList<TicketDetails> = mutableListOf()

    val user : String
        get() = this.username

    val balance: Int
        get() = this.details.accountBalance

    fun mapUserToDTO() = UserDTO(
        this.id,
        this.username,
        this.role,
        this.details.mapEntityToDTO()
    )

    fun mapUserToPrincipal() = UserPrincipal(
        this.id,
        this.username,
        this.password,
        listOf(SimpleGrantedAuthority(this.role.name))
    )

    fun updateBalance(amount: Int) {
        this.details.updateBalance(amount)
    }
}