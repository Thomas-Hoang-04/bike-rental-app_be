package com.cnpm.bikerentalapp.user.model.entity

import com.cnpm.bikerentalapp.user.model.dto.TransactionsDetailsDTO
import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "transactions_details")
class TransactionsDetails(
    @Column(name = "amount", nullable = false)
    private val amount: Int,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "purpose", nullable = false)
    private val purpose: TransactionPurpose,

    @Column(name = "descriptions", nullable = false)
    private val descriptions: String,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    private val status: TransactionStatus = TransactionStatus.PENDING,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private val user: UserCredential,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private lateinit var id: UUID

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private lateinit var createdAt: OffsetDateTime

    fun mapEntityToDTO() = TransactionsDetailsDTO(
        this.id,
        this.user.user,
        this.createdAt.toString(),
        this.amount,
        this.purpose,
        this.descriptions,
        this.status
    )
}