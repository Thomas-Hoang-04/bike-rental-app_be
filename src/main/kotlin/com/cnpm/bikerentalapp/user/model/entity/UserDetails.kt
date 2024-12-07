package com.cnpm.bikerentalapp.user.model.entity

import com.cnpm.bikerentalapp.user.model.dto.UserDetailsDTO
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(name = "user_details")
class UserDetails (

    @Column(name = "name", nullable = false)
    private val name: String,

    @Column(name = "email", nullable = false)
    private val email: String,

    @Column(name = "phone_num", nullable = false, length = 12, unique = true)
    private val phoneNum: String,

    @Column(name = "dob", nullable = false)
    private val dob: LocalDate,

    @Column(name = "balance", nullable = false)
    @Min(0)
    private val balance: Int = 0,

    @Suppress("unused")
    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private val credential: UserCredential

) {
    @Suppress("unused")
    @Id
    @Column(name = "id")
    private lateinit var id: UUID

    fun mapEntityToDTO() = UserDetailsDTO(
        this.name,
        this.phoneNum,
        this.email,
        this.dob.format(DateTimeFormatter.ISO_LOCAL_DATE),
        this.balance
    )
}