package com.cnpm.bikerentalapp.user.repository

import com.cnpm.bikerentalapp.user.model.entity.TicketDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TicketRepository: JpaRepository<TicketDetails, UUID> {
    @Modifying
    @Query("UPDATE ticket_details SET status = 'EXPIRED'::ticket_status WHERE id = :id", nativeQuery = true)
    fun expireTicket(@Param("id") id: UUID)
}