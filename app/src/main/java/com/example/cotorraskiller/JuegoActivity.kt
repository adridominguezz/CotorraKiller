package com.example.cotorraskiller

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Integer.max
import java.lang.Math.min
import java.util.HashMap
import java.util.Random

class JuegoActivity : AppCompatActivity() {

    lateinit var UID: String
    lateinit var NOMBRE: String
    var COTORRA: Long = 0
    lateinit var imgCotorra: ImageView
    lateinit var contadorKills: TextView
    lateinit var nombreJugador: TextView
    lateinit var tiempo: TextView
    var velocidadCotorra: Long = 500

    var AnchoPantalla: Int =0
    var AltoPantalla: Int =0

    lateinit var AltoTV: TextView
    lateinit var AnchoTV: TextView

    lateinit var random: Random

    var GameOver = false
    lateinit var miDialog: Dialog

    lateinit var firebaseAuth: FirebaseAuth
    //lateinit var user: FirebaseUser
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var players: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        imgCotorra = findViewById(R.id.imgCotorra)
        contadorKills = findViewById(R.id.contadorKills)
        nombreJugador = findViewById(R.id.nombreTextView)
        tiempo = findViewById(R.id.TiempoTextView)
        AltoTV = findViewById(R.id.AltoTV)
        AnchoTV = findViewById(R.id.AnchoTV)

        miDialog = Dialog(this)

        firebaseAuth = FirebaseAuth.getInstance()
       // user = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        players = firebaseDatabase.getReference("players")

        val intent: Bundle? = intent.extras
        UID = intent?.getString("UID").toString()
        NOMBRE = intent?.getString("NAME").toString()
//        var contador = intent?.getString("COTORRAS").toString()
//        COTORRA = contador.toLong()
        COTORRA = 0

        contadorKills.text = COTORRA.toString()
        nombreJugador.text = NOMBRE

        Pantalla()
        CuentaAtras()


        imgCotorra.setOnClickListener(){
            if (!GameOver) {
                COTORRA++
                contadorKills.text = COTORRA.toString()

                imgCotorra.setImageResource(R.drawable.cotorra_dead)

                Handler().postDelayed({
                    imgCotorra.setImageResource(R.drawable.cotorra)
                    Movimiento()
                }, velocidadCotorra)
            }
        }

    }
    private fun Pantalla(){ //Metodo para obtener el tamaño de la pantalla
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)

        AnchoPantalla = point.x
        AltoPantalla = point.y

        var ancho = AnchoPantalla.toString()
        var alto = AltoPantalla.toString()

        AnchoTV.text = ancho
        AltoTV.text = alto

        random = Random()
    }

    private fun Movimiento() {
        val min = 0
        val maxX = AnchoPantalla - (imgCotorra.width*2)
        val maxY = AltoPantalla - (imgCotorra.height*2)

        val randomX = random.nextInt((maxX - min) + 1) + min
        val randomY = random.nextInt((maxY - min) + 1) + min

        val newY = if (randomY < 0) 0 else randomY // Asegurar que no sea menor que 0

        imgCotorra.x = randomX.toFloat()
        imgCotorra.y = newY.toFloat()
    }

    private fun CuentaAtras(){
        object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var segundosRestantes : Long = millisUntilFinished/1000
                when {
                    segundosRestantes > 5000 -> velocidadCotorra = 200
                    segundosRestantes > 10000 -> velocidadCotorra = 250
                    segundosRestantes > 15000 -> velocidadCotorra = 300
                    segundosRestantes > 20000 -> velocidadCotorra = 350
                    segundosRestantes > 25000 -> velocidadCotorra = 400
                }
                tiempo.setText(""+ segundosRestantes + "S")
            }

            override fun onFinish() {
                tiempo.setText("0S")
                GameOver = true
                MensajeGameOver()
                guardarResultados(COTORRA)
            }
        }.start()
    }

    private fun MensajeGameOver() {

        var cotorrasMuertas: TextView
        var cotorrasM = COTORRA.toString()

        val dialogView = layoutInflater.inflate(R.layout.gameover, null)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.hasMatado)
        dialogMessage.text = "Has matado $cotorrasM cotorras"


        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.tiempoTerminado))
            .setMessage(resources.getString(R.string.hasMatado) + " " + cotorrasM + " cotorras")
            .setNeutralButton(resources.getString(R.string.juegarDeNuevo)) { dialog, which ->
                COTORRA = 0
                dialog.dismiss()
                contadorKills.text = "0"
                GameOver = false
                CuentaAtras()
                Movimiento()
            }
            .setNegativeButton(resources.getString(R.string.irMenu)) { dialog, which ->
                startActivity(Intent(this, MenuActivity::class.java))
            }
            .setPositiveButton(resources.getString(R.string.irRanking)) { dialog, which ->
                startActivity(Intent(this, RankingActivity::class.java))
            }
            .show()

    }

    private fun guardarResultados(cotorras: Long) {

        val user = firebaseAuth.currentUser
        val uid = user?.uid
        val contextView = findViewById<View>(R.id.layoutJuego)

        if (uid != null) {
            val uidString = uid.toString()
            val database = FirebaseFirestore.getInstance()

            val userRef = database.collection("players").document(uidString)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val cotorrasActual = documentSnapshot.getLong("cotorras") ?: 0
                        if (cotorras > cotorrasActual.toInt()) {
                            userRef.update("cotorras", cotorras)
                                .addOnSuccessListener {
                                    Snackbar.make(
                                        contextView,
                                        "Puntuaje actualizado",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Snackbar.make(
                                        contextView,
                                        "Error al actualizar cotorras",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Snackbar.make(
                                contextView,
                                "No has superado tu record. ¡Intentalo de nuevo!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Snackbar.make(contextView, "Usuario no encontrado", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
    }

