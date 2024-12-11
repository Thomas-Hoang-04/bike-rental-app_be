package com.cnpm.bikerentalapp.user.repository

import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<UserCredential, UUID> {

    @Query("SELECT * FROM user_credentials usrc WHERE usrc.username = :username", nativeQuery = true)
    fun getByUsername(@Param("username") username: String) : Optional<UserCredential>

    @Query("SELECT ST_AsEncodedPolyline(trip.travel_route) FROM trip_details trip WHERE trip.id = :id", nativeQuery = true)
    fun getTravelRoute(@Param("id") id: String): String
}