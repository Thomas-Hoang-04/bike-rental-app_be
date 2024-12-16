package com.cnpm.bikerentalapp.config.utility

import com.cnpm.bikerentalapp.user.repository.TicketRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Component
class TicketStatusUpdater(
    private val ticketRepo: TicketRepository,
) {
    @Scheduled(cron = "0 */3 * * * *")
    @Transactional
    fun updateTicketStatus() {
        ticketRepo.findAll().forEach {
            if (it.valid.isBefore(OffsetDateTime.now())) {
                ticketRepo.expireTicket(it.ticketID)
            }
        }
    }
}