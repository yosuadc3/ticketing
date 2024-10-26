package com.bca.ticketing.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(value="tickets")
data class Ticket(
    @Id val id: Long?,
    val userId: Long,
    val routeId: Long,
    val status: String,
)
