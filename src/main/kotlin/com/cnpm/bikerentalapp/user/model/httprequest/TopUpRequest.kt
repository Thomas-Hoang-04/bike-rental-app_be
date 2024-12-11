package com.cnpm.bikerentalapp.user.model.httprequest

import com.cnpm.bikerentalapp.user.model.entity.TransactionsDetails
import com.cnpm.bikerentalapp.user.model.entity.UserCredential
import com.cnpm.bikerentalapp.user.model.types.TransactionPurpose
import com.cnpm.bikerentalapp.user.model.types.TransactionStatus

data class TopUpRequest(
    val from: String,
    val to: String = from,
    val amount: Int
) {
    fun mapToSrcTransactions(src: UserCredential, status: TransactionStatus): TransactionsDetails =
        TransactionsDetails(
            user = src,
            amount = -amount,
            purpose = TransactionPurpose.POINTSHARE,
            descriptions = "Chia sẻ điểm cho $to",
            status = status
        )

    fun mapToDestTransactions(dest: UserCredential, status: TransactionStatus): TransactionsDetails =
        TransactionsDetails(
            user = dest,
            amount = amount,
            purpose = TransactionPurpose.POINTSHARE,
            descriptions = "Nhận điểm từ $from",
            status = status
        )

    fun mapToTopUpTransactions(user: UserCredential, status: TransactionStatus): TransactionsDetails =
        TransactionsDetails(
            user = user,
            amount = amount,
            purpose = TransactionPurpose.TOPUP,
            descriptions = "Thanh toán trực tuyến",
            status = status
        )
}
