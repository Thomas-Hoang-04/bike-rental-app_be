package com.example.cnpm.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.UUID

@Entity
@Table(name = "bike_data")
class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bike_id")
    val id: UUID = UUID.randomUUID()

    @Column(name = "plate", nullable = false, unique = true)
    var plate: String = ""

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "bike_type", nullable = false)
    var type: BikeType = BikeType.MANUAL

    @Column(name = "battery", nullable = false)
    var battery: Int = 100

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "bike_status", nullable = false)
    var status: BikeStatus = BikeStatus.AVAILABLE
}

