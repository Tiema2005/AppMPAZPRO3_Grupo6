package com.example.appmpazpro3_grupo6.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EstudianteViewModel : ViewModel() {
    var nombreEstudiante by mutableStateOf("")
        private set

    var cursoEstudiante by mutableStateOf("3° Básico")
        private set

    fun registrarEstudiante(
        nombre: String,
        curso: String
    ) {
        nombreEstudiante = nombre
        cursoEstudiante = curso
    }
}
