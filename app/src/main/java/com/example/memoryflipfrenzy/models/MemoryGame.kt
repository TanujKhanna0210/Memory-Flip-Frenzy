package com.example.memoryflipfrenzy.models

import com.example.memoryflipfrenzy.utils.DEFAULT_ICONS

class MemoryGame(boardSize: BoardSize) {

    val cards: List<MemoryCard>
    val numPairs = 0

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }
}