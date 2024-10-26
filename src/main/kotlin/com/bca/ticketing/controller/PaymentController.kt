package com.bca.ticketing.controller

import com.bca.ticketing.model.Payment
import com.bca.ticketing.service.PaymentService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/payments")
class PaymentController(private val paymentService: PaymentService) {

    @PostMapping("/process")
    fun processPayment(
        @RequestParam userId: Long,
        @RequestParam ticketId: Long,
        @RequestParam amount: Double
    ): Mono<Payment> {
        return paymentService.processPayment(userId, ticketId, amount)
    }

    @GetMapping("/{paymentId}")
    fun getPaymentById(@PathVariable paymentId: Long): Mono<Payment> {
        return paymentService.getPaymentById(paymentId)
    }

    @GetMapping("/ticket/{ticketId}")
    fun getPaymentsByTicketId(@PathVariable ticketId: Long): Flux<Payment> {
        return paymentService.getPaymentsByTicketId(ticketId)
    }

    @PostMapping("/refund/{paymentId}")
    fun refundPayment(@PathVariable paymentId: Long): Mono<Payment> {
        return paymentService.refundPayment(paymentId)
    }
}