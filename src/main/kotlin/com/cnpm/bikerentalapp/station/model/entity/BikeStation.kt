package com.cnpm.bikerentalapp.station.model.entity

import com.cnpm.bikerentalapp.bike.model.entity.Bike
import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.httprequest.StationCreateRequest
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import java.util.*

@Entity
@Table(name = "bike_station", indexes = [
    Index(name = "station_region_id", columnList = "region_id, region_num", unique = true),
    Index(name = "station_geo_idx", columnList = "station_geo"),
    Index(name = "station_coordinates", columnList = "latitude, longitude", unique = true)
])
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

    @OneToMany(mappedBy = "location", orphanRemoval = false,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val bikes: MutableList<Bike> = mutableListOf()

    fun mapStationToDTO() = StationDTO(
        stationID = this.stationID,
        regionID = this.regionID,
        regionNum = this.regionNum,
        name = this.name,
        address = this.address,
        capacity = this.capacity,
        status = this.status,
        latitude = this.latitude,
        longitude = this.longitude,
        bikeList = this.bikes.map { it.mapBikeToDTO() }
    )

    fun mapStationCreateToEntity(req: StationCreateRequest, regionCodex: Pair<String, Int>) {
        this.regionID = regionCodex.first
        this.regionNum = regionCodex.second
        this.name = req.name
        this.address = req.address
        this.capacity = req.capacity ?: this.capacity
        this.status = req.status ?: this.status
        this.latitude = req.latitude
        this.longitude = req.longitude
        this.createGeoLocation()
    }

    fun createGeoLocation() {
        this.geoLocation = GeometryFactory().createPoint(Coordinate(this.longitude, this.latitude))
    }
}