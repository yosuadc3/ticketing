package com.bca.ticketing.repository

import com.bca.ticketing.model.Route
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface RouteRepository: ReactiveCrudRepository<Route, Long> {
}