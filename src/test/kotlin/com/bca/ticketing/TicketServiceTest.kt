package com.bca.ticketing

import com.bca.ticketing.model.Payment
import com.bca.ticketing.model.Route
import com.bca.ticketing.model.Ticket
import com.bca.ticketing.model.User
import com.bca.ticketing.service.PaymentService
import com.bca.ticketing.service.TicketService
import com.bca.ticketing.repository.RouteRepository
import com.bca.ticketing.repository.TicketRepository
import com.bca.ticketing.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(MockitoExtension::class)
class TicketServiceTest {

    private lateinit var ticketService: TicketService

    @Mock
    private lateinit var ticketRepository: TicketRepository

    @Mock
    private lateinit var routeRepository: RouteRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        ticketService = TicketService(ticketRepository, routeRepository, userRepository, paymentService)
    }

    @Test
    fun `should book a ticket successfully`() {
        val user = User(1L, "John Doe", "john@example.com", 200.0)
        val route = Route(1L, "A", "B", 100.0)
        val ticket = Ticket(null, 1L, 1L, "PENDING")
        val savedTicket = ticket.copy(id = 1L)
        val payment = Payment(null, user.id!!, savedTicket.id!!, route.price, "PENDING")

        `when`(userRepository.findById(1L)).thenReturn(Mono.just(user))
        `when`(routeRepository.findById(1L)).thenReturn(Mono.just(route))
        `when`(ticketRepository.save(ticket)).thenReturn(Mono.just(savedTicket))
        `when`(paymentService.processPayment(user.id!!, savedTicket.id!!, route.price)).thenReturn(Mono.just(payment))
        `when`(ticketRepository.save(savedTicket.copy(status = "BOOKED"))).thenReturn(Mono.just(savedTicket.copy(status = "BOOKED")))
        `when`(userRepository.save(user.copy(balance = user.balance - route.price))).thenReturn(Mono.just(user.copy(balance = 100.0)))

        val result = ticketService.bookTicket(1L, 1L)

        StepVerifier.create(result)
            .expectNext(savedTicket.copy(status = "BOOKED"))
            .verifyComplete()

        verify(userRepository).findById(1L)
        verify(routeRepository).findById(1L)
        verify(ticketRepository).save(ticket)
        verify(paymentService).processPayment(user.id!!, savedTicket.id!!, route.price)
        verify(ticketRepository).save(savedTicket.copy(status = "BOOKED"))
        verify(userRepository).save(user.copy(balance = user.balance - route.price))
    }

    @Test
    fun `should throw error when balance is insufficient`() {
        val user = User(1L, "John Doe", "john@example.com", 50.0)
        val route = Route(1L, "A", "B", 100.0)

        `when`(userRepository.findById(1L)).thenReturn(Mono.just(user))
        `when`(routeRepository.findById(1L)).thenReturn(Mono.just(route))

        val result = ticketService.bookTicket(1L, 1L)

        StepVerifier.create(result)
            .expectErrorMatches { it is IllegalStateException && it.message == "Insufficient balance" }
            .verify()

        verify(userRepository).findById(1L)
        verify(routeRepository).findById(1L)

        verify(ticketRepository, never()).save(any())
        verify(paymentService, never()).processPayment(any(Long::class.java), any(Long::class.java), any(Double::class.java))
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `should get ticket by id`() {
        val ticket = Ticket(1L, 1L, 1L, "BOOKED")

        `when`(ticketRepository.findById(1L)).thenReturn(Mono.just(ticket))

        val result = ticketService.getTicketById(1L)

        StepVerifier.create(result)
            .expectNext(ticket)
            .verifyComplete()

        verify(ticketRepository).findById(1L)
    }

    @Test
    fun `should get all tickets by user id`() {
        val ticket1 = Ticket(1L, 1L, 1L, "BOOKED")
        val ticket2 = Ticket(2L, 1L, 1L, "PENDING")

        `when`(ticketRepository.findAllByUserId(1L)).thenReturn(Flux.just(ticket1, ticket2))

        val result = ticketService.getTicketsByUserId(1L)

        StepVerifier.create(result)
            .expectNext(ticket1)
            .expectNext(ticket2)
            .verifyComplete()

        verify(ticketRepository).findAllByUserId(1L)
    }
}