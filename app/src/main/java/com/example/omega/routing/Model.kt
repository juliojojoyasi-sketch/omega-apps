package com.example.omega.routing

data class RoadNode(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

enum class TurnType {
    START,
    STRAIGHT,
    SLIGHT_RIGHT,
    TURN_RIGHT,
    SHARP_RIGHT,
    SLIGHT_LEFT,
    TURN_LEFT,
    SHARP_LEFT,
    ROUNDABOUT,
    DESTINATION
}

data class NavigationStep(
    val instruction: String,
    val roadName: String,
    val distanceMeters: Double,
    val durationSeconds: Long,
    val turnType: TurnType,
    val startLat: Double,
    val startLon: Double
)

data class LatLon(
    val latitude: Double,
    val longitude: Double
)

data class RoadEdge(
    val id: String,
    val fromNodeId: String,
    val toNodeId: String,
    val roadName: String,
    val roadClass: String, // "HIGHWAY", "TRUNK", "PRIMARY", "SECONDARY", "URBAN"
    val lengthMeters: Double,
    val speedKmh: Double,
    val isOneWay: Boolean = false,
    val geometry: List<LatLon> = emptyList()
)

data class RouteResult(
    val totalDistanceMeters: Double,
    val totalDurationSeconds: Long,
    val points: List<LatLon>,
    val steps: List<NavigationStep>,
    val destinationName: String
)
