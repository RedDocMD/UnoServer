package org.deep.unoserver.models

sealed class Move

class DrawMove(val noOfCards: Int) : Move()
class PlayMove(val card: Card) : Move()
object SkipMove : Move()
object ChoseColorNextMove : Move()
class ChoseColorMove(val color: Color) : Move()