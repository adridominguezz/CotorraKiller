package com.example.cotorraskiller

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Random

class JuegoActivity : AppCompatActivity() {

    lateinit var UID: String
    lateinit var NOMBRE: String
    var COTORRA: Long = 0
    lateinit var imgCotorra: ImageView
    lateinit var imgStar: ImageView
    lateinit var imgCanario: ImageView
    lateinit var contadorKills: TextView
    lateinit var nombreJugador: TextView
    lateinit var tiempo: TextView
    var velocidadCotorra: Long = 500

    var AnchoPantalla: Int =0
    var AltoPantalla: Int =0

    lateinit var random: Random

    var GameOver = false
    lateinit var miDialog: Dialog

    lateinit var firebaseAuth: FirebaseAuth
    //lateinit var user: FirebaseUser
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var players: DatabaseReference

    //Temporizador
    private var countDownTimer: CountDownTimer? = null
    private var tiempoRestante: Long = 20000 // ajusta el valor inicial según tu necesidad



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        imgCotorra = findViewById(R.id.imgCotorra)
        contadorKills = findViewById(R.id.contadorKills)
        nombreJugador = findViewById(R.id.nombreTextView)
        tiempo = findViewById(R.id.TiempoTextView)

        miDialog = Dialog(this)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        players = firebaseDatabase.getReference("players")

        val intent: Bundle? = intent.extras
        UID = intent?.getString("UID").toString()
        NOMBRE = intent?.getString("NAME").toString()
        COTORRA = 0

        contadorKills.text = COTORRA.toString()
        nombreJugador.text = NOMBRE

        Pantalla()
        CuentaAtras()


        imgCotorra.setOnClickListener(){

            var esCotorra: Boolean = (imgCotorra.drawable.constantState?.equals(ContextCompat.getDrawable(this, R.drawable.cotorra)?.constantState) == true)
            if (!GameOver) {

                if (esCotorra) {
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

        // Después de inicializar las vistas
        imgCanario = findViewById(R.id.imgCanario)
        imgStar = findViewById(R.id.imgStar)

        // Dentro de la función onCreate
        val handler = Handler()

        // ...

        val aparecerImagenes = object : Runnable {
            override fun run() {
                val randomNum = random.nextInt(2)

                // Generar coordenadas x e y aleatorias
                val randomX = random.nextInt(AnchoPantalla - imgCanario.width*2)
                val randomY = random.nextInt(AltoPantalla - imgCanario.height*2)

                if (randomNum == 0) {
                    imgCanario.visibility = View.VISIBLE
                    imgCanario.x = randomX.toFloat()
                    imgCanario.y = randomY.toFloat()

                    Handler().postDelayed({
                        imgCanario.visibility = View.INVISIBLE
                    }, 2000)

                    // Lógica de temporizador para imgCanario
                    imgCanario.setOnClickListener {
                        countDownTimer?.cancel()
                        tiempoRestante -= 3000
                        CuentaAtras()
                        imgCanario.visibility = View.INVISIBLE
                    }
                } else {
                    imgStar.visibility = View.VISIBLE
                    imgStar.x = randomX.toFloat()
                    imgStar.y = randomY.toFloat()

                    Handler().postDelayed({
                        imgStar.visibility = View.INVISIBLE
                    }, 2000)
                    // Lógica de temporizador para imgStar
                    imgStar.setOnClickListener {
                        countDownTimer?.cancel()
                        tiempoRestante += 3000
                        CuentaAtras()
                        imgStar.visibility = View.INVISIBLE
                    }
                }

                // Volver a programar la aparición de imágenes
                handler.postDelayed(this, (random.nextInt(5000) + 2000).toLong())
            }
        }

        // Iniciar el proceso de aparición de imágenes
        handler.postDelayed(aparecerImagenes, (random.nextInt(5000) + 1000).toLong())


    }
    private fun Pantalla(){ //Metodo para obtener el tamaño de la pantalla
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)

        AnchoPantalla = point.x
        AltoPantalla = point.y

        var ancho = AnchoPantalla.toString()
        var alto = AltoPantalla.toString()


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
        countDownTimer = object : CountDownTimer(tiempoRestante, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = millisUntilFinished
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
                tiempoRestante = 0
                tiempo.setText("0S")
                GameOver = true
                MensajeGameOver()
                guardarResultados(COTORRA)
            }
        }.start()
    }

    private fun pausarTemporizador() {
        countDownTimer?.cancel()
    }

    private fun reanudarTemporizador(tiempoRestante: Long) {
        countDownTimer?.cancel()
        CuentaAtras()
    }

    private fun MensajeGameOver() {

        var cotorrasMuertas: TextView
        var cotorrasM = COTORRA.toString()


        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.tiempoTerminado))
            .setMessage(resources.getString(R.string.hasMatado) + " " + cotorrasM + " cotorras")
            .setNeutralButton(resources.getString(R.string.juegarDeNuevo)) { dialog, which ->
                COTORRA = 0
                tiempoRestante=20000
                CuentaAtras()
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

    fun menu(view: View) {

        pausarTemporizador()

        val dialogView = layoutInflater.inflate(R.layout.menu_juego, null)
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .show()

        val reanudar = dialogView.findViewById<Button>(R.id.reanudarBtn)
        val jugarNuevo = dialogView.findViewById<Button>(R.id.juegarDeNuevoBtn)
        val menu = dialogView.findViewById<Button>(R.id.irMenuBtn)
        val ranking = dialogView.findViewById<Button>(R.id.irRankingBtn)

        reanudar.setOnClickListener {
            dialog.dismiss()
            reanudarTemporizador(tiempoRestante)
        }

        jugarNuevo.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, MenuActivity::class.java))

        }

        menu.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, MenuActivity::class.java))

        }

        ranking.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, RankingActivity::class.java))
        }
    }


}

