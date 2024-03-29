package com.example.cotorraskiller

import android.content.ContentValues
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import org.checkerframework.checker.nullness.qual.NonNull
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var editTextIcon: EditText
    private lateinit var editTextImg: EditText
    private lateinit var btnRegistro: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var imagenCircleView: CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = Firebase.auth
        btnRegistro = findViewById(R.id.registerBtn)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        editTextName = findViewById(R.id.editTextName)
        editTextIcon = findViewById(R.id.editTextIcon)
        editTextImg = findViewById(R.id.editTextImg)



        //Escucha el boton de registro
        btnRegistro.setOnClickListener {

            //Convierte en Strings los textos dentro de los campos editText
            val name: String = editTextName.text.toString()
            val logEmail: String = editTextEmail.text.toString()
            val logPassword: String = editTextPassword.text.toString()
            val logConfirmPassword: String = editTextConfirmPassword.text.toString()
            val icon: String = editTextIcon.text.toString()
            val img: String = editTextImg.text.toString()

            //Comprueba que los campos de email y contraseña no están vacios y que los campos de contraseña y confirmar contraseña sean iguales
            if ((checkEmpty(logEmail, logPassword)) && (logPassword == logConfirmPassword)){
                //Comprueba que el texto que hay dentro del email lleva formato correcto de email, sino marca un error
                if(!Patterns.EMAIL_ADDRESS.matcher(logEmail).matches()){
                    editTextEmail.setError("Correo válido")
                    editTextEmail.setFocusable(true)

                } else if (logPassword.length<6){ //Este comprueba que la contraseña tenga al menos 6 caracteres
                    editTextPassword.setError("Contraseña debe tener al menos 6 carácteres")
                    editTextPassword.setFocusable(true)

                } else{ //Si pasa las anteriores comprobaciones, pasa a registrar el jugador
                    RegistrarJugadorFirebase(logEmail, logPassword, name, icon, img)
                }

            }
        }


    }




    private fun RegistrarJugadorFirebase(logEmail: String, logPassword: String, name: String, icon: String, img: String) {
        auth.createUserWithEmailAndPassword(logEmail, logPassword)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //Toast.makeText(this, "USUARIO CREADO", Toast.LENGTH_SHORT).show()

                    val user = auth.currentUser

                    val contador = 0

                    assert(user != null)

                    val uid = user?.uid
                    val uidString = uid.toString()
                    val database = FirebaseFirestore.getInstance()

                    database.collection("players").document(uidString).set(
                        hashMapOf(
                            "uid" to uidString,
                            "email" to logEmail,
                            "password" to logPassword,
                            "name" to name,
                            "cotorras" to contador,
                            "imagen" to img,
                            "icon" to icon
                            )
                    )

                    Toast.makeText(this, "USUARIO REGISTRADO", Toast.LENGTH_SHORT).show()
                    startActivity((Intent(this,LoginActivity::class.java)))
                    finish()
                } else {
                    Log.e("Error", "Error al crear usuario: ${task.exception}")
                    Toast.makeText(this, "ERROR AL REGISTRAR USUARIO", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }

    }


    private fun checkEmpty(logEmail: String, logPassword: String): Boolean {
        return logEmail.isNotEmpty() && logPassword.isNotEmpty()
    }

    fun volver(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

}