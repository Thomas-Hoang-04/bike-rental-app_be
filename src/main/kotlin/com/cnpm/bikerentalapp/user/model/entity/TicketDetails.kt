package com.cnpm.bikerentalapp.user.model.entity

import com.cnpm.bikerentalapp.user.model.dto.TicketDetailsDTO
import com.cnpm.bikerentalapp.user.model.types.TicketStatus
import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "ticket_details")
class TicketDetails(

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "ticket_type", nullable = false)
    private val ticket: TicketTypes,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    private val status: TicketStatus = TicketStatus.ACTIVE,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private val user: UserCredential,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private lateinit var id: UUID

    @CreationTimestamp
    @Column(name = "issued_at", nullable = false)
    private lateinit var issuedAt: OffsetDateTime

    @Column(name = "valid_till", nullable = false)
    private lateinit var validTill: OffsetDateTime

    val valid: OffsetDateTime
        get() = validTill

    val ticketID : UUID
        get() = id

    fun mapEntityToDTO() = TicketDetailsDTO(
        this.ticket,
        this.user.user,
        this.issuedAt.toString(),
        this.validTill.toString(),
        this.status
    )
}