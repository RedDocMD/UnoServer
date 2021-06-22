package org.deep.unoserver.models

import java.lang.RuntimeException
import java.util.*

class Player(val name: String) {
    private val hand: MutableList<Card> = mutableListOf()
    val uuid = UUID.randomUUID()
    var isReady = false

    fun initPlayer(deck: Deck) {
        if (isReady) {
            throw CannotMakePlayerReadyTwice()
        }
        isReady = true
        for (i in 1..7)
            hand.add(deck.drawCard())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid?.hashCode() ?: 0
    }
}

class CannotMakePlayerReadyTwice : RuntimeException()