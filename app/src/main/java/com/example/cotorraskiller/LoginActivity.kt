package com.example.cotorraskiller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        btnLogin = findViewById(R.id.entrarBtn)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)



        btnLogin.setOnClickListener {
            val logEmail = editTextEmail.text.toString()
            val logPassword = editTextPassword.text.toString()

            if (checkEmpty(logEmail, logPassword)){
                if(!Patterns.EMAIL_ADDRESS.matcher(logEmail).matches()){
                    editTextEmail.setError("Correo válido")
                    editTextEmail.setFocusable(true)

                } else if (logPassword.length<6){ //Este comprueba que la contraseña tenga al menos 6 caracteres
                    editTextPassword.setError("Contraseña debe tener al menos 6 carácteres")
                    editTextPassword.setFocusable(true)

                } else{ //Si pasa las anteriores comprobaciones, pasa a registrar el jugador
                    auth.signInWithEmailAndPassword(logEmail,logPassword)
                        .addOnCompleteListener(this){task ->
                            if (task.isSuccessful){
                                startActivity((Intent(this,JuegoActivity::class.java)))
                                finish()
                            }else{
                                Toast.makeText(applicationContext, "ERROR INICIAR SESION", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
    private fun checkEmpty(logEmail: String, logPassword: String): Boolean {
        return logEmail.isNotEmpty() && logPassword.isNotEmpty()
    }
    fun volver(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}