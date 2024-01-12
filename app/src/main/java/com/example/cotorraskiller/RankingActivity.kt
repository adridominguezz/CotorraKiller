package com.example.cotorraskiller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class RankingActivity : AppCompatActivity() {
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var rVJugadores: RecyclerView
    lateinit var adapter: Adapter
    lateinit var jugadorList: MutableList<Jugador>
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)


        mLayoutManager = LinearLayoutManager(this)
        firebaseAuth = FirebaseAuth.getInstance()
        rVJugadores = findViewById(R.id.recyclerViewJugadores)

        mLayoutManager.reverseLayout = true //Ordena de la Z a la A
        mLayoutManager.stackFromEnd =  true //Empieza desde arriba

        rVJugadores.setHasFixedSize(true)
        rVJugadores.layoutManager = mLayoutManager
        jugadorList = mutableListOf()

        ObtenerTodosLosUsuario()

    }

    private fun ObtenerTodosLosUsuario() {
        val fUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("players")


        ref.orderByChild("cotorras").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                jugadorList.clear()

                for (ds in dataSnapshot.children) {
                    var jugador = ds.getValue(Jugador::class.java)
                    jugadorList.add(jugador!!)
                }

                // Mueve la creación del adaptador fuera del bucle
                // Asegúrate de que la lista jugadorList se esté llenando correctamente antes de crear el adaptador
                // Puedes agregar un Log para verificar si la lista contiene elementos
                Toast.makeText(this@RankingActivity, "Cantidad de jugadores: ${jugadorList.size}", Toast.LENGTH_SHORT).show()


                // Asegúrate de que el adaptador se inicializa con la lista correcta
                adapter = Adapter(this@RankingActivity, jugadorList)
                rVJugadores.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RankingActivity, "Error al obtener datos: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}