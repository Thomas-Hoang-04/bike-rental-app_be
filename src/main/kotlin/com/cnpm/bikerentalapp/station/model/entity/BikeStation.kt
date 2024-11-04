package com.cnpm.bikerentalapp.station.model.entity

import com.cnpm.bikerentalapp.bike.model.entity.Bike
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import java.util.UUID

@Entity
@Table(name = "bike_station", indexes = [Index(name = "station_region_id", columnList = "region_id, region_num", unique = true)])
class BikeStation {

    @Id
    @Column(name = "station_id")
    val stationID: UUID = UUID.randomUUID()

    @Column(name = "region_id", nullable = false, length = 3)
    var regionID: String = ""

    @Column(name = "region_num", nullable = false)
    var regionNum: Int = 0

    @Column(name = "latitude", nullable = false)
    var latitude: Double = 0.0

    @Column(name = "longitude", nullable = false)
    var longitude: Double = 0.0

    @Column(name = "station_geo", columnDefinition = "GEOGRAPHY(Point, 4326)", nullable = false)
    var geoLocation: Point = GeometryFactory().createPoint(Coordinate(0.0, 0.0))

    @Column(name = "station_name", nullable = false)
    var name: String = ""

    @Column(name = "address", nullable = false)
    var address: String = ""

    @Column(name = "capacity", nullable = false)
    var capacity: Int = 0

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    var status: StationStatus = StationStatus.ACTIVE

    @OneToMany(targetEntity = Bike::class, cascade = [CascadeType.ALL])
    @JoinColumn(name = "bike_location", referencedColumnName = "station_id")
    private val bikesList: List<Bike> = emptyList()
}