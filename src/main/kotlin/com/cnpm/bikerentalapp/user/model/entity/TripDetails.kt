package com.cnpm.bikerentalapp.user.model.entity

import com.cnpm.bikerentalapp.user.model.dto.TripDetailsDTO
import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import jakarta.persistence.*
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.hibernate.dialect.PostgreSQLIntervalSecondJdbcType
import org.locationtech.jts.geom.LineString
import java.time.OffsetDateTime
import java.time.Duration

@Entity
@Table(name = "trip_details")
class TripDetails(
    @Id
    @Column(name = "id")
    private val tripId: String,

    @Column(name = "start_time", nullable = false)
    private val startTime: OffsetDateTime,

    @Column(name = "end_time", nullable = false)
    private val endTime: OffsetDateTime,

    @Column(name = "start_address", nullable = false)
    private val startStation: String,

    @Column(name = "end_address", nullable = false)
    private val endStation: String,

    @Column(name = "travel_time", columnDefinition = "INTERVAL" , nullable = false)
    @JdbcType(PostgreSQLIntervalSecondJdbcType::class)
    private val travelTime: Duration,

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType::class)
    @Column(name = "ticket_type", nullable = false)
    private val ticketTypes: TicketTypes = TicketTypes.SINGLE,

    @Suppress("unused")
    @Column(name = "travel_route", columnDefinition = "GEOGRAPHY(LINESTRING, 4326)" ,nullable = false)
    private val travelRoute: LineString,

    @Column(name = "distance", nullable = false)
    private val distance: Double,

    @Column(name = "fee", nullable = false)
    private val fee: Int,

    @Column(name = "bike_plate", nullable = false, length = 12)
    private val bikePlate: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private val user: UserCredential,
) {
    val trip: String
        get() = this.tripId

    fun mapEntityToDTO(route: String) = TripDetailsDTO(
        this.tripId,
        this.user.user,
        this.bikePlate,
        this.startTime.toString(),
        this.endTime.toString(),
        this.startStation,
        this.endStation,
        this.travelTime.toString(),
        this.ticketTypes,
        route,
        this.distance,
        this.fee
    )
}