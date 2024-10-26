package com.bca.ticketing.controller

import com.bca.ticketing.model.Route
import com.bca.ticketing.service.RouteService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/routes")
class RouteController(private val routeService: RouteService) {

    @PostMapping("/add")
    fun addRoute(
        @RequestParam origin: String,
        @RequestParam destination: String,
        @RequestParam price: Double
    ): Mono<Route> {
        return routeService.addRoute(origin, destination, price)
    }

    @GetMapping("/{routeId}")
    fun getRouteById(@PathVariable routeId: Long): Mono<Route> {
        return routeService.getRouteById(routeId)
    }

    @GetMapping
    fun getAllRoutes(): Flux<Route> {
        return routeService.getAllRoutes()
    }

    @PutMapping("/{routeId}")
    fun updateRoute(
        @PathVariable routeId: Long,
        @RequestParam(required = false) origin: String?,
        @RequestParam(required = false) destination: String?,
        @RequestParam(required = false) price: Double?
    ): Mono<Route> {
        return routeService.updateRoute(routeId, origin, destination, price)
    }

    @DeleteMapping("/{routeId}")
    fun deleteRoute(@PathVariable routeId: Long): Mono<Void> {
        return routeService.deleteRoute(routeId)
    }
}