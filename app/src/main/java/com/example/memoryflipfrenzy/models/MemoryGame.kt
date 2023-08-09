package com.example.memoryflipfrenzy.models

import com.example.memoryflipfrenzy.utils.DEFAULT_ICONS

class MemoryGame(
    private val boardSize: BoardSize,
    private val customImages: List<String>?
) {
    val cards: List<MemoryCard>

    var numPairsFound = 0
    private var numFlips = 0

    var indexOfSingleSelectedCard : Int? = null

    init {
        if(customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        } else {
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(), it) }
        }
    }

    fun flipCard(position: Int):Boolean {
        numFlips++
        val card = cards[position]
        // We have to take care of these THREE cases :
        // - 0 cards previously flipped over => Flip over the selected card
        // - 1 card previously flipped over =>  Flip over the selected card + Check if the cards match
        // - 2 cards previously flipped over => Restore cards + Flip over the selected card

        // The 1st case (0 cards) and the 3rd case (2 cards) are basically the same. So in the end we have to deal with 2 cases.
        var foundMatch = false
        if(indexOfSingleSelectedCard == null){
            // 0 or 2 cards previously selected
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else{
            // 1 card previously selected
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun restoreCards() {
        for(card in cards){
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound ++
        return true
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numFlips / 2
    }
}
