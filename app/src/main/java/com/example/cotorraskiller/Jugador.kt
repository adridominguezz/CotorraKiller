package com.example.cotorraskiller

class Jugador {
    val uid: String
    val nombre: String
    val email: String
    var cotorras: Long = 0

    constructor(uid: String, nombre: String, email: String, cotorras: Long) {
        this.uid = uid
        this.nombre = nombre
        this.email = email
        this.cotorras = cotorras
    }
}


