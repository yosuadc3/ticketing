package com.bca.ticketing

import com.bca.ticketing.service.RouteService
import com.bca.ticketing.model.Route
import com.bca.ticketing.repository.RouteRepository
import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
//import org.mockito.kotlin.argumentCaptor
//import org.mockito.kotlin.any
//import org.mockito.kotlin.eq
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class RouteServiceTest {

    private val routeRepository: RouteRepository = mock(RouteRepository::class.java)
    private val routeService: RouteService = RouteService(routeRepository)

    @Test
    fun `should add a new route`() {
        val route = Route(null, "A", "B", 100.0)
        val savedRoute = route.copy(id = 1L)

        `when`(routeRepository.save(route)).thenReturn(Mono.just(savedRoute))

        val result = routeService.addRoute("A", "B", 100.0)

        StepVerifier.create(result)
            .expectNext(savedRoute)
            .verifyComplete()

        verify(routeRepository).save(route)
    }

    @Test
    fun `should get route by id`() {
        val route = Route(1L, "A", "B", 100.0)

        `when`(routeRepository.findById(1L)).thenReturn(Mono.just(route))

        val result = routeService.getRouteById(1L)

        StepVerifier.create(result)
            .expectNext(route)
            .verifyComplete()

        verify(routeRepository).findById(1L)
    }

    @Test
    fun `should get all routes`() {
        val route1 = Route(1L, "A", "B", 100.0)
        val route2 = Route(2L, "B", "C", 150.0)

        `when`(routeRepository.findAll()).thenReturn(Flux.just(route1, route2))

        val result = routeService.getAllRoutes()

        StepVerifier.create(result)
            .expectNext(route1)
            .expectNext(route2)
            .verifyComplete()

        verify(routeRepository).findAll()
    }

    @Test
    fun `should update route`() {
        val existingRoute = Route(1L, "A", "B", 100.0)
        val updatedRoute = existingRoute.copy(price = 120.0)

        `when`(routeRepository.findById(1L)).thenReturn(Mono.just(existingRoute))
        `when`(routeRepository.save(updatedRoute)).thenReturn(Mono.just(updatedRoute))

        val result = routeService.updateRoute(1L, null, null, 120.0)

        StepVerifier.create(result)
            .expectNext(updatedRoute)
            .verifyComplete()

        verify(routeRepository).findById(1L)
        verify(routeRepository).save(updatedRoute)
    }

    @Test
    fun `should delete route by id`() {
        `when`(routeRepository.deleteById(1L)).thenReturn(Mono.empty())

        val result = routeService.deleteRoute(1L)

        StepVerifier.create(result)
            .verifyComplete()

        verify(routeRepository).deleteById(1L)
    }
}