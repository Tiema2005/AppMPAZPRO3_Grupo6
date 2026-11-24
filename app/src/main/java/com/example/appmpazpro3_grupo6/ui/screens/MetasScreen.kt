package com.example.appmpazpro3_grupo6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmpazpro3_grupo6.data.remote.MetaEstudioMPAZ
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.components.MPAZButton
import com.example.appmpazpro3_grupo6.ui.components.MPAZCard
import com.example.appmpazpro3_grupo6.ui.components.MPAZScreenHeader
import com.example.appmpazpro3_grupo6.ui.theme.*

@Composable
fun MetasScreen(
    metas: List<MetaEstudioMPAZ> = emptyList(),
    cargando: Boolean = false,
    procesando: Boolean = false,
    mensajeError: String? = null,
    onVolverClick: () -> Unit = {},
    onRecargar: () -> Unit = {},
    onGuardar: (
        Long?,
        String,
        String,
        Boolean,
        () -> Unit
    ) -> Unit = { _, _, _, _, terminado -> terminado() },
    onEliminar: (
        Long,
        () -> Unit
    ) -> Unit = { _, terminado -> terminado() }
) {
    var mostrarFormulario by rememberSaveable {
        mutableStateOf(false)
    }

    var metaEditandoId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    var completada by rememberSaveable {
        mutableStateOf(false)
    }

    var metaParaEliminar by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    fun abrirFormulario(meta: MetaEstudioMPAZ?) {
        metaEditandoId = meta?.id
        titulo = meta?.titulo.orEmpty()
        descripcion = meta?.descripcion.orEmpty()
        completada = meta?.completada ?: false
        mostrarFormulario = true
    }

    MPAZBackground {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val alturaPantalla = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = alturaPantalla)
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 16.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MPAZScreenHeader(
                        titulo = "Mis metas de estudio",
                        subtitulo = "¡Organiza tus objetivos!",
                        onVolverClick = onVolverClick
                    )

                    Text(
                        text = "🎯",
                        fontSize = 54.sp
                    )

                    Text(
                        text = "Crea metas y completa tus desafíos",
                        color = MPAZTextoSecundario,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )

                    MPAZButton(
                        texto = "＋ Crear nueva meta",
                        onClick = {
                            abrirFormulario(null)
                        },
                        enabled = !procesando && !cargando
                    )

                    if (cargando) {
                        CircularProgressIndicator(
                            color = MPAZCoral
                        )

                        Text(
                            text = "Cargando tus metas...",
                            color = MPAZTextoSecundario
                        )
                    }

                    if (mensajeError != null) {
                        MPAZCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = mensajeError,
                                modifier = Modifier.fillMaxWidth(),
                                color = MPAZCoral,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(8.dp))

                            TextButton(
                                onClick = onRecargar,
                                enabled = !cargando && !procesando
                            ) {
                                Text("Volver a cargar")
                            }
                        }
                    }

                    if (!cargando && metas.isEmpty()) {
                        MPAZCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "🦉📚",
                                    fontSize = 44.sp
                                )

                                Text(
                                    text = "Todavía no tienes metas",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MPAZTexto,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = "Comienza creando un objetivo para mejorar cada día.",
                                    color = MPAZTextoSecundario,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    if (!cargando && metas.isNotEmpty()) {
                        Text(
                            text = "Mis objetivos: ${metas.size}",
                            modifier = Modifier.fillMaxWidth(),
                            color = MPAZTexto,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        metas.forEach { meta ->
                            MPAZCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (meta.completada) {
                                                "✅"
                                            } else {
                                                "🎯"
                                            },
                                            fontSize = 25.sp
                                        )

                                        Spacer(Modifier.width(10.dp))

                                        Text(
                                            text = meta.titulo,
                                            modifier = Modifier.weight(1f),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MPAZTexto
                                        )
                                    }

                                    if (!meta.descripcion.isNullOrBlank()) {
                                        Text(
                                            text = meta.descripcion,
                                            color = MPAZTextoSecundario,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = meta.completada,
                                            enabled = !procesando && !cargando,
                                            onCheckedChange = { nuevoEstado ->
                                                onGuardar(
                                                    meta.id,
                                                    meta.titulo,
                                                    meta.descripcion.orEmpty(),
                                                    nuevoEstado
                                                ) {}
                                            }
                                        )

                                        Text(
                                            text = if (meta.completada) {
                                                "Meta completada"
                                            } else {
                                                "Marcar como completada"
                                            },
                                            color = if (meta.completada) {
                                                MPAZVerde
                                            } else {
                                                MPAZTexto
                                            },
                                            fontSize = 14.sp
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextButton(
                                            enabled = !procesando && !cargando,
                                            onClick = {
                                                abrirFormulario(meta)
                                            }
                                        ) {
                                            Text("✏️ Editar")
                                        }

                                        TextButton(
                                            enabled = !procesando && !cargando,
                                            onClick = {
                                                metaParaEliminar = meta.id
                                            }
                                        ) {
                                            Text(
                                                text = "Eliminar",
                                                color = MPAZCoral
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!cargando && metas.isNotEmpty()) {
                        TextButton(
                            onClick = onRecargar,
                            enabled = !procesando
                        ) {
                            Text("↻ Actualizar mis metas")
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }

    if (mostrarFormulario) {
        AlertDialog(
            onDismissRequest = {
                if (!procesando) {
                    mostrarFormulario = false
                }
            },
            title = {
                Text(
                    text = if (metaEditandoId == null) {
                        "Crear nueva meta"
                    } else {
                        "Editar meta"
                    },
                    color = MPAZTexto,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = {
                            if (it.length <= 120) {
                                titulo = it
                            }
                        },
                        label = {
                            Text("Título de la meta")
                        },
                        supportingText = {
                            Text("${titulo.length}/120")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            if (it.length <= 500) {
                                descripcion = it
                            }
                        },
                        label = {
                            Text("Descripción")
                        },
                        supportingText = {
                            Text("${descripcion.length}/500")
                        },
                        minLines = 2,
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (metaEditandoId != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = completada,
                                onCheckedChange = {
                                    completada = it
                                }
                            )

                            Text("Meta completada")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !procesando &&
                            titulo.isNotBlank() &&
                            titulo.length <= 120 &&
                            descripcion.length <= 500,
                    onClick = {
                        onGuardar(
                            metaEditandoId,
                            titulo.trim(),
                            descripcion.trim(),
                            completada
                        ) {
                            mostrarFormulario = false
                        }
                    }
                ) {
                    Text(
                        text = if (procesando) {
                            "Guardando..."
                        } else {
                            "Guardar"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !procesando,
                    onClick = {
                        mostrarFormulario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    val idEliminar = metaParaEliminar

    if (idEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                if (!procesando) {
                    metaParaEliminar = null
                }
            },
            title = {
                Text("Eliminar meta")
            },
            text = {
                Text(
                    "¿Estás seguro de que quieres eliminar esta meta?"
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !procesando,
                    onClick = {
                        onEliminar(idEliminar) {
                            metaParaEliminar = null
                        }
                    }
                ) {
                    Text(
                        text = if (procesando) {
                            "Eliminando..."
                        } else {
                            "Eliminar"
                        },
                        color = MPAZCoral
                    )
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !procesando,
                    onClick = {
                        metaParaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(
    name = "MPAZ - Mis metas",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun MetasScreenPreview() {
    AppMPAZPRO3_Grupo6Theme {
        MetasScreen(
            metas = listOf(
                MetaEstudioMPAZ(
                    id = 1,
                    titulo = "Practicar Matemática",
                    descripcion = "Resolver cinco ejercicios.",
                    completada = false
                ),
                MetaEstudioMPAZ(
                    id = 2,
                    titulo = "Leer un cuento",
                    descripcion = "Leer y aprender palabras nuevas.",
                    completada = true
                )
            )
        )
    }
}