package com.bca.ticketing.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("payments")
data class Payment(
    @Id val id: Long? = null,
    val ticketId: Long,
    val userId: Long,
    val amount: Double,
    val status: String
)
