package com.example.cotorraskiller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class RankingActivity : AppCompatActivity() {


    //OBTENER LA LISTA DE JUGADORES
    val playersCollection = FirebaseFirestore.getInstance().collection("players") // Definir la colección de Firebase
    val playerList: ArrayList<Jugador> = ArrayList() // Crear la lista para almacenar los jugadores

    //HACER EL RECYCLERVIEW
    lateinit var recyclerView: RecyclerView
    lateinit var playerAdapter: PlayerAdapter

    lateinit var iconView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        listarJugadores()


    }

    private fun listarJugadores(){
        playersCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                // Obtener campos específicos del documento
                val uid = document.getString("uid") ?: ""
                val name = document.getString("name") ?: ""
                val email = document.getString("email") ?: ""
                val cotorras = document.getLong("cotorras") ?: 0
                val imgUrl = document.getString("imagen") ?: ""
                val iconUrl = document.getString("icon") ?: ""

                // Crear un objeto Jugador con los campos deseados
                val jugador = Jugador(uid, name, email, cotorras, iconUrl, imgUrl)

                // Agregar el jugador a la lista
                playerList.add(jugador)
            }
            // Ordenar la lista de jugadores por el número de cotorras de mayor a menor
            playerList.sortByDescending { it.cotorras }

            rellenarRecycler(playerList)

        }.addOnFailureListener { exception ->
            // Manejar errores si es necesario
            println("Error getting players: $exception")
        }
    }

    private fun rellenarRecycler(playerList: ArrayList<Jugador>) {
        recyclerView = findViewById(R.id.recyclerViewJugadores)
        recyclerView.layoutManager = LinearLayoutManager(this)
        playerAdapter = PlayerAdapter(playerList)
        recyclerView.adapter = playerAdapter
    }


    fun volver(view: View) {
        startActivity(Intent(this, MenuActivity::class.java))
    }

}