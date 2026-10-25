package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.ProgresoEstudiante
import com.example.appmpazpro3_grupo6.repository.ProgresoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProgresoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = ProgresoRepository(application)

    val progreso: StateFlow<ProgresoEstudiante> =
        repositorio.progreso.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ProgresoEstudiante()
        )

    private val _errorGuardado = MutableStateFlow<String?>(null)

    val errorGuardado: StateFlow<String?> = _errorGuardado

    fun registrarEjercicio(
        asignaturaId: String,
        cantidadCompletada: Int
    ) {
        _errorGuardado.value = null

        viewModelScope.launch {
            try {
                repositorio.registrarEjercicioRespondido(
                    asignaturaId = asignaturaId,
                    ejerciciosRespondidos = cantidadCompletada
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorGuardado.value =
                    "No se pudo guardar el progreso del estudiante."
            }
        }
    }

    fun registrarRespuesta(
        asignaturaId: String,
        numeroEjercicio: Int,
        esCorrecta: Boolean
    ) {
        _errorGuardado.value = null

        viewModelScope.launch {
            try {
                repositorio.registrarRespuesta(
                    asignaturaId = asignaturaId,
                    numeroEjercicio = numeroEjercicio,
                    esCorrecta = esCorrecta
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorGuardado.value =
                    "No se pudo guardar la respuesta del ejercicio."
            }
        }
    }
}