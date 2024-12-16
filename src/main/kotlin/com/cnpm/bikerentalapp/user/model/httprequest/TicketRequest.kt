package com.cnpm.bikerentalapp.user.model.httprequest

import com.cnpm.bikerentalapp.user.model.entity.TicketDetails
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.types.TicketTypes

data class TicketRequest(
    val username: String,
    val ticket: TicketTypes,
    val price: Int,
    val quantity: Int = 1,
) {
    fun mapToEntity(user: UserCredential) = TicketDetails(
        user = user,
        ticket = ticket,
    )
}
