package org.deep.unoserver.models

import java.lang.RuntimeException
import java.util.*

class Player(val name: String) {
    private val hand: MutableList<Card> = mutableListOf()
    val uuid: UUID = UUID.randomUUID()
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
        return uuid.hashCode() ?: 0
    }

    fun drawCard(deck: Deck) {
        if (!isReady)
            throw PlayerNotReady()
        hand.add(deck.drawCard())
    }

    fun cards(): List<Card> {
        if (!isReady)
            throw PlayerNotReady()
        return hand
    }

    fun removeCard(card: Card) {
        if (!isReady)
            throw PlayerNotReady()
        hand.remove(card)
    }
}

class CannotMakePlayerReadyTwice : RuntimeException()
class PlayerNotReady : RuntimeException()