package com.example.appmpazpro3_grupo6.ui.screens

enum class DificultadMPAZ(val id: String, val titulo: String, val estrellas: String) {
    FACIL("facil", "Fácil", "⭐"),
    MEDIO("medio", "Medio", "⭐⭐"),
    DIFICIL("dificil", "Difícil", "⭐⭐⭐");

    companion object {
        fun desdeId(id: String): DificultadMPAZ? = entries.firstOrNull { it.id == id }
    }
}

data class PreguntaMPAZ(
    val articuloWikipedia: String,
    val pregunta: String,
    val opciones: List<String>,
    val correcta: String,
    val explicacion: String,
    val pista: String,
    val consejo: String
)

data class DatosAsignaturaMPAZ(val titulo: String, val emoji: String, val descripcion: String)

fun datosAsignaturaMPAZ(id: String): DatosAsignaturaMPAZ? = when (id) {
    "lenguaje" -> DatosAsignaturaMPAZ("Lenguaje y Comunicación", "📖", "Leer, escribir y comprender textos")
    "matematica" -> DatosAsignaturaMPAZ("Matemática", "🔢", "Calcular y resolver problemas")
    "ciencias" -> DatosAsignaturaMPAZ("Ciencias Naturales", "🧪", "Descubrir los seres vivos y la naturaleza")
    "historia" -> DatosAsignaturaMPAZ("Historia y Geografía", "🌎", "Conocer Chile, mapas y nuestro entorno")
    else -> null
}

private fun p(
    pregunta: String, a: String, b: String, c: String,
    correcta: String, articuloWikipedia: String,
    explicacion: String, pista: String, consejo: String
): PreguntaMPAZ = PreguntaMPAZ(
    articuloWikipedia, pregunta, listOf(a, b, c), correcta, explicacion, pista, consejo
)

