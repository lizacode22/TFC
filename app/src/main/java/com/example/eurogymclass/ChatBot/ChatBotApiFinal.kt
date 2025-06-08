package com.example.eurogymclass.ChatBot

import com.example.eurogymclass.BuildConfig
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object ChatBotApiFinal {
    private val API_KEY = BuildConfig.CHATBOT_API_KEY
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

        val body = json.toRequestBody("application/json".toMediaType())

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