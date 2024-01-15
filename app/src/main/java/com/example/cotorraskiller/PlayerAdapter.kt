package com.example.cotorraskiller

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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

        Glide.with(holder.itemView.context)
            .load(currentItem.iconUrl)  // Reemplaza "imageUrl" con la propiedad que contiene la URL de la imagen en tu modelo Jugador
            .into(holder.iconView)

        Glide.with(holder.itemView.context)
            .load(currentItem.imgUrl)  // Reemplaza "imageUrl" con la propiedad que contiene la URL de la imagen en tu modelo Jugador
            .into(holder.imgView)


    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombreJugadorRanking: TextView = itemView.findViewById(R.id.nombrePlayerRanking)
        var emailJugadorRanking: TextView = itemView.findViewById(R.id.emailPlayerRanking)
        var puntuacionJugadorRanking: TextView = itemView.findViewById(R.id.puntuacionPlayerRanking)
        var iconView: ImageView = itemView.findViewById(R.id.iconView)
        var imgView: ImageView = itemView.findViewById(R.id.imgPlayerRanking)

        fun render(){
            iconView.setOnClickListener{
                //val intent = Intent(iconView.context).
            }

            imgView.setOnClickListener{

            }
        }

    }
}