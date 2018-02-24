package kittentrate.ui.game

/**
 * Created by Manuel Lorenzo on 20/03/2017.
 */

class Game {
    var score: Int = 0

    init {
        score = 0
    }

    fun increaseScore() {
        score += 1
    }

    fun decreaseScore() {
        score -= 1
    }

    fun resetScore() {
        score = 0
    }
}
