package com.example.omega.map

enum class MapLayerType(val displayName: String, val icon: String, val description: String) {
    CARTE("Carte", "🗺️", "Fond clair OSM, routes, villes, rivières et POI"),
    SATELLITE("Satellite", "🛰️", "Imagerie aérienne haute résolution et couleurs réelles"),
    RELIEF("Relief", "⛰️", "Courbes de niveau, ombrages topographiques et massifs")
}
