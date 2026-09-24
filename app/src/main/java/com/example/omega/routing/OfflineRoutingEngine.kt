package com.example.omega.routing

import java.util.PriorityQueue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class OfflineRoutingEngine(val graph: RoadGraph = MadagascarRoadData.buildGraph()) {

    companion object {
        fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val phi1 = Math.toRadians(lat1)
            val phi2 = Math.toRadians(lat2)
            val deltaLambda = Math.toRadians(lon2 - lon1)
            val y = sin(deltaLambda) * cos(phi2)
            val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * deltaLambda
            val bearing = Math.toDegrees(atan2(y, x))
            return (bearing + 360.0) % 360.0
        }
    }

    private data class AStarNode(
        val nodeId: String,
        val gScore: Double, // travel time in seconds
        val fScore: Double  // gScore + heuristic
    ) : Comparable<AStarNode> {
        override fun compareTo(other: AStarNode): Int = this.fScore.compareTo(other.fScore)
    }

    fun calculateRoute(
        startLat: Double,
        startLon: Double,
        destLat: Double,
        destLon: Double,
        destinationName: String = "Destination sélectionnée"
    ): RouteResult? {
        val startNode = graph.findNearestNode(startLat, startLon) ?: return null
        val targetNode = graph.findNearestNode(destLat, destLon) ?: return null

        val openSet = PriorityQueue<AStarNode>()
        val cameFrom = mutableMapOf<String, RoadEdge>()
        val gScore = mutableMapOf<String, Double>()
        val closedSet = mutableSetOf<String>()

        val maxSpeedMps = 100.0 * 1000.0 / 3600.0 // 100 km/h in m/s for admissible heuristic

        gScore[startNode.id] = 0.0
        val initialHeuristic = MadagascarRoadData.calculateHaversineDistance(
            startNode.latitude, startNode.longitude,
            targetNode.latitude, targetNode.longitude
        ) / maxSpeedMps

        openSet.add(AStarNode(startNode.id, 0.0, initialHeuristic))

        var found = false

        while (openSet.isNotEmpty()) {
            val current = openSet.poll() ?: break
            val currentId = current.nodeId

            if (currentId == targetNode.id) {
                found = true
                break
            }

            if (closedSet.contains(currentId)) continue
            closedSet.add(currentId)

            for (edge in graph.getOutgoingEdges(currentId)) {
                val neighborId = edge.toNodeId
                if (closedSet.contains(neighborId)) continue

                val speedMps = (edge.speedKmh * 1000.0) / 3600.0
                val edgeDuration = edge.lengthMeters / speedMps
                val tentativeGScore = (gScore[currentId] ?: Double.MAX_VALUE) + edgeDuration

                if (tentativeGScore < (gScore[neighborId] ?: Double.MAX_VALUE)) {
                    cameFrom[neighborId] = edge
                    gScore[neighborId] = tentativeGScore

                    val neighborNode = graph.getNode(neighborId)
                    val h = if (neighborNode != null) {
                        MadagascarRoadData.calculateHaversineDistance(
                            neighborNode.latitude, neighborNode.longitude,
                            targetNode.latitude, targetNode.longitude
                        ) / maxSpeedMps
                    } else 0.0

                    openSet.add(AStarNode(neighborId, tentativeGScore, tentativeGScore + h))
                }
            }
        }

        // Build route points and edges
        val routeEdges = mutableListOf<RoadEdge>()
        var curr = targetNode.id
        while (cameFrom.containsKey(curr)) {
            val edge = cameFrom[curr] ?: break
            routeEdges.add(0, edge)
            curr = edge.fromNodeId
        }

        // If start and target are the same or direct line
        val allPoints = mutableListOf<LatLon>()
        allPoints.add(LatLon(startLat, startLon))

        if (routeEdges.isEmpty()) {
            // Direct route between points
            allPoints.add(LatLon(destLat, destLon))
            val dist = MadagascarRoadData.calculateHaversineDistance(startLat, startLon, destLat, destLon)
            val dur = (dist / (40.0 * 1000.0 / 3600.0)).toLong().coerceAtLeast(30L)
            val directStep = NavigationStep(
                instruction = "Dirigez-vous vers $destinationName",
                roadName = "Itinéraire direct",
                distanceMeters = dist,
                durationSeconds = dur,
                turnType = TurnType.START,
                startLat = startLat,
                startLon = startLon
            )
            val destStep = NavigationStep(
                instruction = "Arrivée à destination : $destinationName",
                roadName = destinationName,
                distanceMeters = 0.0,
                durationSeconds = 0L,
                turnType = TurnType.DESTINATION,
                startLat = destLat,
                startLon = destLon
            )
            return RouteResult(
                totalDistanceMeters = dist,
                totalDurationSeconds = dur,
                points = allPoints,
                steps = listOf(directStep, destStep),
                destinationName = destinationName
            )
        }

        var totalDist = 0.0
        var totalSeconds = 0L

        // Add first connection from start to first node if far
        val distToFirstNode = MadagascarRoadData.calculateHaversineDistance(
            startLat, startLon, startNode.latitude, startNode.longitude
        )
        if (distToFirstNode > 50) {
            allPoints.add(LatLon(startNode.latitude, startNode.longitude))
            totalDist += distToFirstNode
            totalSeconds += (distToFirstNode / (35.0 * 1000.0 / 3600.0)).toLong()
        }

        for (edge in routeEdges) {
            totalDist += edge.lengthMeters
            val speedMps = (edge.speedKmh * 1000.0) / 3600.0
            totalSeconds += (edge.lengthMeters / speedMps).toLong()

            val toNode = graph.getNode(edge.toNodeId)
            if (toNode != null) {
                allPoints.add(LatLon(toNode.latitude, toNode.longitude))
            }
        }

        // Add final connection to destination
        val distFromLastNode = MadagascarRoadData.calculateHaversineDistance(
            targetNode.latitude, targetNode.longitude, destLat, destLon
        )
        if (distFromLastNode > 50) {
            allPoints.add(LatLon(destLat, destLon))
            totalDist += distFromLastNode
            totalSeconds += (distFromLastNode / (35.0 * 1000.0 / 3600.0)).toLong()
        }

        // Generate Turn-By-Turn Steps
        val steps = mutableListOf<NavigationStep>()

        // 1st Step: Start
        val firstEdge = routeEdges.first()
        val firstNode = graph.getNode(firstEdge.fromNodeId) ?: startNode
        steps.add(
            NavigationStep(
                instruction = "Prenez ${firstEdge.roadName} en direction de ${graph.getNode(firstEdge.toNodeId)?.name ?: destinationName}",
                roadName = firstEdge.roadName,
                distanceMeters = firstEdge.lengthMeters,
                durationSeconds = (firstEdge.lengthMeters / ((firstEdge.speedKmh * 1000.0) / 3600.0)).toLong(),
                turnType = TurnType.START,
                startLat = firstNode.latitude,
                startLon = firstNode.longitude
            )
        )

        // Intermediate Steps with turns
        for (i in 0 until routeEdges.size - 1) {
            val edge1 = routeEdges[i]
            val edge2 = routeEdges[i + 1]
            val nodeBetween = graph.getNode(edge1.toNodeId) ?: continue
            val nodeBefore = graph.getNode(edge1.fromNodeId) ?: continue
            val nodeAfter = graph.getNode(edge2.toNodeId) ?: continue

            val b1 = calculateBearing(nodeBefore.latitude, nodeBefore.longitude, nodeBetween.latitude, nodeBetween.longitude)
            val b2 = calculateBearing(nodeBetween.latitude, nodeBetween.longitude, nodeAfter.latitude, nodeAfter.longitude)
            val angleDiff = ((b2 - b1 + 540.0) % 360.0) - 180.0

            val turnType: TurnType
            val turnText: String
            when {
                angleDiff > 120.0 -> {
                    turnType = TurnType.SHARP_RIGHT
                    turnText = "Tournez fortement à droite sur ${edge2.roadName}"
                }
                angleDiff > 45.0 -> {
                    turnType = TurnType.TURN_RIGHT
                    turnText = "Tournez à droite sur ${edge2.roadName}"
                }
                angleDiff > 20.0 -> {
                    turnType = TurnType.SLIGHT_RIGHT
                    turnText = "Serrez à droite sur ${edge2.roadName}"
                }
                angleDiff < -120.0 -> {
                    turnType = TurnType.SHARP_LEFT
                    turnText = "Tournez fortement à gauche sur ${edge2.roadName}"
                }
                angleDiff < -45.0 -> {
                    turnType = TurnType.TURN_LEFT
                    turnText = "Tournez à gauche sur ${edge2.roadName}"
                }
                angleDiff < -20.0 -> {
                    turnType = TurnType.SLIGHT_LEFT
                    turnText = "Serrez à gauche sur ${edge2.roadName}"
                }
                else -> {
                    turnType = TurnType.STRAIGHT
                    turnText = "Continuez tout droit sur ${edge2.roadName}"
                }
            }

            val stepDuration = (edge2.lengthMeters / ((edge2.speedKmh * 1000.0) / 3600.0)).toLong()
            steps.add(
                NavigationStep(
                    instruction = turnText,
                    roadName = edge2.roadName,
                    distanceMeters = edge2.lengthMeters,
                    durationSeconds = stepDuration,
                    turnType = turnType,
                    startLat = nodeBetween.latitude,
                    startLon = nodeBetween.longitude
                )
            )
        }

        // Final Destination Step
        steps.add(
            NavigationStep(
                instruction = "Vous êtes arrivé à destination : $destinationName",
                roadName = destinationName,
                distanceMeters = 0.0,
                durationSeconds = 0L,
                turnType = TurnType.DESTINATION,
                startLat = destLat,
                startLon = destLon
            )
        )

        return RouteResult(
            totalDistanceMeters = totalDist,
            totalDurationSeconds = totalSeconds.coerceAtLeast(60L),
            points = allPoints,
            steps = steps,
            destinationName = destinationName
        )
    }
}
