package com.bca.ticketing.service

import com.bca.ticketing.model.Ticket
import com.bca.ticketing.repository.RouteRepository
import com.bca.ticketing.repository.TicketRepository
import com.bca.ticketing.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val routeRepository: RouteRepository,
    private val userRepository: UserRepository,
    private val paymentService: PaymentService
) {

    fun bookTicket(userId: Long, routeId: Long): Mono<Ticket> {
        return userRepository.findById(userId)
            .flatMap { user ->
                routeRepository.findById(routeId).flatMap { route ->
                    if (user.balance >= route.price) {
                        val ticket = Ticket(
                            id = null,
                            routeId = routeId,
                            userId = userId,
                            status = "PENDING"
                        )
                        ticketRepository.save(ticket).flatMap { savedTicket ->
                            paymentService.processPayment(user.id!!, savedTicket.id!!, route.price)
                                .flatMap {
                                    val bookedTicket = savedTicket.copy(status = "BOOKED")
                                    ticketRepository.save(bookedTicket).flatMap {
                                        val updatedUser = user.copy(balance = user.balance - route.price)
                                        userRepository.save(updatedUser).thenReturn(bookedTicket)
                                    }
                                }
                        }
                    } else {
                        Mono.error(IllegalStateException("Insufficient balance"))
                    }
                }
            }
    }

    fun getTicketById(ticketId: Long): Mono<Ticket> {
        return ticketRepository.findById(ticketId)
    }

    fun getTicketsByUserId(userId: Long): Flux<Ticket> {
        return ticketRepository.findAllByUserId(userId)
    }
}