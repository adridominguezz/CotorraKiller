package com.example.cotorraskiller

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MenuActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var players: DatabaseReference

    lateinit var btnCerrarSesion: Button
    lateinit var btnJugar: Button
    lateinit var btnRanking: Button
    lateinit var txtCotorras: TextView
    lateinit var txtName: TextView
    lateinit var txtEmail: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        firebaseDatabase = FirebaseDatabase.getInstance()
        players = firebaseDatabase.getReference("players")


        btnCerrarSesion = findViewById(R.id.CerrarSesionBtn)
        btnJugar = findViewById(R.id.JugarBtn)
        btnRanking = findViewById(R.id.rankingBtn)
        txtCotorras = findViewById(R.id.cotorrasTxt)
        txtEmail = findViewById(R.id.userEmailTxt)
        txtName = findViewById(R.id.userNameTxt)

        btnCerrarSesion.setOnClickListener { cerrarSesion() }
        btnJugar.setOnClickListener { Toast.makeText(this, "JUGAR", Toast.LENGTH_SHORT).show()}
        btnRanking.setOnClickListener { Toast.makeText(this, "RANKING", Toast.LENGTH_SHORT).show() }

    }

    override fun onStart() {
        userLog()
        super.onStart()
    }

    private fun userLog(){ //Metodo que comprueba si hay un usuario con la sesión iniciada
        if (user == null) {
            // Si el usuario no está autenticado, redirige a la actividad de inicio de sesión
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Termina la actividad actual para evitar volver atrás a esta pantalla sin autenticación
        } else {
            consulta()
            Toast.makeText(this, "Usuario en línea", Toast.LENGTH_SHORT).show()

        }
    }

    private fun cerrarSesion(){
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java ))
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    private fun consulta(){
        val query: Query = players.orderByChild("email").equalTo(user!!.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { ds ->
                    var cotorrasString = ""+ds.child("cotorras").value
                    var nameString = ""+ds.child("name").value
                    var emailString = ""+ds.child("email").value

                    txtCotorras.text = cotorrasString
                    txtEmail.text = emailString
                    txtName.text = nameString

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Aquí va la lógica para manejar errores de lectura de datos
            }
        })

    }

}