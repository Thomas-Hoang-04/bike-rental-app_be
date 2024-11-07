package com.cnpm.bikerentalapp.bike.repository

import com.cnpm.bikerentalapp.bike.model.entity.Bike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BikeRepository : JpaRepository<Bike, UUID> {

    @Query("SELECT * FROM bike_data b WHERE b.bike_type = CAST(:type as bike_type)", nativeQuery = true)
    fun getBikeByType(@Param("type") type: String): List<Bike>

    @Query("SELECT * FROM bike_data b WHERE b.bike_status = 'AVAILABLE'::bike_status", nativeQuery = true)
    fun getAvailableBikes(): List<Bike>

    @Query("SELECT * FROM bike_data b WHERE b.bike_status = 'AVAILABLE'::bike_status AND b.bike_type = CAST(:type as bike_type)", nativeQuery = true)
    fun getAvailableBikesByType(@Param("type") type: String): List<Bike>

    @Query("SELECT COUNT(*) FROM bike_data b WHERE b.bike_type = CAST(:type as bike_type)", nativeQuery = true)
    fun countBikesByType(@Param("type") type: String): Int

    @Query("SELECT * FROM bike_data b WHERE b.plate = :plate", nativeQuery = true)
    fun getBikeByPlate(@Param("plate") plate: String): Optional<Bike>

    @Query("SELECT bike_id FROM bike_data b WHERE b.plate = :plate", nativeQuery = true)
    fun getBikeIdByPlate(@Param("plate") plate: String): Optional<UUID>

    @Query("SELECT COUNT(*) FROM bike_data b WHERE b.bike_location = :location", nativeQuery = true)
    fun checkBikeCountByLocation(@Param("location") location: UUID): Int

}