package com.cnpm.bikerentalapp.station.model.entity

import com.cnpm.bikerentalapp.bike.model.entity.Bike
import com.cnpm.bikerentalapp.station.model.dto.StationDTO
import com.cnpm.bikerentalapp.station.model.types.StationStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import java.util.UUID

@Entity
@Table(name = "bike_station", indexes = [
    Index(name = "station_region_id", columnList = "region_id, region_num", unique = true),
    Index(name = "station_geo_idx", columnList = "station_geo"),
    Index(name = "station_coordinates", columnList = "latitude, longitude", unique = true)
])
class BikeStation (
    @Column(name = "region_id", nullable = false, length = 3)
    private val regionID: String,

    @Column(name = "region_num", nullable = false)
    private val regionNum: Int,

    @Column(name = "latitude", nullable = false)
    private val latitude: Double,

    @Column(name = "longitude", nullable = false)
    private val longitude: Double,

    @Column(name = "station_name", nullable = false)
    private val name: String,

    @Column(name = "address", nullable = false)
    private val address: String,

    @Column(name = "capacity", nullable = false)
    private val capacity: Int,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "status", nullable = false)
    private val status: StationStatus,

    @OneToMany(mappedBy = "location", orphanRemoval = false,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    private val bikes: MutableList<Bike> = mutableListOf()

) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "station_id")
    private lateinit var id: UUID

    val stationID: UUID
        get() = this.id

    @Column(name = "station_geo", columnDefinition = "GEOGRAPHY(Point, 4326)", nullable = false)
    private lateinit var geoLocation: Point

    init {
        this.geoLocation = GeometryFactory().createPoint(Coordinate(this.longitude, this.latitude))
    }

    val publicCapacity: Int
        get() = this.capacity

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

    fun createGeoLocation() {
        this.geoLocation = GeometryFactory().createPoint(Coordinate(this.longitude, this.latitude))
    }
}