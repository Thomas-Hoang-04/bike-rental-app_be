package com.example.cnpm.bike.repositories

import com.example.cnpm.bike.model.entity.BikeStation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface BikeStationRepository: JpaRepository<BikeStation, String> {
    @Query("SELECT * FROM bike_station b WHERE b.status = 'ACTIVE'::bike_station_status", nativeQuery = true)
    fun getAvailableStations(): List<BikeStation>

    @Query("SELECT MAX(region_num) FROM bike_station b where b.region_id = :id", nativeQuery = true)
    fun getRegionalStationMaxNum(@Param("id") regionID: String): Optional<Int>

    @Query("SELECT * FROM bike_station b WHERE b.region_id = :regionID", nativeQuery = true)
    fun getStationsByRegion(@Param("regionID") regionID: String): List<BikeStation>

    @Query("""SELECT * FROM bike_station b 
            JOIN public.region_ref rr on rr.region_id = b.region_id
            WHERE city_name = :city""", nativeQuery = true)
    fun getStationsByCity(@Param("city") city: String): List<BikeStation>

    @Query("SELECT region_id FROM region_ref r where r.city_name = :city", nativeQuery = true)
    fun getRegionIDByCity(@Param("city") city: String): Optional<String>

    @Query("SELECT * FROM bike_station b WHERE b.region_id = :regionID AND b.region_num = :regionIdx", nativeQuery = true)
    fun getStationByRegionID(@Param("regionID") regionID: String, @Param("regionIdx") regionNum: Int): Optional<BikeStation>

    @Query("SELECT capacity FROM bike_station b WHERE b.station_id = :location", nativeQuery = true)
    fun getStationCapacity(@Param("location") location: String): Optional<Int>
}