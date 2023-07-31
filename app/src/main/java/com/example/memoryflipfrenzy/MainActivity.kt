package com.example.memoryflipfrenzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryflipfrenzy.models.BoardSize
import com.example.memoryflipfrenzy.models.MemoryCard
import com.example.memoryflipfrenzy.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

    private lateinit var rvBoard : RecyclerView
    private lateinit var tvNumMoves : TextView
    private lateinit var tvNumPairs : TextView

    private var boardSize : BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        val memoryCards = randomizedImages.map { MemoryCard(it) }

        rvBoard.adapter = MemoryGameAdapter(this, boardSize, memoryCards)
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }
}