package com.cnpm.bikerentalapp.user.model.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "user_details")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID = UUID.randomUUID()

    @Column(name = "name", nullable = false)
    var name: String = ""

    @Column(name = "email", nullable = false)
    var email: String = ""

    @Column(name = "phone_num", nullable = false, length = 12, unique = true)
    var phoneNum: String = ""

    @Column(name = "dob", nullable = false)
    var dob: LocalDate = LocalDate.now()
}