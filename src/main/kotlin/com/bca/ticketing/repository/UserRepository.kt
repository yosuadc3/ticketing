package com.bca.ticketing.repository

import com.bca.ticketing.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository: ReactiveCrudRepository<User, Long> {
}