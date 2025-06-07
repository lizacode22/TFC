package com.example.eurogymclass.ChatBot

import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ChatBotApi {
    private val API_KEY: String by lazy {
        val properties = java.util.Properties()
        val inputStream = java.io.File("secrets.properties").inputStream()
        properties.load(inputStream)
        properties.getProperty("OPENAI_API_KEY")
    }
    private const val ENDPOINT = "https://api.openai.com/v1/chat/completions"

    private val client = OkHttpClient()

    fun enviarMensaje(mensaje: String, callback: (String) -> Unit) {
        val safeMensaje = mensaje.replace("\"", "\\\"")
        val mensajeLower = mensaje.lowercase()

        when {
            "horario" in mensajeLower || "días" in mensajeLower -> {
                callback("El gimnasio abre de lunes a viernes de 7:00 a 23:00. El fin de semana abre sus puertas de 8:00 a 21:00.")
                return
            }

            "ubicación" in mensajeLower || "dónde está" in mensajeLower -> {
                callback("Nos encontramos en Calle Pintores, 6. Alcorcón (28921), Madrid.")
                return
            }

            else -> {

            }
        }

        val json = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            {"role": "system", "content": "Esta app llamada EuroGymClass permite a los usuarios registrados de un gimnasio en Alcorcón reservar clases colectivas, ver el historial de reservas, editar su perfil y recibir notificaciones del centro. Las clases pueden ser de funcional, GAP, ciclo, pilates o CrossGym. La app no permite chatear con otros usuarios ni consultar rutinas de entrenamiento. Solo puedes hacer gestiones relacionadas con reservas e información del centro."},
            {"role": "user", "content": "$safeMensaje"}
          ]
        }
    """.trimIndent()

        val mediaType = "application/json".toMediaType()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(ENDPOINT)
            .addHeader("Authorization", "Bearer $API_KEY")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error de red: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val errorBodyString = response.body?.string() ?: "Sin detalles"
                        callback("Error ${response.code}: ${response.message}")
                        return
                    }

                    val bodyString = response.body?.string()
                    val jsonResp = JsonParser.parseString(bodyString).asJsonObject
                    val contenido = jsonResp["choices"]
                        .asJsonArray[0]
                        .asJsonObject["message"]
                        .asJsonObject["content"]
                        .asString
                    callback(contenido.trim())
                }
            }
        })
    }
}