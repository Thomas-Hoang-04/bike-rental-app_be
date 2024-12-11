package com.cnpm.bikerentalapp.user.model.httprequest

import com.cnpm.bikerentalapp.user.model.entity.TransactionsDetails
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus

data class TransactionRequest(
    val username: String,
    val amount: Int,
    val purpose: TransactionPurpose,
    val descriptions: String,
) {
    fun mapToEntity(user: UserCredential, status: TransactionStatus): TransactionsDetails =
        TransactionsDetails(
            user = user,
            amount = amount,
            purpose = purpose,
            descriptions = descriptions,
            status = status,
        )
}
