package com.cnpm.bikerentalapp.bike.model.entity

import com.cnpm.bikerentalapp.bike.model.dto.BikeDTO
import com.cnpm.bikerentalapp.bike.model.httprequest.BikeCreateRequest
import com.cnpm.bikerentalapp.bike.model.types.BikeStatus
import com.cnpm.bikerentalapp.bike.model.types.BikeType
import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.*

@Entity
@Table(name = "bike_data")
class Bike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bike_id")
    val id: UUID = UUID.randomUUID()

    @Column(name = "plate", nullable = false, unique = true, length = 16)
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

    @ManyToOne
    @JoinColumn(name = "bike_location", nullable = true, referencedColumnName = "station_id")
    var location: BikeStation? = null

    fun mapBikeCreateToEntity(req: BikeCreateRequest, location: BikeStation?) {
        this.plate = req.plate
        this.type = req.type
        this.battery = req.battery ?: this.battery
        this.status = req.status ?: this.status
        this.location = location
    }

    fun mapBikeToDTO() = BikeDTO(
        id = this.id,
        plate = this.plate,
        type = this.type,
        battery = this.battery,
        status = this.status,
        location = this.location?.stationID
    )
}

