package com.example.cotorraskiller

import java.io.Serializable

data class Jugador (
    val uid: String,
    val name: String,
    val email: String,
    var cotorras: Long,
    val iconUrl: String,
    val imgUrl: String
): Serializable




