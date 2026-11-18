package com.example.appmpazpro3_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.LecturaCiencias
import com.example.appmpazpro3_grupo6.repository.LecturasCienciasRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface EstadoLecturaCiencias {
    data object Cargando : EstadoLecturaCiencias
    data object SinResultados : EstadoLecturaCiencias
    data object Error : EstadoLecturaCiencias
    data class Disponible(val lectura: LecturaCiencias) : EstadoLecturaCiencias
}

class LecturasCienciasViewModel(
    private val repositorio: LecturasCienciasRepository = LecturasCienciasRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<EstadoLecturaCiencias>(
        EstadoLecturaCiencias.Cargando
    )
    val estado: StateFlow<EstadoLecturaCiencias> = _estado.asStateFlow()

    private var carga: Job? = null

    init {
        cargarFotosintesis()
    }

    fun cargarFotosintesis() {
        if (carga?.isActive == true) return

        _estado.value = EstadoLecturaCiencias.Cargando

        carga = viewModelScope.launch {
            _estado.value = try {
                val lectura = repositorio.obtenerFotosintesis()

                if (lectura == null) {
                    EstadoLecturaCiencias.SinResultados
                } else {
                    EstadoLecturaCiencias.Disponible(lectura)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                EstadoLecturaCiencias.Error
            }
        }
    }
}