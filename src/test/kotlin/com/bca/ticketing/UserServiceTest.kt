package com.bca.ticketing

import com.bca.ticketing.service.UserService
import com.bca.ticketing.model.User
import com.bca.ticketing.repository.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    init {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should register a user`() {
        val user = User(null, "John Doe", "john@example.com", 0.0)

        `when`(userRepository.save(user)).thenReturn(Mono.just(user))

        val registeredUser = userService.registerUser("John Doe", "john@example.com")

        StepVerifier.create(registeredUser)
            .expectNext(user)
            .verifyComplete()

        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `should get user by id`() {
        val user = User(id = 1, name = "Jane Doe", email = "jane@example.com", balance = 150.0)

        `when`(userRepository.findById(1)).thenReturn(Mono.just(user))

        val retrievedUser = userService.getUserById(1)

        StepVerifier.create(retrievedUser)
            .expectNext(user)
            .verifyComplete()

        verify(userRepository, times(1)).findById(1)
    }

    @Test
    fun `should return all users`() {
        val user1 = User(id = 1, name = "Alice", email = "alice@example.com", balance = 200.0)
        val user2 = User(id = 2, name = "Bob", email = "bob@example.com", balance = 300.0)
        val users = Flux.just(user1, user2)

        `when`(userRepository.findAll()).thenReturn(users)

        val retrievedUsers = userService.getAllUsers()

        StepVerifier.create(retrievedUsers)
            .expectNext(user1, user2)
            .verifyComplete()

        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `should update a user`() {
        val existingUser = User(id = 1, name = "Jane Doe", email = "jane@example.com", balance = 150.0)
        val updatedUser = existingUser.copy(name = "Jane Smith")

        `when`(userRepository.findById(1)).thenReturn(Mono.just(existingUser))
        `when`(userRepository.save(updatedUser)).thenReturn(Mono.just(updatedUser))

        val result = userService.updateUser(1, "Jane Smith", null)

        StepVerifier.create(result)
            .expectNext(updatedUser)
            .verifyComplete()

        verify(userRepository, times(1)).findById(1)
        verify(userRepository, times(1)).save(updatedUser)
    }

    @Test
    fun `should delete a user`() {
        val userId = 1L

        `when`(userRepository.deleteById(userId)).thenReturn(Mono.empty())

        val result = userService.deleteUser(userId)

        StepVerifier.create(result)
            .verifyComplete()

        verify(userRepository, times(1)).deleteById(userId)
    }

    @Test
    fun `should update user balance`() {
        val existingUser = User(id = 1, name = "John Doe", email = "john@example.com", balance = 100.0)
        val updatedUser = existingUser.copy(balance = existingUser.balance + 50.0)

        `when`(userRepository.findById(1)).thenReturn(Mono.just(existingUser))
        `when`(userRepository.save(updatedUser)).thenReturn(Mono.just(updatedUser))

        val result = userService.updateBalance(1, 50.0)

        StepVerifier.create(result)
            .expectNext(updatedUser)
            .verifyComplete()

        verify(userRepository, times(1)).findById(1)
        verify(userRepository, times(1)).save(updatedUser)
    }
}