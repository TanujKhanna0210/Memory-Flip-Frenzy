package com.example.memoryflipfrenzy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memoryflipfrenzy.models.BoardSize
import com.example.memoryflipfrenzy.models.MemoryCard
import com.example.memoryflipfrenzy.models.MemoryGame
import com.example.memoryflipfrenzy.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private lateinit var clRoot : ConstraintLayout
    private lateinit var rvBoard : RecyclerView
    private lateinit var tvNumMoves : TextView
    private lateinit var tvNumPairs : TextView

    private lateinit var memoryGame : MemoryGame
    private lateinit var adapter : MemoryGameAdapter
    private var boardSize : BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryGameAdapter(this, boardSize, memoryGame.cards, object: MemoryGameAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateGameWithFlip(position: Int) {
        // Error checking
        // 2 error cases(invalid moves) that can occur are :-
        // - If the player has already won but still tries to flip a card
        // - If the player tries to flip a card that is already face up
        if(memoryGame.haveWonGame()){
            // Alert the user of an invalid move
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            // Alert the user of an invalid move
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
            return
        }

        // Actually flipping over the card :
        if(memoryGame.flipCard(position)){
            Log.i(TAG, "Found a match! Number of pairs found : ${memoryGame.numPairsFound}")
        }
        adapter.notifyDataSetChanged()
    }
}