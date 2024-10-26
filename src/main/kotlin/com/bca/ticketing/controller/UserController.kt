package com.bca.ticketing.controller

import com.bca.ticketing.model.User
import com.bca.ticketing.service.UserService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestParam name: String, @RequestParam email: String): Mono<User> {
        return userService.registerUser(name, email)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): Mono<User> {
        return userService.getUserById(userId)
    }

    @GetMapping
    fun getAllUsers(): Flux<User> {
        return userService.getAllUsers()
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) email: String?
    ): Mono<User> {
        return userService.updateUser(userId, name, email)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): Mono<Void> {
        return userService.deleteUser(userId)
    }

    @PostMapping("/{userId}/balance")
    fun updateBalance(
        @PathVariable userId: Long,
        @RequestParam amount: Double
    ): Mono<User> {
        return userService.updateBalance(userId, amount)
    }
}