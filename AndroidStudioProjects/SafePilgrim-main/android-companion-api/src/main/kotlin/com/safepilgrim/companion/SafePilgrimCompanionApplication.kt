package com.safepilgrim.companion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SafePilgrimCompanionApplication

fun main(args: Array<String>) {
    runApplication<SafePilgrimCompanionApplication>(*args)
}
