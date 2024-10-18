package com.example.cnpm.repositories

import com.example.cnpm.model.Bike
import com.example.cnpm.model.BikeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BikeRepository : JpaRepository<Bike, UUID> {

    @Query("SELECT * FROM bike_data b WHERE b.bike_type::bike_type = :type::bike_type", nativeQuery = true)
    fun getBikeByType(@Param("type") type: BikeType): List<Bike>

    @Query("""SELECT * FROM bike_data b WHERE b.bike_status::bike_status = 'AVAILABLE'::bike_status 
        AND b.bike_type::bike_type = :type::bike_type""", nativeQuery = true)
    fun getAvailableBikesByType(@Param("type") type: BikeType): List<Bike>

    @Query("SELECT COUNT(*) FROM bike_data b WHERE b.bike_type::bike_type = :type::bike_type", nativeQuery = true)
    fun countBikesByType(@Param("type") type: BikeType): Int

    @Query("SELECT * FROM bike_data b WHERE b.plate = :plate", nativeQuery = true)
    fun getBikeByPlate(@Param("plate") plate: String): Bike

    @Query("SELECT bike_id FROM bike_data b WHERE b.plate = :plate", nativeQuery = true)
    fun getBikeIdByPlate(@Param("plate") plate: String): UUID

    @Query("SELECT COUNT(*) FROM bike_data b WHERE b.plate = :plate", nativeQuery = true)
    fun checkBikeExistByPlate(@Param("plate") plate: String): Int
}