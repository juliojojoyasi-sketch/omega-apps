package com.example.omega.map

import org.osmdroid.tileprovider.tilesource.ITileSource
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.MapTileIndex

object OmegaTileSources {
    // High-performance OpenStreetMap France server - Free open access, no watermarks, full coverage of Madagascar
    val CARTE_SOURCE: ITileSource = XYTileSource(
        "OSMFr",
        0,
        20,
        256,
        ".png",
        arrayOf(
            "https://a.tile.openstreetmap.fr/osmfr/",
            "https://b.tile.openstreetmap.fr/osmfr/",
            "https://c.tile.openstreetmap.fr/osmfr/"
        ),
        "© OpenStreetMap contributors, OpenStreetMap France"
    )

    // Esri ArcGIS World Imagery - Open satellite / aerial photography for Madagascar & worldwide
    val SATELLITE_SOURCE: ITileSource = object : XYTileSource(
        "ArcGISWorldImagery",
        0,
        18,
        256,
        ".jpg",
        arrayOf(
            "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/"
        ),
        "Source: Esri, Maxar, Earthstar Geographics, CNES/Airbus DS"
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            val z = MapTileIndex.getZoom(pMapTileIndex)
            val x = MapTileIndex.getX(pMapTileIndex)
            val y = MapTileIndex.getY(pMapTileIndex)
            return "$baseUrl$z/$y/$x$mImageFilenameEnding"
        }
    }

    // OpenTopoMap - OpenStreetMap based topographic relief & elevation contours
    val RELIEF_SOURCE: ITileSource = XYTileSource(
        "OpenTopoMap",
        0,
        17,
        256,
        ".png",
        arrayOf(
            "https://a.tile.opentopomap.org/",
            "https://b.tile.opentopomap.org/",
            "https://c.tile.opentopomap.org/"
        ),
        "Map data: © OpenStreetMap contributors, SRTM | Map style: © OpenTopoMap (CC-BY-SA)"
    )

    fun getSourceForType(type: MapLayerType): ITileSource {
        return when (type) {
            MapLayerType.CARTE -> CARTE_SOURCE
            MapLayerType.SATELLITE -> SATELLITE_SOURCE
            MapLayerType.RELIEF -> RELIEF_SOURCE
        }
    }
}
