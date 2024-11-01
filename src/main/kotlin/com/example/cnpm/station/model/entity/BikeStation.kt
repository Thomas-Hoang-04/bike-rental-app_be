package com.example.cnpm.station.model.entity

import com.example.cnpm.bike.model.entity.Bike
import com.example.cnpm.station.model.types.BikeStationStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType

@Entity
@Table(name = "bike_station", indexes = [Index(name = "station_region_id", columnList = "region_id, region_num", unique = true)])
class BikeStation {

    @Id
    @Column(name = "station_id")
    val locationID: String = ""

    @Column(name = "region_id", nullable = false, length = 3)
    var regionID: String = ""

    @Column(name = "region_num", nullable = false)
    var regionNum: Int = 0

    @Column(name = "station_name", nullable = false)
    var name: String = ""

    @Column(name = "address", nullable = false)
    var address: String = ""

    @Column(name = "capacity", nullable = false)
    var capacity: Int = 0

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    var status: BikeStationStatus = BikeStationStatus.ACTIVE

    @OneToMany(targetEntity = Bike::class, cascade = [CascadeType.ALL])
    @JoinColumn(name = "bike_location", referencedColumnName = "station_id")
    private val bikesList: List<Bike> = emptyList()
}