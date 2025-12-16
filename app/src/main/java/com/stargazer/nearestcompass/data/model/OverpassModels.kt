package com.stargazer.nearestcompass.data.model

import com.google.gson.annotations.SerializedName

data class OverpassModels (
    @SerializedName("version")
    val version: String? = null,

    @SerializedName("elements")
    val elements: List<OverpassElement> = emptyList()
)

data class OverpassElement(
    @SerializedName("type")
    val type: String? = null,

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("lat")
    val lat: Double? = null,

    @SerializedName("lon")
    val lon: Double? = null,

    @SerializedName("tags")
    val tags: OverpassTags? = null
)

data class OverpassTags(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("shop")
    val shop: String? = null,

    @SerializedName("opening_hours")
    val openingHours: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("addr:street")
    val street: String? = null,

    @SerializedName("addr:housenumber")
    val houseNumber: String? = null
)

data class Store(
    val id: Long,
    val name: String?,
    val latitude: Double,
    val longitude: Double,
    val type: StoreType,
    val openingHours: String? = null,
    val distance: Double = 0.0
) {
    companion object{
        fun fromOverpassElement(element: OverpassElement, userLocation: LocationData): Store?{
            if ( element.lat == null || element.lon  == null || element.id == null){
                return null
            }
            val name = element.tags?.name ?: "isimsiz Market"

            val type = when (element.tags?.shop) {
                "convenience" -> StoreType.CONVENIENCE
                "supermarket" -> StoreType.SUPERMARKET
                else -> StoreType.UNKNOWN
            }

            val storeLocation = LocationData(
                latitude = element.lat,
                longitude = element.lon,
                accuracy = 0f
            )

            val distance = com.stargazer.nearestcompass.utils.LocationsUtils.calculateDistance(
                from = userLocation,
                to = storeLocation
            )

            return Store(
                id = element.id,
                name = name,
                latitude = element.lat,
                longitude = element.lon,
                type = type,
                openingHours = element.tags?.openingHours,
                distance = distance
            )
        }
    }
}

enum class StoreType {
    CONVENIENCE,    // Bakkal
    SUPERMARKET,    // Süpermarket
    UNKNOWN         // Bilinmeyen
}