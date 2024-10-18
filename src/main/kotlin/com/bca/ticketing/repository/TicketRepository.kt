package com.bca.ticketing.repository

import com.bca.ticketing.model.Ticket
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TicketRepository: ReactiveCrudRepository<Ticket, Long> {
}