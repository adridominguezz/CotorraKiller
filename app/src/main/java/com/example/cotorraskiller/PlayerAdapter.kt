package com.example.cotorraskiller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlayerAdapter(private val playersList: List<Jugador>) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jugadores, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount() = playersList.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val currentItem = playersList[position]

        holder.nombreJugadorRanking.text = currentItem.name.toUpperCase()
        holder.emailJugadorRanking.text = currentItem.email
        holder.puntuacionJugadorRanking.text = currentItem.cotorras.toString()

        Glide.with(holder.itemView.context)
            .load(currentItem.iconUrl)
            .into(holder.iconView)

        Glide.with(holder.itemView.context)
            .load(currentItem.imgUrl)
            .into(holder.imgView)

        // Listener para abrir PlayerDetailsActivity al hacer clic en cualquier elemento de la vista
        holder.itemView.setOnClickListener {
            openPlayerDetailsActivity(currentItem, holder.itemView.context)
        }

        // Listener para abrir OpenPhotoActivity al hacer clic en la imagen
        holder.imgView.setOnClickListener {
            openPhotoActivity(currentItem.imgUrl, holder.itemView.context)
        }
    }

    private fun openPlayerDetailsActivity(player: Jugador, context: Context) {
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        intent.putExtra("PLAYER", player)
        context.startActivity(intent)
    }

    private fun openPhotoActivity(imageUrl: String, context: Context) {
        val intent = Intent(context, OpenPhotoActivity::class.java)
        intent.putExtra("IMAGE_URL", imageUrl)
        context.startActivity(intent)
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombreJugadorRanking: TextView = itemView.findViewById(R.id.nombrePlayerRanking)
        var emailJugadorRanking: TextView = itemView.findViewById(R.id.emailPlayerRanking)
        var puntuacionJugadorRanking: TextView = itemView.findViewById(R.id.puntuacionPlayerRanking)
        var iconView: ImageView = itemView.findViewById(R.id.iconView)
        var imgView: ImageView = itemView.findViewById(R.id.imgPlayerRanking)
    }
}
