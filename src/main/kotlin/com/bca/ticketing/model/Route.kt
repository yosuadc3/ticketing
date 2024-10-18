package com.bca.ticketing.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value="routes")
data class Route(
    @Id val id: Long?,
    val origin: String,
    val destination: String,
    val price: Double

)
