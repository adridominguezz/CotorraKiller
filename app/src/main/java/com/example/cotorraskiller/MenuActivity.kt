package com.example.cotorraskiller

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cotorraskiller.R.id.editImgBtn
import com.example.cotorraskiller.R.id.imgUserMenu
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MenuActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var players: DatabaseReference
    var db = FirebaseFirestore.getInstance()

    lateinit var btnCerrarSesion: Button
    lateinit var btnJugar: Button
    lateinit var btnRanking: Button
    lateinit var txtCotorras: TextView
    lateinit var txtName: TextView
    lateinit var txtEmail: TextView
    lateinit var editImgBtn: FloatingActionButton
    lateinit var imagenCircleView: CircleImageView

    lateinit var referenciaAlmacenamiento: StorageReference
    var rutaAlmacenamiento: String = "fotosPerfil/*"

    //    Permisos
    private val CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO = 200
    private val CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN = 300

    // Matrices
    private var PermisosDeAlmacenamiento: Array<String>? = null
    private lateinit var imagen_uri: Uri
    private lateinit var perfil: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser




        firebaseDatabase = FirebaseDatabase.getInstance()
        players = firebaseDatabase.getReference("players")

        referenciaAlmacenamiento = FirebaseStorage.getInstance().getReference()
        PermisosDeAlmacenamiento = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)


        btnCerrarSesion = findViewById(R.id.CerrarSesionBtn)
        btnJugar = findViewById(R.id.JugarBtn)
        btnRanking = findViewById(R.id.rankingBtn)
        txtCotorras = findViewById(R.id.cotorrasTxt)
        txtEmail = findViewById(R.id.userEmailTxt)
        txtName = findViewById(R.id.userNameTxt)
        editImgBtn = findViewById(R.id.editImgBtn)
        imagenCircleView = findViewById(R.id.imgUserMenu)

        //BOTON CERRAR SESION
        btnCerrarSesion.setOnClickListener { cerrarSesion() }
        //BOTON JUGAR
        btnJugar.setOnClickListener {


            var uidString: String = user?.uid.toString()
            var nameString: String = txtName.text.toString()
            var cotorras = txtCotorras.text.toString()

            startActivity(Intent(this, JuegoActivity::class.java).putExtra("UID", uidString).putExtra( "NAME", nameString).putExtra("COTORRAS", cotorras))

        }
        //BOTON RANKING
        btnRanking.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java))
        }

        //Escucha el boton de editar imagen
//        editImgBtn.setOnClickListener {
//
//            MaterialAlertDialogBuilder(this)
//                .setTitle(resources.getString(R.string.seleccionImg))
//                //Boton de galeria
//                .setNeutralButton(resources.getString(R.string.Galeria)) { dialog, which ->
//                    if (!ComprombarPermisoAlmacenamiento()){ //Si no se habilitó el permiso se solicita
//                        SolicitarPermisoAlmacenamiento()
//                    }else{
//                        ElegirImagenGaleria() //Si se habilitó el permiso se elige
//                    }
//                }
////                .setNegativeButton(resources.getString(R.string.irMenu)) { dialog, which ->
////
////                }
////                .setPositiveButton(resources.getString(R.string.irRanking)) { dialog, which ->
////
////                }
//                .show()
//        }

    }

    override fun onStart() {
        userLog()
        super.onStart()
    }

    //Metodo que comprueba si hay un usuario con la sesión iniciada
    private fun userLog(){
        if (user == null) {
            // Si el usuario no está autenticado, redirige a la actividad de inicio de sesión
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Termina la actividad actual para evitar volver atrás a esta pantalla sin autenticación
        } else {
            //consulta()
            datosUser()
            Toast.makeText(this, "Usuario en línea", Toast.LENGTH_SHORT).show()

        }
    }

    private fun cerrarSesion(){
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java ))
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    //METODO PARA OBTENER LOS DATOS DEL USUARIO
    private fun datosUser() {

        val uid = user?.uid
        val uidString = uid.toString()
        val docRef = db.collection("players").document(uidString)

        docRef.get().addOnSuccessListener { document->

                if (document != null && document.exists()) {

                    val cotorrasLong= document.getLong("cotorras")
                    val nameString = document.getString("name")
                    val emailString = document.getString("email")
                    val imagen = document.getString("imagen")

                    txtCotorras.text = cotorrasLong.toString()
                    txtEmail.text = emailString
                    txtName.text = nameString
                    try {
                        Picasso.get().load(imagen).into(imagenCircleView)
                    }catch (e: Exception){
                        Picasso.get().load(R.drawable.cazador)
                    }


                } else {
                    Log.d(TAG, "No such document")

                }
        }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "Error al obtener el documento: $exception")
                // Muestra un Toast indicando el fallo
                Toast.makeText(this@MenuActivity, "Error al obtener el documento", Toast.LENGTH_SHORT).show()
            }

    }

    //METODO PARA ABRIR LA GALERIA
    private fun ElegirImagenGaleria() {
        var intentGaleria = Intent(Intent.ACTION_PICK)
        intentGaleria.setType("image/*")
        startActivityForResult(intentGaleria,CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN)
    }

    private fun ComprombarPermisoAlmacenamiento(): Boolean {
        val resultado: Boolean = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return resultado
    }

    //SE LLAMA CUANDO EL USUARIO O JUGADOR PRESIONA PERMITIR O DENEGAR EL CUADRO DEL DIALOGO
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        when (requestCode) {
            CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido, ahora puedes hacer lo que sea que requiera este permiso
                    ElegirImagenGaleria()
                } else {
                    // Permiso denegado, puedes informar al usuario o manejar la denegación
                    Toast.makeText(
                        this,
                        "El permiso para acceder a la galería fue denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
            // Otros casos de solicitud de permisos si los tienes
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //SE LLAMA CUANDO EL USUARIO YA HA ELEGIDO LA IMAGEN DE LA GALERIA
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_OK){
            //DE LA IMAGEN VAMOS A OBTENER LA URI
            if (requestCode == CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN){
                val imagenUri: Uri? = data?.data
                SubirFoto(imagenUri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //METODO CAMBIAR FOTO PERFIL DEL USUARIO
    private fun SubirFoto(imagenUri: Uri?) {
        var RutaArchivoYNombre = rutaAlmacenamiento + "" + perfil + "" + user!!.uid
        var storageReference: StorageReference = referenciaAlmacenamiento.child(RutaArchivoYNombre)
        storageReference.putFile(imagen_uri).addOnSuccessListener { taskSnapshot ->
            val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
            uriTask.addOnSuccessListener { uri ->
                val downloadUri: Uri? = uri
                val resultado = HashMap<String, Any>()
                resultado[perfil] = downloadUri.toString()

                players.child(user!!.uid).updateChildren(resultado)
                    .addOnSuccessListener {
                        Toast.makeText(this@MenuActivity, "IMAGEN CAMBIADA", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@MenuActivity, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(this@MenuActivity, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this@MenuActivity, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show()
        }

    }

    //PERMISO DE ALMACENAMIENTO EN TIEMPO DE EJECUCIÓN
    private fun SolicitarPermisoAlmacenamiento() {
        requestPermissions(PermisosDeAlmacenamiento!!, CODIGO_DE_SOLICITUD_DE_ALMACENAMIENTO)
    }

}