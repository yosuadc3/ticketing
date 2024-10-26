package com.bca.ticketing.service

import com.bca.ticketing.model.User
import com.bca.ticketing.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Service
class UserService(private val userRepository: UserRepository) {

    fun registerUser(name: String, email: String): Mono<User> {
        val user = User(null, name, email, 0.0)
        return userRepository.save(user)
    }

    fun getUserById(userId: Long): Mono<User> {
        return userRepository.findById(userId)
    }

    fun getAllUsers(): Flux<User> {
        return userRepository.findAll()
    }

    fun updateUser(userId: Long, name: String?, email: String?): Mono<User> {
        return userRepository.findById(userId)
            .flatMap { user ->
                val updatedUser = user.copy(
                    name = name ?: user.name,
                    email = email ?: user.email
                )
                userRepository.save(updatedUser)
            }
    }

    fun deleteUser(userId: Long): Mono<Void> {
        return userRepository.deleteById(userId)
    }

    fun updateBalance(userId: Long, amount: Double): Mono<User> {
        return userRepository.findById(userId)
            .flatMap { user ->
                val updatedUser = user.copy(balance = user.balance + amount)
                userRepository.save(updatedUser)
            }
    }
}