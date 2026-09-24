package com.example.omega.routing

class RoadGraph {
    private val nodes = mutableMapOf<String, RoadNode>()
    private val adjacency = mutableMapOf<String, MutableList<RoadEdge>>()

    fun addNode(node: RoadNode) {
        nodes[node.id] = node
        if (!adjacency.containsKey(node.id)) {
            adjacency[node.id] = mutableListOf()
        }
    }

    fun addEdge(edge: RoadEdge) {
        adjacency.getOrPut(edge.fromNodeId) { mutableListOf() }.add(edge)
        if (!edge.isOneWay) {
            val reverseEdge = edge.copy(
                id = "${edge.id}_rev",
                fromNodeId = edge.toNodeId,
                toNodeId = edge.fromNodeId,
                geometry = edge.geometry.reversed()
            )
            adjacency.getOrPut(edge.toNodeId) { mutableListOf() }.add(reverseEdge)
        }
    }

    fun getNode(id: String): RoadNode? = nodes[id]
    fun getAllNodes(): List<RoadNode> = nodes.values.toList()
    fun getOutgoingEdges(nodeId: String): List<RoadEdge> = adjacency[nodeId] ?: emptyList()

    fun findNearestNode(latitude: Double, longitude: Double): RoadNode? {
        if (nodes.isEmpty()) return null
        var closestNode: RoadNode? = null
        var minDistance = Double.MAX_VALUE

        for (node in nodes.values) {
            val d = MadagascarRoadData.calculateHaversineDistance(
                latitude, longitude,
                node.latitude, node.longitude
            )
            if (d < minDistance) {
                minDistance = d
                closestNode = node
            }
        }
        return closestNode
    }
}
