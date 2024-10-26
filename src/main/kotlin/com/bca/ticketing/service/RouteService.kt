package com.bca.ticketing.service

import com.bca.ticketing.model.Route
import com.bca.ticketing.repository.RouteRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class RouteService(private val routeRepository: RouteRepository) {

    fun addRoute(origin: String, destination: String, price: Double): Mono<Route> {
        val route = Route(null, origin, destination, price)
        return routeRepository.save(route)
    }

    fun getRouteById(routeId: Long): Mono<Route> {
        return routeRepository.findById(routeId)
    }

    fun getAllRoutes(): Flux<Route> {
        return routeRepository.findAll()
    }

    fun updateRoute(routeId: Long, origin: String?, destination: String?, price: Double?): Mono<Route> {
        return routeRepository.findById(routeId)
            .flatMap { route ->
                val updatedRoute = route.copy(
                    origin = origin ?: route.origin,
                    destination = destination ?: route.destination,
                    price = price ?: route.price
                )
                routeRepository.save(updatedRoute)
            }
    }

    fun deleteRoute(routeId: Long): Mono<Void> {
        return routeRepository.deleteById(routeId)
    }
}