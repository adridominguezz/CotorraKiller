package com.example.cotorraskiller

class Jugador {
    val nombre: String
    val email: String
    var cotorras: Long = 0

    constructor(nombre: String, email: String, cotorras: Long) {
        this.nombre = nombre
        this.email = email
        this.cotorras = cotorras
    }
}



