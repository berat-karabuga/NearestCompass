package com.stargazer.nearestcompass.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson

object ApiClient {

    val client: HttpClient by lazy {
        HttpClient(Android){
            install(ContentNegotiation){
                gson {
                    setPrettyPrinting()
                    setLenient()
                    serializeNulls()
                }
            }

            install(Logging){
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }

            engine {
                connectTimeout = 30_000
                socketTimeout = 30_000
            }
        }
    }

    fun close(){
        client.close()
    }
}

