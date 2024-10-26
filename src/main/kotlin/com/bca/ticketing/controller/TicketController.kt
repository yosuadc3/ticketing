package com.bca.ticketing.controller

import com.bca.ticketing.model.Ticket
import com.bca.ticketing.service.TicketService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {

    // Endpoint to book a ticket
    @PostMapping("/book")
    fun bookTicket(
        @RequestParam userId: Long,
        @RequestParam routeId: Long
    ): Mono<Ticket> {
        return ticketService.bookTicket(userId, routeId)
    }

    // Endpoint to get a ticket by ID
    @GetMapping("/{ticketId}")
    fun getTicketById(@PathVariable ticketId: Long): Mono<Ticket> {
        return ticketService.getTicketById(ticketId)
    }

    // Endpoint to get all tickets for a user
    @GetMapping("/user/{userId}")
    fun getTicketsByUser(@PathVariable userId: Long): Flux<Ticket> {
        return ticketService.getTicketsByUserId(userId)
    }
}