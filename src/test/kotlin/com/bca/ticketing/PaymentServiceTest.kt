package com.bca.ticketing

import com.bca.ticketing.model.Payment
import com.bca.ticketing.repository.PaymentRepository
import com.bca.ticketing.repository.TicketRepository
import com.bca.ticketing.repository.UserRepository
import com.bca.ticketing.service.PaymentService
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
class PaymentServiceTest {

    private lateinit var paymentService: PaymentService

    @Mock
    private lateinit var paymentRepository: PaymentRepository

    @Mock
    private lateinit var ticketRepository: TicketRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        paymentService = PaymentService(paymentRepository, ticketRepository, userRepository)
    }

    @Test
    fun `should process payment successfully`() {
        val userId = 1L
        val ticketId = 1L
        val amount = 100.0
        val payment = Payment(null, ticketId, userId, amount, "PAID")

        `when`(paymentRepository.save(any())).thenReturn(Mono.just(payment))

        val result = paymentService.processPayment(userId, ticketId, amount)

        StepVerifier.create(result)
            .expectNext(payment)
            .verifyComplete()

        verify(paymentRepository).save(any(Payment::class.java))
    }

    @Test
    fun `should get payment by ID`() {
        val paymentId = 1L
        val payment = Payment(paymentId, 1L, 1L, 100.0, "PAID")

        `when`(paymentRepository.findById(paymentId)).thenReturn(Mono.just(payment))

        val result = paymentService.getPaymentById(paymentId)

        StepVerifier.create(result)
            .expectNext(payment)
            .verifyComplete()

        verify(paymentRepository).findById(paymentId)
    }

    @Test
    fun `should return empty when payment not found by ID`() {
        val paymentId = 1L

        `when`(paymentRepository.findById(paymentId)).thenReturn(Mono.empty())

        val result = paymentService.getPaymentById(paymentId)

        StepVerifier.create(result)
            .verifyComplete()

        verify(paymentRepository).findById(paymentId)
    }

    @Test
    fun `should get payments by ticket ID`() {
        val ticketId = 1L
        val payments = listOf(
            Payment(1L, ticketId, 1L, 100.0, "PAID"),
            Payment(2L, ticketId, 2L, 150.0, "PAID")
        )

        `when`(paymentRepository.findAllByTicketId(ticketId)).thenReturn(Flux.fromIterable(payments))

        val result = paymentService.getPaymentsByTicketId(ticketId)

        StepVerifier.create(result)
            .expectNextCount(payments.size.toLong())
            .verifyComplete()

        verify(paymentRepository).findAllByTicketId(ticketId)
    }

    @Test
    fun `should refund payment successfully`() {
        val paymentId = 1L
        val payment = Payment(paymentId, 1L, 1L, 100.0, "PAID")
        val refundedPayment = payment.copy(status = "REFUNDED")

        `when`(paymentRepository.findById(paymentId)).thenReturn(Mono.just(payment))
        `when`(paymentRepository.save(refundedPayment)).thenReturn(Mono.just(refundedPayment))

        val result = paymentService.refundPayment(paymentId)

        StepVerifier.create(result)
            .expectNext(refundedPayment)
            .verifyComplete()

        verify(paymentRepository).findById(paymentId)
        verify(paymentRepository).save(refundedPayment)
    }

    @Test
    fun `should return empty when refund payment not found`() {
        val paymentId = 1L

        `when`(paymentRepository.findById(paymentId)).thenReturn(Mono.empty())

        val result = paymentService.refundPayment(paymentId)

        StepVerifier.create(result)
            .verifyComplete()

        verify(paymentRepository).findById(paymentId)
        verify(paymentRepository, never()).save(any())
    }
}