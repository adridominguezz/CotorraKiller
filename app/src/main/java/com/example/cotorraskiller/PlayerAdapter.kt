package com.example.cotorraskiller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter (private val playersList:List<Jugador>): RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.jugadores, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount() = playersList.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val currentItem = playersList[position]

        holder.nombreJugadorRanking.text = currentItem.name.toUpperCase()
        holder.emailJugadorRanking.text = currentItem.email
        holder.puntuacionJugadorRanking.text = currentItem.cotorras.toString()
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombreJugadorRanking: TextView = itemView.findViewById(R.id.nombrePlayerRanking)
        var emailJugadorRanking: TextView = itemView.findViewById(R.id.emailPlayerRanking)
        var puntuacionJugadorRanking: TextView = itemView.findViewById(R.id.puntuacionPlayerRanking)
    }
}