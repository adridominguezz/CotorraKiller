package com.example.cotorraskiller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.database.ValueEventListener

class Adapter(context: Context, listaUsuarios: MutableList<Jugador>) : RecyclerView.Adapter<com.example.cotorraskiller.Adapter.MyHolder>() {

    private lateinit var context: Context
    private lateinit var listaUsuarios: MutableList<Jugador>

    init {
        this.context = context
        this.listaUsuarios = listaUsuarios
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombreJugadorRanking: TextView = itemView.findViewById(R.id.nombrePlayerRanking)
        var emailJugadorRanking: TextView = itemView.findViewById(R.id.emailPlayerRanking)
        var puntuacionJugadorRanking: TextView = itemView.findViewById(R.id.puntuacionPlayerRanking)
    }

    //Llamamos/inflamos el diseño
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.jugadores, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        //Obtener los datos del modelo
        val nombre = listaUsuarios[position].nombre
        val email = listaUsuarios[position].email
        val puntuacion = listaUsuarios[position].cotorras.toString()

        // Obtenemos los datos del jugador
        holder.nombreJugadorRanking.text = nombre
        holder.emailJugadorRanking.text = email
        holder.puntuacionJugadorRanking.text = puntuacion
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }
}
