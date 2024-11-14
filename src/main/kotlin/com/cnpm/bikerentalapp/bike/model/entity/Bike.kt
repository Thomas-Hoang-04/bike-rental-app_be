package com.cnpm.bikerentalapp.bike.model.entity

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.types.BikeStatus
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.*

@Entity
@Table(name = "bike_data")
class Bike(

    @Column(name = "plate", nullable = false, unique = true, length = 16)
    private val plate: String,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "bike_type", nullable = false)
    private val type: BikeType,

    @Column(name = "battery", nullable = false)
    @Min(0)
    @Max(100)
    private val battery: Int,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "bike_status", nullable = false)
    private val status: BikeStatus,

    @ManyToOne
    @JoinColumn(name = "bike_location", nullable = true, referencedColumnName = "station_id")
    private val location: BikeStation?
) {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bike_id")
    private lateinit var id: UUID

    val publicType: BikeType
        get() = this.type

    fun mapBikeToDTO() = BikeDTO(
        id = this.id,
        plate = this.plate,
        type = this.type,
        battery = this.battery,
        status = this.status,
        location = this.location?.stationID
    )
}

