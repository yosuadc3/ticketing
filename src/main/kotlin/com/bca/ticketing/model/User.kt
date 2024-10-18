package com.bca.ticketing.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value="users")
data class User(
    @Id val id: Long?,
    val name: String,
    val email: String

)
