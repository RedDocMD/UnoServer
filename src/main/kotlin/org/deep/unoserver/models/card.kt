package org.deep.unoserver.models

import java.util.*

sealed class Card {
    val uuid: UUID = UUID.randomUUID()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}

enum class Color {
    RED, GREEN, BLUE, YELLOW
}

sealed class ColoredCard(val color: Color) : Card()
class NumberCard(val number: Int, color: Color) : ColoredCard(color)
class ActionCard(val action: Action, color: Color) : ColoredCard(color)

enum class Action {
    SKIP, DRAW_TWO, REVERSE
}

class WildDrawCard : Card()
class WildDrawFourCard : Card()

class Deck(private val cards: MutableList<Card> = mutableListOf()) {
    private val randGen = Random()

    init {
        if (cards.isEmpty()) {
            for (i in 1..4) {
                cards.add(WildDrawCard())
                cards.add(WildDrawFourCard())
            }
            enumValues<Color>().forEach { color ->
                for (i in 1..9) {
                    cards.add(NumberCard(i, color))
                    cards.add(NumberCard(i, color))
                }
                cards.add(NumberCard(0, color))
                enumValues<Action>().forEach { action ->
                    cards.add(ActionCard(action, color))
                    cards.add(ActionCard(action, color))
                }
            }
        }

        cards.shuffle()
    }

    fun drawCard(): Card {
        val idx = randGen.nextInt(cards.size)
        return cards.removeAt(idx)
    }

    fun drawTop(): Card {
        return cards.removeLast()
    }

    fun returnAndShuffle(card: Card) {
        cards.add(card)
        cards.shuffle()
    }
}

class DiscardPile {
    private val pile: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        pile.add(card)
    }

    fun top() = pile.last()

    fun createDeck(): Deck {
        val topCard = top()
        pile.removeLast()
        val deck = Deck(pile.toMutableList())
        pile.clear()
        pile.add(topCard)
        return deck
    }
}