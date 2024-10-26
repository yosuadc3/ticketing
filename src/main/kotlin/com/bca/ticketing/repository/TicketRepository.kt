package com.bca.ticketing.repository

import com.bca.ticketing.model.Ticket
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface TicketRepository : ReactiveCrudRepository<Ticket, Long> {
    fun findAllByUserId(userId: Long): Flux<Ticket>
}