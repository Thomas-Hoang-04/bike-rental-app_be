package com.example.cnpm.bike.model.entity

import com.example.cnpm.bike.model.types.BikeStationStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType

@Entity
@Table(name = "bike_station")
class BikeStation {

    @Id
    @Column(name = "station_id")
    val id: String = ""

    @Column(name = "region_id", nullable = false, length = 3)
    var regionID: String = ""

    @Column(name = "region_num", nullable = false)
    var regionNum: Int = 0

    @Column(name = "station_name", nullable = false)
    var stationName: String = ""

    @Column(name = "address", nullable = false)
    var stationAddress: String = ""

    @Column(name = "capacity", nullable = false)
    var stationCapacity: Int = 0

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    var stationStatus: BikeStationStatus = BikeStationStatus.ACTIVE

    @OneToMany(targetEntity = Bike::class, cascade = [CascadeType.ALL])
    @JoinColumn(name = "bike_location", referencedColumnName = "station_id")
    private val bikes: List<Bike> = emptyList()
}