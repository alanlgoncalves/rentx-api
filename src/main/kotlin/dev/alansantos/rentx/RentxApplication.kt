package dev.alansantos.rentx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RentxApplication

fun main(args: Array<String>) {
    runApplication<RentxApplication>(*args)
}
