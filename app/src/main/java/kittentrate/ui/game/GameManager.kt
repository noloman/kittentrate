package kittentrate.ui.game

/**
 * Created by Manuel Lorenzo on 21/03/2017.
 */

//class GameManager(private val gameViewModel: GameViewModel) {
//    private val facingUpCardsWeakHashMap = WeakHashMap<Int, Card>(Constants.NUMBER_MATCHING_CARDS)
//    private val gameObservable: Observable<Game> = Observable.just(Game())
//
//    val gameScore: Observable<Int>
//        get() = gameObservable.map { t: Game -> t.score }
//
//    fun cardFlipped(position: Int, card: Card) {
//        if (!facingUpCardsWeakHashMap.containsKey(position)) {
//            facingUpCardsWeakHashMap[position] = card
//            if (shouldCheckForCardsMatch()) {
//                // This would be the last card, we should check for matches.
//                val id = card.id
//                var matchFound = true
//                for ((_, loopCard) in facingUpCardsWeakHashMap) {
//                    if (loopCard.id != id) {
//                        matchFound = false
//                    }
//                }
//                if (matchFound) {
//                    // Match found; remove MATCHED cards from data set
//                    gameViewModel.removeViewFlipper()
//                    for ((_, card1) in facingUpCardsWeakHashMap) {
//                        gameViewModel.notifyAdapterItemRemoved(card1.id)
//                    }
//                    facingUpCardsWeakHashMap.clear()
//                    increaseGameScore()
//                    gameViewModel.removeViewFlipper()
//                } else {
//                    // Match not found; turn cards over and start from the beginning.
//                    decreaseGameScore()
//                    gameViewModel.onTurnCardsOver()
//                }
//                //gameViewModel.onGameScoreChanged(gameScore)
//            }
//        }
//    }
//
//    fun removeCardsFromMap() {
//        for ((key) in facingUpCardsWeakHashMap) {
//            gameViewModel.notifyAdapterItemChanged(key)
//        }
//        facingUpCardsWeakHashMap.clear()
//    }
//
//    fun shouldDispatchUiEvent(): Boolean {
//        return facingUpCardsWeakHashMap.size != Constants.NUMBER_MATCHING_CARDS
//    }
//
//    fun resetScore() {
//        resetGameScore()
//    }
//
//    private fun shouldCheckForCardsMatch(): Boolean {
//        return facingUpCardsWeakHashMap.size == Constants.NUMBER_MATCHING_CARDS
//    }
//
//    private fun increaseGameScore() {
//        gameObservable.map { t: Game -> t.score += 1 }
//    }
//
//    private fun decreaseGameScore() {
//        gameObservable.map { t: Game -> t.score -= 1 }
//    }
//
//    private fun resetGameScore() {
//        gameObservable.map { t: Game -> t.score = 0 }
//    }
//}
