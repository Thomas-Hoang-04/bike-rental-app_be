package com.cnpm.bikerentalapp.user.model.httprequest

import com.cnpm.bikerentalapp.config.utility.Geolocation
import com.cnpm.bikerentalapp.user.model.entity.TripDetails
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.types.TicketTypes
import com.fasterxml.jackson.annotation.JsonProperty
import org.locationtech.jts.geom.LineString
import java.time.Duration
import java.time.OffsetDateTime

data class TripRequest(
    val id: String,
    val username: String,

    @JsonProperty("bike_plate")
    val bikePlate: String,

    @JsonProperty("start_time")
    val startTime: String,

    @JsonProperty("end_time")
    val endTime: String,

    @JsonProperty("start_address")
    val startAddress: String,

    @JsonProperty("end_address")
    val endAddress: String,

    @JsonProperty("travel_time")
    val travelTime: String,

    @JsonProperty("ticket_type")
    val ticketType: TicketTypes,

    val fee: Int,
    val route: List<Geolocation>
) {
    fun mapRequestToEntity(route: LineString, distance: Double, user: UserCredential) = TripDetails(
        tripId = this.id,
        startTime = OffsetDateTime.parse(this.startTime),
        endTime = OffsetDateTime.parse(this.endTime),
        startStation = this.startAddress,
        endStation = this.endAddress,
        travelTime = Duration.parse(this.travelTime),
        ticketTypes = this.ticketType,
        travelRoute = route,
        distance = distance,
        fee = this.fee,
        bikePlate = this.bikePlate,
        user = user,
    )
}
