package com.example.memoryflipfrenzy

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class MemoryGameAdapter(private val context: Context, private val numPieces: Int) :
    RecyclerView.Adapter<MemoryGameAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN = 10
        private const val TAG = "MemoryGameAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = parent.width / 2 - (2 * MARGIN)
        val cardHeight = parent.height / 4 - (2 * MARGIN)
        val cardSideLength = min(cardWidth, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN, MARGIN, MARGIN, MARGIN)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = numPieces

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int){
            Log.i(TAG, "Clicked on card $position")
        }
    }
}
