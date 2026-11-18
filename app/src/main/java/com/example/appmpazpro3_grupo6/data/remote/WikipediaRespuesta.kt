package com.example.appmpazpro3_grupo6.data.remote

import com.google.gson.annotations.SerializedName

data class WikipediaRespuesta(
    @SerializedName("pages")
    val pages: List<WikipediaPagina>?
)

data class WikipediaPagina(
    @SerializedName("key")
    val key: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?
)