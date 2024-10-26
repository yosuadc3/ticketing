package com.bca.ticketing.service

import com.bca.ticketing.model.Payment
import com.bca.ticketing.repository.PaymentRepository
import com.bca.ticketing.repository.TicketRepository
import com.bca.ticketing.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class PaymentService(private val paymentRepository: PaymentRepository, private val ticketRepository: TicketRepository, private val userRepository: UserRepository,) {

    fun processPayment(userId: Long, ticketId: Long, amount: Double): Mono<Payment> {
        val payment = Payment(
            id = null,
            ticketId = ticketId,
            userId = userId,
            amount = amount,
            status = "PAID"
        )
        return paymentRepository.save(payment)
    }
    fun getPaymentById(paymentId: Long): Mono<Payment> {
        return paymentRepository.findById(paymentId)
    }

    fun getPaymentsByTicketId(ticketId: Long): Flux<Payment> {
        return paymentRepository.findAllByTicketId(ticketId)
    }

    fun refundPayment(paymentId: Long): Mono<Payment> {
        return paymentRepository.findById(paymentId)
            .flatMap { payment ->
                val refundedPayment = payment.copy(status = "REFUNDED")
                paymentRepository.save(refundedPayment)
            }
    }
}