fun preguntasMPAZ(asignaturaId: String, dificultad: DificultadMPAZ): List<PreguntaMPAZ> =
    when ("${asignaturaId}_${dificultad.id}") {
        "lenguaje_facil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Sustantivo",
                pregunta = "¿Cuál de estas palabras es un sustantivo?",
                opciones = listOf("Correr", "Mesa", "Rápido"),
                correcta = "Mesa",
                explicacion = "Mesa es un sustantivo porque nombra " +
                        "un objeto. Correr es un verbo y rápido " +
                        "puede ser un adjetivo.",
                pista = "Piensa cuál de las palabras nombra un objeto.",
                consejo = "Para reconocer un sustantivo, pregunta: " +
                        "¿es el nombre de una persona, animal, " +
                        "lugar o cosa?"
            ),
            p(
                "¿Cuál de estas palabras expresa una acción?",
                "Saltar", "Bonito", "Silla",
                "Saltar", "Verbo",
                "Saltar es un verbo porque expresa una acción.",
                "Piensa en algo que puedes hacer.",
                "Los verbos indican acciones o estados."
            ),
            p(
                "¿Cuál es el plural de flor?",
                "Flores", "Florito", "Florero",
                "Flores", "Plural",
                "Flores es el plural de flor.",
                "Piensa en cómo nombrar varias flores.",
                "Muchas palabras forman el plural agregando s o es."
            ),
            p(
                "¿Qué palabra significa lo contrario de alto?",
                "Grande", "Bajo", "Largo",
                "Bajo", "Antonimia",
                "Bajo es un antónimo de alto.",
                "Busca la palabra de significado contrario.",
                "Los antónimos expresan significados opuestos."
            ),
            p(
                "¿Qué artículo corresponde a la palabra casa?",
                "El", "La", "Los",
                "La", "Artículo_(gramática)",
                "Decimos la casa porque casa es un sustantivo femenino singular.",
                "Di la frase completa en voz alta.",
                "Los artículos acompañan a los sustantivos."
            )
        )
        "lenguaje_medio" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Adjetivo",
                pregunta = "¿Qué oración contiene un adjetivo?",
                opciones = listOf(
                    "El gato duerme.",
                    "El gato negro duerme.",
                    "El gato corre."
                ),
                correcta = "El gato negro duerme.",
                explicacion = "La palabra negro es un adjetivo " +
                        "porque describe una característica del gato.",
                pista = "Busca una palabra que describa cómo es el gato.",
                consejo = "Los adjetivos permiten describir " +
                        "colores, tamaños y otras características."
            ),
            p(
                "¿Qué palabra es sinónimo de contento?",
                "Alegre", "Enojado", "Asustado",
                "Alegre", "Sinonimia",
                "Alegre y contento tienen significados similares.",
                "Piensa en una persona feliz.",
                "Los sinónimos tienen significados iguales o parecidos."
            ),
            p(
                "¿Qué signo usamos al final de una pregunta?",
                "Punto", "Signo de interrogación", "Coma",
                "Signo de interrogación", "Signo_de_interrogación",
                "En español las preguntas usan signos de apertura y cierre: ¿ ?.",
                "Mira el final de la pregunta.",
                "Los signos ayudan a comprender lo que leemos."
            ),
            p(
                "¿Cuál es el verbo en «La niña dibuja un árbol»?",
                "Niña", "Dibuja", "Árbol",
                "Dibuja", "Verbo",
                "Dibuja es la acción que realiza la niña.",
                "Pregunta qué hace la niña.",
                "Identifica primero la acción de la oración."
            ),
            p(
                "¿Qué oración está escrita correctamente?",
                "Mañana iremos al parque.", "mañana iremos al parque", "Mañana iremos al parque",
                "Mañana iremos al parque.", "Oración_(gramática)",
                "La oración comienza con mayúscula y termina en punto.",
                "Revisa la primera letra y el final.",
                "Usa mayúscula inicial y el signo de cierre adecuado."
            )
        )
        "lenguaje_dificil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Inferencia",
                pregunta = "Lucía llevó un paraguas porque había " +
                        "nubes oscuras. ¿Qué podemos inferir?",
                opciones = listOf(
                    "Podría llover.",
                    "Hace mucho calor.",
                    "Es de noche."
                ),
                correcta = "Podría llover.",
                explicacion = "El paraguas y las nubes oscuras " +
                        "son pistas que indican que podría llover.",
                pista = "Relaciona el paraguas con el estado del cielo.",
                consejo = "Inferir es descubrir una idea usando " +
                        "las pistas del texto, aunque no " +
                        "se diga directamente."
            ),
            p(
                "Pedro vio charcos y salió con botas. ¿Qué ocurrió antes?",
                "Llovió", "Nevó", "Hubo sequía",
                "Llovió", "Inferencia",
                "Los charcos y las botas sugieren que llovió.",
                "Utiliza las pistas del relato.",
                "Inferir significa obtener conclusiones a partir de pistas."
            ),
            p(
                "¿Por qué Ana llevó agua al paseo bajo el sol?",
                "Para hidratarse", "Para escribir", "Para dormir",
                "Para hidratarse", "Hidratación",
                "Llevar agua ayuda a mantener la hidratación durante un paseo.",
                "Piensa en lo que necesita el cuerpo cuando hace calor.",
                "Identifica causas y consecuencias en un texto."
            ),
            p(
                "Ordena los hechos: sembrar, regar, crecer. ¿Qué va primero?",
                "Crecer", "Regar", "Sembrar",
                "Sembrar", "Secuencia",
                "Primero se siembra una semilla, luego se riega y después puede crecer.",
                "Busca el paso que debe ocurrir antes de los demás.",
                "Ordenar acontecimientos ayuda a comprender una historia."
            ),
            p(
                "En «El perro pequeño corre rápido», ¿qué palabra describe al perro?",
                "Pequeño", "Corre", "Rápido",
                "Pequeño", "Adjetivo",
                "Pequeño es un adjetivo que describe al sustantivo perro.",
                "Busca cómo es el perro, no qué hace.",
                "Los adjetivos aportan información sobre los sustantivos."
            )
        )
        "matematica_facil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Adición_(matemática)",
                pregunta = "¿Cuánto es 7 + 5?",
                opciones = listOf("10", "12", "14"),
                correcta = "12",
                explicacion = "Partimos de 7 y sumamos 5: " +
                        "8, 9, 10, 11 y 12. Por eso 7 + 5 = 12.",
                pista = "Empieza en 7 y cuenta cinco números más.",
                consejo = "Puedes usar tus dedos o dibujar " +
                        "elementos para practicar las sumas."
            ),
            p(
                "¿Cuánto es 9 - 4?",
                "3", "5", "6",
                "5", "Resta",
                "Si quitamos 4 de 9 quedan 5.",
                "Cuenta hacia atrás cuatro veces desde 9.",
                "Restar es quitar o encontrar una diferencia."
            ),
            p(
                "¿Cuánto es 6 + 8?",
                "12", "14", "16",
                "14", "Adición_(matemática)",
                "6 + 8 = 14.",
                "Completa primero diez y suma lo que falta.",
                "Agrupar en decenas ayuda a sumar."
            ),
            p(
                "¿Cuánto es 30 + 20?",
                "40", "50", "60",
                "50", "Adición_(matemática)",
                "Tres decenas más dos decenas son cinco decenas.",
                "Suma las decenas.",
                "Puedes sumar decenas completas con facilidad."
            ),
            p(
                "¿Cuánto es 15 - 7?",
                "6", "8", "9",
                "8", "Resta",
                "15 - 7 = 8.",
                "Cuenta desde 7 hasta llegar a 15.",
                "La resta se puede comprobar mediante una suma."
            )
        )
        "matematica_medio" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Notación_posicional",
                pregunta = "¿Cuánto es 23 + 18?",
                opciones = listOf("31", "41", "51"),
                correcta = "41",
                explicacion = "Sumamos las unidades: 3 + 8 = 11. " +
                        "Escribimos 1 y llevamos 1 decena. " +
                        "Después sumamos 2 + 1 + 1 = 4 decenas. " +
                        "El resultado es 41.",
                pista = "Suma las unidades 3 + 8. " +
                        "Recuerda llevar una decena.",
                consejo = "Ordena los números en columnas: " +
                        "unidades con unidades y decenas con decenas."
            ),
            p(
                "Hay 4 cajas con 3 lápices cada una. ¿Cuántos lápices hay?",
                "7", "12", "14",
                "12", "Multiplicación",
                "4 × 3 = 12, porque son cuatro grupos de tres.",
                "Suma 3 cuatro veces.",
                "Multiplicar permite sumar grupos iguales."
            ),
            p(
                "¿Cuánto es 5 × 6?",
                "25", "30", "35",
                "30", "Multiplicación",
                "Cinco grupos de seis suman treinta.",
                "Cuenta de seis en seis cinco veces.",
                "Las tablas ayudan a multiplicar con rapidez."
            ),
            p(
                "20 caramelos entre 4 niños: ¿cuántos recibe cada uno?",
                "4", "5", "6",
                "5", "División_(matemática)",
                "20 ÷ 4 = 5, por eso cada niño recibe 5.",
                "Busca un número que multiplicado por 4 dé 20.",
                "Dividir permite repartir en partes iguales."
            ),
            p(
                "¿Cuál es el perímetro de un cuadrado de lado 4 cm?",
                "8 cm", "12 cm", "16 cm",
                "16 cm", "Perímetro",
                "El perímetro es 4 + 4 + 4 + 4 = 16 cm.",
                "Suma sus cuatro lados iguales.",
                "El perímetro es la longitud total del contorno."
            )
        )
        "matematica_dificil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Resta",
                pregunta = "Una biblioteca tiene 46 libros, " +
                        "presta 19 y recibe 28 nuevos. " +
                        "¿Cuántos libros tiene al final?",
                opciones = listOf("55", "65", "37"),
                correcta = "55",
                explicacion = "Primero restamos los prestados: " +
                        "46 - 19 = 27. Después sumamos los nuevos: " +
                        "27 + 28 = 55 libros.",
                pista = "Primero resta los libros prestados. " +
                        "Después suma los libros recibidos.",
                consejo = "En problemas de varias operaciones, " +
                        "separa los pasos. Identifica qué cantidad " +
                        "disminuye y cuál aumenta."
            ),
            p(
                "Hay 6 bolsas con 7 canicas cada una. ¿Cuántas canicas hay?",
                "36", "42", "48",
                "42", "Multiplicación",
                "6 × 7 = 42 canicas.",
                "Usa la tabla del siete.",
                "Descompón una multiplicación difícil en otras más sencillas."
            ),
            p(
                "Si 36 galletas se reparten entre 6 niños, ¿cuántas recibe cada uno?",
                "5", "6", "7",
                "6", "División_(matemática)",
                "36 ÷ 6 = 6 galletas por niño.",
                "Piensa qué número por seis da treinta y seis.",
                "La división es la operación inversa de la multiplicación."
            ),
            p(
                "Un bus lleva 24 pasajeros, bajan 9 y suben 13. ¿Cuántos quedan?",
                "26", "28", "30",
                "28", "Aritmética",
                "24 - 9 + 13 = 28 pasajeros.",
                "Primero resta, luego suma.",
                "Resuelve problemas de varias operaciones paso a paso."
            ),
            p(
                "Un rectángulo mide 8 cm de largo y 3 cm de ancho. ¿Cuál es su perímetro?",
                "11 cm", "22 cm", "24 cm",
                "22 cm", "Perímetro",
                "8 + 3 + 8 + 3 = 22 cm.",
                "El rectángulo tiene dos lados largos y dos cortos.",
                "Suma la longitud de todos los lados para encontrar el perímetro."
            )
        )
        "ciencias_facil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Fotosíntesis",
                pregunta = "¿Qué necesitan las plantas para crecer?",
                opciones = listOf("Luz y agua", "Solo piedras", "Solo plástico"),
                correcta = "Luz y agua",
                explicacion = "Las plantas necesitan luz y agua " +
                        "para crecer. También requieren aire, " +
                        "nutrientes y condiciones adecuadas.",
                pista = "Piensa qué necesita una planta " +
                        "cuando la riegas y la dejas cerca del sol.",
                consejo = "Puedes observar una planta en casa " +
                        "y registrar cómo cambia con los días."
            ),
            p(
                "¿Qué sentido usamos para escuchar una campana?",
                "Vista", "Oído", "Gusto",
                "Oído", "Audición",
                "El oído permite percibir los sonidos.",
                "Piensa en tus orejas.",
                "Los sentidos nos ayudan a conocer el entorno."
            ),
            p(
                "¿Qué caracteriza a las aves?",
                "Plumas", "Seis patas", "Branquias",
                "Plumas", "Aves",
                "Todas las aves tienen plumas, aunque algunas no vuelan.",
                "Piensa en lo que cubre a una paloma.",
                "Observa características para clasificar animales."
            ),
            p(
                "¿En qué estado se encuentra el hielo?",
                "Gaseoso", "Líquido", "Sólido",
                "Sólido", "Hielo",
                "El hielo es agua en estado sólido.",
                "Es duro y mantiene su forma.",
                "El agua puede encontrarse en tres estados principales."
            ),
            p(
                "¿Qué estrella nos da luz durante el día?",
                "La Luna", "El Sol", "Marte",
                "El Sol", "Sol",
                "El Sol es la estrella más cercana a la Tierra.",
                "Piensa en qué ilumina el cielo de día.",
                "La luz solar es importante para la vida en la Tierra."
            )
        )
        "ciencias_medio" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Raíz_(botánica)",
                pregunta = "¿Qué parte de la planta absorbe " +
                        "agua del suelo?",
                opciones = listOf("Las flores", "La raíz", "Los frutos"),
                correcta = "La raíz",
                explicacion = "La raíz absorbe agua y minerales " +
                        "del suelo que la planta necesita.",
                pista = "Busca la parte que normalmente " +
                        "está debajo de la tierra.",
                consejo = "Dibuja una planta y escribe " +
                        "el nombre de sus partes principales."
            ),
            p(
                "¿En qué lugar vive normalmente un pez de río?",
                "Agua dulce", "Desierto", "Cielo",
                "Agua dulce", "Agua_dulce",
                "Los peces de río viven en ambientes de agua dulce.",
                "Piensa dónde nada un pez de río.",
                "Los hábitats entregan condiciones para vivir."
            ),
            p(
                "¿Qué parte de una planta suele formar semillas?",
                "Flor", "Raíz", "Tallo",
                "Flor", "Flor",
                "En muchas plantas, las flores participan en la reproducción.",
                "Piensa en lo que puede transformarse en fruto.",
                "Las distintas partes de la planta cumplen funciones."
            ),
            p(
                "¿Qué animal se alimenta principalmente de plantas?",
                "León", "Conejo", "Águila",
                "Conejo", "Herbívoro",
                "Los conejos comen principalmente materia vegetal.",
                "Busca el animal que come hierbas.",
                "Los animales pueden clasificarse por su alimentación."
            ),
            p(
                "Cuando el agua líquida pasa a hielo, ¿cómo se llama?",
                "Fusión", "Congelación", "Evaporación",
                "Congelación", "Solidificación",
                "La congelación cambia el agua líquida a estado sólido.",
                "Piensa qué ocurre en el congelador.",
                "La temperatura puede producir cambios de estado."
            )
        )
        "ciencias_dificil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Evaporación",
                pregunta = "Cuando el agua se calienta y se " +
                        "transforma en vapor, ¿cómo se llama " +
                        "ese proceso?",
                opciones = listOf("Congelación", "Evaporación", "Condensación"),
                correcta = "Evaporación",
                explicacion = "La evaporación transforma " +
                        "el agua líquida en vapor. La congelación " +
                        "forma hielo y la condensación transforma " +
                        "vapor en gotas de agua.",
                pista = "Piensa qué ocurre con un charco " +
                        "cuando recibe calor durante el día.",
                consejo = "Recuerda los cambios de estado: " +
                        "sólido, líquido y gaseoso."
            ),
            p(
                "Cuando el vapor se enfría y forma gotas, ¿qué ocurre?",
                "Evaporación", "Condensación", "Fusión",
                "Condensación", "Condensación",
                "El vapor se transforma en pequeñas gotas de agua.",
                "Piensa en las gotas sobre un vaso frío.",
                "La condensación forma parte del ciclo del agua."
            ),
            p(
                "En una cadena alimentaria, ¿quién produce su alimento usando luz?",
                "Planta", "Zorro", "Águila",
                "Planta", "Cadena_trófica",
                "Las plantas son productores porque realizan fotosíntesis.",
                "Busca el organismo que usa energía solar.",
                "Identifica productores y consumidores en las cadenas alimentarias."
            ),
            p(
                "Dos plantas reciben igual agua, pero distinta luz. ¿Qué están comparando?",
                "La luz", "El agua", "La maceta",
                "La luz", "Método_científico",
                "La variable que cambia es la cantidad de luz.",
                "Busca qué condición es distinta.",
                "En un experimento cambia una condición a la vez."
            ),
            p(
                "¿En qué orden ocurre parte del ciclo del agua?",
                "Evaporación, condensación, lluvia", "Lluvia, fusión, evaporación", "Congelación, suelo, fuego",
                "Evaporación, condensación, lluvia", "Ciclo_hidrológico",
                "El agua se evapora, el vapor se condensa y luego puede precipitar.",
                "Recuerda vapor, nubes y lluvia.",
                "Los cambios de estado permiten que el agua circule."
            )
        )
        "historia_facil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Mapa",
                pregunta = "¿Qué representa un mapa?",
                opciones = listOf("Lugares y territorios", "Recetas", "Canciones"),
                correcta = "Lugares y territorios",
                explicacion = "Un mapa representa lugares " +
                        "y territorios mediante símbolos, " +
                        "colores y ubicaciones.",
                pista = "Piensa qué utilizamos para encontrar lugares.",
                consejo = "Revisa el título, los símbolos y " +
                        "la orientación cuando observes un mapa."
            ),
            p(
                "¿En qué continente se encuentra Chile?",
                "Europa", "América", "África",
                "América", "América_del_Sur",
                "Chile se ubica en América del Sur.",
                "Busca el continente donde está Sudamérica.",
                "Usa un mapa para ubicar países y continentes."
            ),
            p(
                "¿Qué océano está frente a la costa de Chile?",
                "Atlántico", "Pacífico", "Índico",
                "Pacífico", "Océano_Pacífico",
                "Chile limita al oeste con el océano Pacífico.",
                "Piensa en el océano al oeste de Chile.",
                "Aprende a ubicar mares y océanos en un mapa."
            ),
            p(
                "¿Qué instrumento ayuda a reconocer el norte?",
                "Brújula", "Termómetro", "Balanza",
                "Brújula", "Brújula",
                "La brújula sirve para orientarse mediante los puntos cardinales.",
                "Piensa en una aguja que indica direcciones.",
                "Los puntos cardinales ayudan a orientarnos."
            ),
            p(
                "¿Qué color suele representar el agua en un mapa físico escolar?",
                "Azul", "Rojo", "Amarillo",
                "Azul", "Mapa",
                "Muchos mapas escolares usan azul para mares y ríos.",
                "Piensa en el color de los océanos del mapa.",
                "Lee siempre la leyenda de colores del mapa."
            )
        )
        "historia_medio" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Este",
                pregunta = "Si el norte está arriba en un mapa, " +
                        "¿dónde está el este?",
                opciones = listOf("A la izquierda", "A la derecha", "Abajo"),
                correcta = "A la derecha",
                explicacion = "Cuando el norte está arriba, " +
                        "el sur está abajo, el este a la derecha " +
                        "y el oeste a la izquierda.",
                pista = "Imagina una rosa de los vientos " +
                        "con el norte apuntando hacia arriba.",
                consejo = "Practica con una rosa de los vientos: " +
                        "Norte, Sur, Este y Oeste."
            ),
            p(
                "¿Cuál es la capital de Chile?",
                "Valparaíso", "Santiago", "Concepción",
                "Santiago", "Santiago_de_Chile",
                "Santiago es la capital de Chile.",
                "Piensa en la ciudad donde está la sede del gobierno nacional.",
                "Las capitales son ciudades con funciones administrativas importantes."
            ),
            p(
                "¿Qué caracteriza a un paisaje rural?",
                "Muchos cultivos y campos", "Muchos rascacielos", "Solo aeropuertos",
                "Muchos cultivos y campos", "Medio_rural",
                "En las zonas rurales suelen existir campos y actividades agrícolas.",
                "Piensa en el campo, no en el centro de una gran ciudad.",
                "Compara paisajes rurales y urbanos."
            ),
            p(
                "¿Cuál es un ejemplo de patrimonio cultural?",
                "Una fiesta tradicional", "Un teléfono nuevo", "Una nube",
                "Una fiesta tradicional", "Patrimonio_cultural",
                "Las celebraciones tradicionales forman parte de la cultura de una comunidad.",
                "Piensa en costumbres que se transmiten entre generaciones.",
                "Cuidar las tradiciones ayuda a valorar nuestra identidad."
            ),
            p(
                "¿Qué usamos para representar el recorrido entre dos lugares?",
                "Mapa", "Receta", "Calendario",
                "Mapa", "Mapa",
                "Un mapa muestra ubicaciones y ayuda a planear recorridos.",
                "Piensa en calles y caminos dibujados.",
                "Busca el título y los símbolos de los mapas."
            )
        )
        "historia_dificil" -> listOf(
            PreguntaMPAZ(
                articuloWikipedia = "Rosa_de_los_vientos",
                pregunta = "Caminas dos cuadras al norte " +
                        "y una al este. ¿En qué dirección general " +
                        "quedas respecto del inicio?",
                opciones = listOf("Suroeste", "Noreste", "Noroeste"),
                correcta = "Noreste",
                explicacion = "Como avanzaste hacia el norte " +
                        "y hacia el este, quedas al noreste " +
                        "de tu punto de partida.",
                pista = "Combina las dos direcciones " +
                        "en las que caminaste.",
                consejo = "Dibuja una cruz con los puntos cardinales " +
                        "y marca cada movimiento con una flecha."
            ),
            p(
                "Desde la plaza caminas al este y luego al sur. ¿Hacia dónde quedas?",
                "Noreste", "Sureste", "Noroeste",
                "Sureste", "Rosa_de_los_vientos",
                "Si avanzas hacia el este y el sur, quedas al sureste.",
                "Combina las dos direcciones.",
                "Puedes dibujar una rosa de los vientos para orientarte."
            ),
            p(
                "¿Cuál es una fuente histórica que cuenta algo de su época?",
                "Una carta antigua", "Un videojuego actual", "Una nube",
                "Una carta antigua", "Fuente_primaria",
                "Una carta escrita en el pasado puede ser una fuente histórica primaria.",
                "Busca un testimonio del tiempo que se estudia.",
                "Las fuentes nos ayudan a comprender cómo vivían las personas."
            ),
            p(
                "¿Qué acción ayuda a conservar el patrimonio cultural?",
                "Cuidar monumentos", "Rayar edificios históricos", "Destruir objetos antiguos",
                "Cuidar monumentos", "Patrimonio_cultural",
                "Proteger los monumentos permite conservar la historia compartida.",
                "Piensa en una acción respetuosa.",
                "El patrimonio es responsabilidad de toda la comunidad."
            ),
            p(
                "Si un mapa muestra norte arriba, ¿qué dirección queda abajo y a la izquierda?",
                "Sureste", "Suroeste", "Noreste",
                "Suroeste", "Puntos_cardinales",
                "Abajo corresponde al sur e izquierda al oeste: suroeste.",
                "Combina sur y oeste.",
                "Practica orientarte con los cuatro puntos cardinales."
            )
        )
        else -> emptyList()
    }