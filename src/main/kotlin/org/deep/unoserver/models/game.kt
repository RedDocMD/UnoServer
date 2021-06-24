package org.deep.unoserver.models

object GameConstants {
    const val NO_PLAYERS = 4
}

enum class PlayDirection {
    CLOCKWISE, ANTICLOCKWISE
}

// TODO: Separate GameState and GameController
class GameState(
    val players: Array<Player>,
    private var _nextPlayer: Int,
    private var _direction: PlayDirection,
    private var _deck: Deck,
    val pile: DiscardPile,
) {
    val nextPlayer: Int
        get() = _nextPlayer
    val direction: PlayDirection
        get() = _direction
    val deck: Deck
        get() = _deck
    var nextColor: Color? = null
    private val nextPlayerMoves: MutableList<Move> = mutableListOf()

    init {
        if (players.size != GameConstants.NO_PLAYERS) {
            throw IncorrectNoOfPlayers()
        }
    }

    fun flipDirection() {
        _direction = if (_direction == PlayDirection.CLOCKWISE)
            PlayDirection.ANTICLOCKWISE
        else
            PlayDirection.CLOCKWISE
    }
}

class GameController(val names: Array<String>) {
    private val state: GameState

    init {
        val players = Array(names.size) { Player(names[it]) }
        val deck = Deck()
        val pile = DiscardPile()
        for (player in players)
            player.initPlayer(deck)

        var topCard = deck.drawTop()
        while (topCard is WildDrawFourCard) {
            deck.returnAndShuffle(topCard)
            topCard = deck.drawCard()
        }
        pile.addCard(topCard)

        val nextMoves = mutableListOf<Move>()
        val (nextPlayer, direction) = when (topCard) {
            is ActionCard -> {
                when (topCard.action) {
                    Action.SKIP -> Pair(2, PlayDirection.CLOCKWISE)
                    Action.DRAW_TWO -> {
                        nextMoves.addAll(listOf(DrawMove(2), SkipMove))
                        Pair(1, PlayDirection.CLOCKWISE)
                    }
                    Action.REVERSE -> Pair(0, PlayDirection.ANTICLOCKWISE)
                }
            }
            is NumberCard -> Pair(1, PlayDirection.CLOCKWISE)
            is WildDrawCard -> {
                nextMoves.add(ChoseColorNextMove)
                Pair(1, PlayDirection.CLOCKWISE)
            }
            is WildDrawFourCard -> TODO("this branch is unreachable")
        }

        state = GameState(players, nextPlayer, direction, deck, pile)
    }

    fun handleMovesOfOneRound(moves: List<Move>) {

    }

    private fun handleMove(move: Move): Boolean {
        val player = state.players[state.nextPlayer]
        when (move) {
            is DrawMove -> {
                for (i in 1..move.noOfCards)
                    player.drawCard(state.deck)
            }
            is ChoseColorMove -> state.nextColor = move.color
            ChoseColorNextMove -> {
                // Ignore
            }
            is PlayMove -> {
                val card = move.card
                player.removeCard(card)
                when (card) {
                    is ActionCard -> TODO()
                    is NumberCard -> TODO()
                    is WildDrawCard -> TODO()
                    is WildDrawFourCard -> TODO()
                }
            }
            SkipMove -> return false
        }
        return true
    }
}

class IncorrectNoOfPlayers : RuntimeException()