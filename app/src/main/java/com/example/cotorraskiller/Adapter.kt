package com.example.cotorraskiller

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

public class Adapter: RecyclerView.Adapter<Adapter.MyHolder>() {

    private lateinit var context: Context
    private lateinit var listaUsuarios: List<Jugador>



//    constructor(context: Context, listaUsuarios: List<Jugador>) : super() {
//        this.context = context
//        this.listaUsuarios = listaUsuarios
//    }


    public class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombreJugadorRanking: TextView = itemView.findViewById(R.id.nombrePlayerRanking)
        var emailJugadorRanking: TextView = itemView.findViewById(R.id.emailPlayerRanking)
        var puntuacionJugadorRanking: TextView = itemView.findViewById(R.id.puntuacionPlayerRanking )



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.MyHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: Adapter.MyHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}