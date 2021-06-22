package org.deep.unoserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UnoServerApplication

fun main(args: Array<String>) {
    runApplication<UnoServerApplication>(*args)
}
