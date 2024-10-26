package com.bca.ticketing.repository

import com.bca.ticketing.model.Payment
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface PaymentRepository : ReactiveCrudRepository<Payment, Long> {

    fun findAllByTicketId(ticketId: Long): Flux<Payment>
}