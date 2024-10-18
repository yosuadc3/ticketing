package com.bca.ticketing.repository

import com.bca.ticketing.model.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PaymentRepository: ReactiveCrudRepository<Payment, Long> {
}