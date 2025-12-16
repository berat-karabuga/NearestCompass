package com.stargazer.nearestcompass.data.repository

import android.util.Log
import com.stargazer.nearestcompass.data.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class OverpassRepository {

    private val baseUrl = "https://overpass-api.de/api/interpreter"


    suspend fun getNearbyStores(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int = 2000
    ): Result<List<Store>> {
        return try {
            Log.d("OverpassRepository", "Fetching stores around: $latitude, $longitude")

            val query = buildOverpassQuery(latitude, longitude, radiusMeters)

            Log.d("OverpassRepository", "Query: $query")

            val response: HttpResponse = ApiClient.client.post(baseUrl) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody("data=$query")
            }

            val overpassResponse = response.body<OverpassModels>()

            Log.d("OverpassRepository", "Found ${overpassResponse.elements.size} elements")

            val userLocation = LocationData(
                latitude = latitude,
                longitude = longitude,
                accuracy = 0f
            )

            val stores = overpassResponse.elements
                .mapNotNull { element ->
                    Store.fromOverpassElement(element, userLocation)
                }
                .sortedBy { it.distance }

            Log.d("OverpassRepository", "Parsed ${stores.size} stores")

            Result.success(stores)

        } catch (e: Exception) {
            Log.e("OverpassRepository", "Error fetching stores", e)
            Result.failure(e)
        }
    }

    private fun buildOverpassQuery(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): String {
        return """
            [out:json][timeout:25];
            (
              node["shop"="convenience"](around:$radiusMeters,$latitude,$longitude);
              node["shop"="supermarket"](around:$radiusMeters,$latitude,$longitude);
            );
            out body;
            >;
            out skel qt;
        """.trimIndent()
    }
}