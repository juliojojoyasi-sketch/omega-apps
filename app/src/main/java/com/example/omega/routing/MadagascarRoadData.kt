package com.example.omega.routing

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object MadagascarRoadData {

    fun calculateHaversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun buildGraph(): RoadGraph {
        val graph = RoadGraph()

        // 1. Major Madagascar Cities & Junction Nodes
        val nodes = listOf(
            RoadNode("TANA_ANALAKELY", "Antananarivo - Avenue de l'Indépendance", -18.9102, 47.5255),
            RoadNode("TANA_ANOSY", "Antananarivo - Lac Anosy / Ministères", -18.9168, 47.5222),
            RoadNode("TANA_MAHAMASINA", "Antananarivo - Stade Mahamasina", -18.9195, 47.5263),
            RoadNode("TANA_AMBOHIJATOVO", "Antananarivo - Ambohijatovo / Tunnel", -18.9140, 47.5285),
            RoadNode("TANA_ANKORONDRANO", "Antananarivo - Ankorondrano / Zone Industrielle", -18.8911, 47.5220),
            RoadNode("TANA_ANDRAVOAHANGY", "Antananarivo - Marché Andravoahangy", -18.8995, 47.5360),
            RoadNode("TANA_ANKATSO", "Antananarivo - Université d'Ankatso", -18.9130, 47.5540),
            RoadNode("TANA_TSARASAOTRA", "Antananarivo - Échangeur Tsarasaotra", -18.8680, 47.5180),
            RoadNode("TANA_IVATO_AERO", "Aéroport International d'Ivato", -18.7969, 47.4788),
            RoadNode("TANA_AMBOHIMANGA", "Ambohimanga Rova Historique", -18.7610, 47.5640),
            RoadNode("TANA_BYPASS_SUD", "Antananarivo - By-Pass Sud / Iavoloha", -18.9850, 47.5300),
            RoadNode("TANA_AMBOHIDRATRIMO", "Ambohidratrimo Junction RN4", -18.8250, 47.4420),
            RoadNode("TANA_ANALAVORY_JCT", "RN1 - Analavory Junction", -19.0060, 46.7230),

            // RN7 corridor (South Axis)
            RoadNode("AMBATOLAMPY", "Ambatolampy", -19.3833, 47.4167),
            RoadNode("ANTSIRABE", "Antsirabe Ville d'Eau", -19.8659, 47.0333),
            RoadNode("AMBOSITRA", "Ambositra Capitale de l'Artisanat", -20.5300, 47.2400),
            RoadNode("FIANARANTSOA", "Fianarantsoa Haute Ville", -21.4527, 47.0857),
            RoadNode("AMBALAVAO", "Ambalavao Papier Antemoro", -21.8333, 46.9333),
            RoadNode("IHOSY", "Ihosy Carrefour Grand Sud", -22.4022, 46.1264),
            RoadNode("RANOHIRA_ISALO", "Ranohira - Parc National de l'Isalo", -22.5600, 45.4100),
            RoadNode("SAKARAHA", "Sakaraha Saphir", -22.9000, 44.5300),
            RoadNode("TOLIARA", "Toliara / Tuléar Port", -23.3516, 43.6672),
            RoadNode("IFATY_BEACH", "Ifaty Plage & Récif de Corail", -23.1480, 43.6210),

            // RN2 corridor (East Axis)
            RoadNode("MANJAKANDRIANA", "Manjakandriana", -18.9167, 47.8000),
            RoadNode("MORAMANGA", "Moramanga Carrefour RN2/RN44", -18.9489, 48.2294),
            RoadNode("ANDASIBE_PARK", "Andasibe - Parc National Périnet", -18.9250, 48.4160),
            RoadNode("BRICKAVILLE", "Brickaville / Pont Rianila", -18.8200, 49.0700),
            RoadNode("TOAMASINA", "Toamasina / Tamatave Grand Port", -18.1492, 49.4023),
            RoadNode("FOULPOINTE", "Foulpointe Mahambo Plage", -17.6744, 49.5086),

            // RN4 corridor (North-West Axis)
            RoadNode("MAHITSY", "Mahitsy RN4", -18.7300, 47.3300),
            RoadNode("ANKAZOBE", "Ankazobe Plateau Tampoketsa", -18.3167, 47.1167),
            RoadNode("MAEVATANANA", "Maevatanana Ikopa", -16.9500, 46.8333),
            RoadNode("AMBATO_BOENY", "Ambato-Boeny Junction", -16.4667, 46.7167),
            RoadNode("AMBONDROMAMY", "Ambondromamy Carrefour RN4/RN6", -16.4333, 47.1500),
            RoadNode("MAHAJANGA", "Mahajanga / Majunga Baobab Géant", -15.7167, 46.3167),

            // West RN34/RN35 axis
            RoadNode("MIANDRIVAZO", "Miandrivazo Descente Tsiribihina", -19.5300, 45.4600),
            RoadNode("MORONDAVA", "Morondava Centre-Ville", -20.2986, 44.2814),
            RoadNode("BAOBABS_AVENUE", "Allée des Baobabs Morondava", -20.2510, 44.4184),
            RoadNode("TSINGY_BEMARAHA", "Bekopaka - Tsingy de Bemaraha", -19.1450, 44.8050),

            // North RN6 axis
            RoadNode("ANTSOHIHY", "Antsohihy Sofia", -14.8800, 47.9900),
            RoadNode("AMBANJA", "Ambanja Sambirano Cacao", -13.6800, 48.4500),
            RoadNode("ANKIFY_PORT", "Port d'Ankify - Embarcadère Nosy Be", -13.6650, 48.3450),
            RoadNode("NOSY_BE_HELLVILLE", "Nosy Be - Andoany / Hell-Ville", -13.4000, 48.2700),
            RoadNode("NOSY_BE_AMBATOLOAKA", "Nosy Be - Ambatoloaka Plage", -13.3667, 48.2167),
            RoadNode("AMBILOBE", "Ambilobe RN6", -13.2000, 49.0500),
            RoadNode("ANTSIRANANA", "Antsiranana / Diego-Suarez Baie", -12.2800, 49.2900)
        )

        nodes.forEach { graph.addNode(it) }

        // Helper to add bidirectional highway/road edges
        fun addEdge(from: String, to: String, name: String, roadClass: String, speed: Double) {
            val n1 = graph.getNode(from) ?: return
            val n2 = graph.getNode(to) ?: return
            val dist = calculateHaversineDistance(n1.latitude, n1.longitude, n2.latitude, n2.longitude)
            graph.addEdge(
                RoadEdge(
                    id = "${from}_${to}",
                    fromNodeId = from,
                    toNodeId = to,
                    roadName = name,
                    roadClass = roadClass,
                    lengthMeters = dist,
                    speedKmh = speed,
                    isOneWay = false
                )
            )
        }

        // --- ANTANANARIVO URBAN NETWORK ---
        addEdge("TANA_ANALAKELY", "TANA_ANOSY", "Avenue de l'Indépendance - Anosy", "URBAN", 40.0)
        addEdge("TANA_ANOSY", "TANA_MAHAMASINA", "Boulevard de la Libération", "URBAN", 35.0)
        addEdge("TANA_ANALAKELY", "TANA_AMBOHIJATOVO", "Avenue du 26 Juin / Tunnel Ambanidia", "URBAN", 30.0)
        addEdge("TANA_AMBOHIJATOVO", "TANA_MAHAMASINA", "Route Circulaire Mahamasina", "URBAN", 35.0)
        addEdge("TANA_AMBOHIJATOVO", "TANA_ANKATSO", "Route d'Ankatso", "URBAN", 40.0)
        addEdge("TANA_ANALAKELY", "TANA_ANDRAVOAHANGY", "Rue Pasteur / Andravoahangy", "URBAN", 35.0)
        addEdge("TANA_ANALAKELY", "TANA_ANKORONDRANO", "Route des Hydrocarbures", "PRIMARY", 50.0)
        addEdge("TANA_ANKORONDRANO", "TANA_TSARASAOTRA", "Boulevard de l'Europe", "PRIMARY", 60.0)
        addEdge("TANA_TSARASAOTRA", "TANA_IVATO_AERO", "Voie Rapide Tsarasaotra - Ivato", "HIGHWAY", 80.0)
        addEdge("TANA_TSARASAOTRA", "TANA_AMBOHIMANGA", "Route Historique Ambohimanga", "SECONDARY", 50.0)
        addEdge("TANA_ANOSY", "TANA_BYPASS_SUD", "RN7 Sortie Sud / Ankadimbahoaka", "PRIMARY", 60.0)
        addEdge("TANA_ANKORONDRANO", "TANA_AMBOHIDRATRIMO", "RN4 Sortie Nord-Ouest", "PRIMARY", 60.0)
        addEdge("TANA_AMBOHIDRATRIMO", "TANA_IVATO_AERO", "Route de l'Aéroport Talatamaty", "PRIMARY", 50.0)
        addEdge("TANA_BYPASS_SUD", "TANA_ANALAVORY_JCT", "RN1 Ouest Arivonimamo", "PRIMARY", 70.0)

        // --- RN7 (SOUTH AXIS: Antananarivo -> Toliara) ---
        addEdge("TANA_BYPASS_SUD", "AMBATOLAMPY", "Route Nationale 7 (RN7)", "TRUNK", 75.0)
        addEdge("AMBATOLAMPY", "ANTSIRABE", "Route Nationale 7 (RN7)", "TRUNK", 80.0)
        addEdge("ANTSIRABE", "AMBOSITRA", "Route Nationale 7 (RN7)", "TRUNK", 70.0)
        addEdge("AMBOSITRA", "FIANARANTSOA", "Route Nationale 7 (RN7)", "TRUNK", 75.0)
        addEdge("FIANARANTSOA", "AMBALAVAO", "Route Nationale 7 (RN7)", "TRUNK", 70.0)
        addEdge("AMBALAVAO", "IHOSY", "Route Nationale 7 (RN7 - Plateau Horombe)", "TRUNK", 85.0)
        addEdge("IHOSY", "RANOHIRA_ISALO", "Route Nationale 7 (RN7 - Isalo)", "TRUNK", 80.0)
        addEdge("RANOHIRA_ISALO", "SAKARAHA", "Route Nationale 7 (RN7)", "TRUNK", 80.0)
        addEdge("SAKARAHA", "TOLIARA", "Route Nationale 7 (RN7 - Tuléar)", "TRUNK", 85.0)
        addEdge("TOLIARA", "IFATY_BEACH", "Route de la Plage Ifaty", "SECONDARY", 50.0)

        // --- RN2 (EAST AXIS: Antananarivo -> Toamasina) ---
        addEdge("TANA_ANDRAVOAHANGY", "MANJAKANDRIANA", "Route Nationale 2 (RN2)", "TRUNK", 65.0)
        addEdge("MANJAKANDRIANA", "MORAMANGA", "Route Nationale 2 (RN2 - Falaise)", "TRUNK", 65.0)
        addEdge("MORAMANGA", "ANDASIBE_PARK", "RN2 - Embranchement Andasibe", "PRIMARY", 70.0)
        addEdge("ANDASIBE_PARK", "BRICKAVILLE", "Route Nationale 2 (RN2)", "TRUNK", 70.0)
        addEdge("BRICKAVILLE", "TOAMASINA", "Route Nationale 2 (RN2 - Côte Est)", "TRUNK", 80.0)
        addEdge("TOAMASINA", "FOULPOINTE", "Route Nationale 5 (RN5 Côte Nord)", "PRIMARY", 60.0)

        // --- RN4 (NORTH-WEST AXIS: Antananarivo -> Mahajanga) ---
        addEdge("TANA_AMBOHIDRATRIMO", "MAHITSY", "Route Nationale 4 (RN4)", "TRUNK", 70.0)
        addEdge("MAHITSY", "ANKAZOBE", "Route Nationale 4 (RN4)", "TRUNK", 75.0)
        addEdge("ANKAZOBE", "MAEVATANANA", "Route Nationale 4 (RN4 - Pont Betsiboka)", "TRUNK", 80.0)
        addEdge("MAEVATANANA", "AMBATO_BOENY", "Route Nationale 4 (RN4)", "TRUNK", 85.0)
        addEdge("AMBATO_BOENY", "AMBONDROMAMY", "Route Nationale 4 (RN4 Carrefour)", "TRUNK", 85.0)
        addEdge("AMBONDROMAMY", "MAHAJANGA", "Route Nationale 4 (RN4 Majunga)", "TRUNK", 85.0)

        // --- RN34 / RN35 (WEST AXIS: Antsirabe -> Morondava) ---
        addEdge("ANTSIRABE", "MIANDRIVAZO", "Route Nationale 34 (RN34)", "PRIMARY", 65.0)
        addEdge("MIANDRIVAZO", "MORONDAVA", "Route Nationale 35 (RN35)", "PRIMARY", 70.0)
        addEdge("MORONDAVA", "BAOBABS_AVENUE", "Piste des Baobabs (Allée des Baobabs)", "SECONDARY", 45.0)
        addEdge("BAOBABS_AVENUE", "TSINGY_BEMARAHA", "Piste du Menabe / Bekopaka", "SECONDARY", 40.0)

        // --- RN6 (NORTH AXIS: Ambondromamy -> Antsiranana) ---
        addEdge("AMBONDROMAMY", "ANTSOHIHY", "Route Nationale 6 (RN6)", "TRUNK", 75.0)
        addEdge("ANTSOHIHY", "AMBANJA", "Route Nationale 6 (RN6 Sambirano)", "TRUNK", 75.0)
        addEdge("AMBANJA", "ANKIFY_PORT", "Route Portuaire Ankify", "SECONDARY", 60.0)
        addEdge("ANKIFY_PORT", "NOSY_BE_HELLVILLE", "Liaison Maritime / Bac Nosy Be", "SECONDARY", 35.0)
        addEdge("NOSY_BE_HELLVILLE", "NOSY_BE_AMBATOLOAKA", "Route Côtière Ambatoloaka", "URBAN", 45.0)
        addEdge("AMBANJA", "AMBILOBE", "Route Nationale 6 (RN6)", "TRUNK", 70.0)
        addEdge("AMBILOBE", "ANTSIRANANA", "Route Nationale 6 (RN6 Diego)", "TRUNK", 80.0)

        return graph
    }
}
