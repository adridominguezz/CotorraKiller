package com.example.cotorraskiller

import android.app.Dialog
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Display
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.lang.Integer.max
import java.lang.Math.min
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

        val intent: Bundle? = intent.extras
        UID = intent?.getString("UID").toString()
        NOMBRE = intent?.getString("NAME").toString()
        var contador = intent?.getString("COTORRAS").toString()
        COTORRA = contador.toLong()

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
        object : CountDownTimer(30000, 1000) {

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
            }
        }.start()
    }

    private fun MensajeGameOver() {
        TODO("Not yet implemented")
    }


}