package com.bca.ticketing.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value="payments")
data class Payment(
    @Id val id: Long?,
    val ticketId: Long,
    val amount: Double,
    val status: String

)
