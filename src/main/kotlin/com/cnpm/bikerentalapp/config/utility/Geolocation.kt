package com.cnpm.bikerentalapp.config.utility

import com.cnpm.bikerentalapp.station.model.entity.BikeStation
import org.locationtech.jts.geom.Coordinate
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

data class Geolocation(
    val lat: Double,
    val lng: Double
) {
    fun mapToEntity(targetStation: BikeStation) {
        val lat: Field = BikeStation::class.java.getDeclaredField("latitude")
        lat.isAccessible = true
        ReflectionUtils.setField(lat, targetStation, lat)
        val lng: Field = BikeStation::class.java.getDeclaredField("longitude")
        lng.isAccessible = true
        ReflectionUtils.setField(lng, targetStation, lng)
    }

    fun mapToPostGISCoordinates(): Coordinate = Coordinate(lng, lat)
}
