package com.example.omega.map

import com.example.omega.data.local.entity.OfflineRegionEntity
import com.example.omega.data.local.entity.SearchPoiEntity

object DefaultMadagascarData {

    val INITIAL_REGIONS = listOf(
        OfflineRegionEntity(
            id = "mg_all",
            name = "Madagascar - National (OSM + Relief)",
            description = "Ensemble du réseau routier national, grandes villes, littoraux et relief",
            minLat = -25.6,
            maxLat = -11.9,
            minLon = 43.1,
            maxLon = 50.5,
            minZoom = 5,
            maxZoom = 11,
            estimatedSizeMb = 85,
            downloadedTiles = 1250,
            totalTiles = 1250,
            isDownloaded = true,
            downloadedAt = System.currentTimeMillis()
        ),
        OfflineRegionEntity(
            id = "mg_antananarivo",
            name = "Antananarivo & Région Analamanga",
            description = "Réseau urbain haute résolution, rues, ruelles, POI et coordonnées détaillées",
            minLat = -19.05,
            maxLat = -18.75,
            minLon = 47.35,
            maxLon = 47.65,
            minZoom = 10,
            maxZoom = 16,
            estimatedSizeMb = 42,
            downloadedTiles = 850,
            totalTiles = 850,
            isDownloaded = true,
            downloadedAt = System.currentTimeMillis()
        ),
        OfflineRegionEntity(
            id = "mg_nosy_be",
            name = "Nosy Be & Archipel du Nord",
            description = "Plages, pistes, reliefs volcaniques (Mont Passot, Lokobe, Hell-Ville)",
            minLat = -13.55,
            maxLat = -13.20,
            minLon = 48.10,
            maxLon = 48.40,
            minZoom = 10,
            maxZoom = 15,
            estimatedSizeMb = 28,
            downloadedTiles = 0,
            totalTiles = 420,
            isDownloaded = false
        ),
        OfflineRegionEntity(
            id = "mg_toamasina",
            name = "Toamasina & Côte Est (RN2)",
            description = "Grand Port, Foulpointe, littoral et corridor d'Andasibe",
            minLat = -18.30,
            maxLat = -17.50,
            minLon = 48.30,
            maxLon = 49.60,
            minZoom = 9,
            maxZoom = 15,
            estimatedSizeMb = 35,
            downloadedTiles = 0,
            totalTiles = 510,
            isDownloaded = false
        ),
        OfflineRegionEntity(
            id = "mg_mahajanga",
            name = "Mahajanga & Boeny (RN4)",
            description = "Baie de Bombetoka, plages du village touristique, Baobab géant",
            minLat = -15.85,
            maxLat = -15.60,
            minLon = 46.20,
            maxLon = 46.45,
            minZoom = 9,
            maxZoom = 15,
            estimatedSizeMb = 24,
            downloadedTiles = 0,
            totalTiles = 360,
            isDownloaded = false
        ),
        OfflineRegionEntity(
            id = "mg_toliara",
            name = "Toliara & Grand Sud (RN7)",
            description = "Tuléar, Ifaty, massif de l'Isalo et littoral corallien",
            minLat = -23.45,
            maxLat = -22.40,
            minLon = 43.55,
            maxLon = 45.50,
            minZoom = 8,
            maxZoom = 14,
            estimatedSizeMb = 38,
            downloadedTiles = 0,
            totalTiles = 490,
            isDownloaded = false
        )
    )

    val INITIAL_POIS = listOf(
        // Villes principales
        SearchPoiEntity(
            name = "Antananarivo (Tananarive)",
            localName = "Antananarivo Renivohitra",
            category = "VILLE",
            latitude = -18.9102,
            longitude = 47.5255,
            region = "Analamanga",
            description = "Capitale de Madagascar, centre historique des 12 collines sacrées",
            popularityScore = 500
        ),
        SearchPoiEntity(
            name = "Toamasina (Tamatave)",
            localName = "Toamasina",
            category = "VILLE",
            latitude = -18.1492,
            longitude = 49.4023,
            region = "Atsinanana",
            description = "Premier port maritime et économique de Madagascar sur l'Océan Indien",
            popularityScore = 400
        ),
        SearchPoiEntity(
            name = "Antsirabe",
            localName = "Antsirabe Ville d'Eau",
            category = "VILLE",
            latitude = -19.8659,
            longitude = 47.0333,
            region = "Vakinankaratra",
            description = "Station thermale, centre industriel et pousse-pousses au bord des hauts plateaux",
            popularityScore = 380
        ),
        SearchPoiEntity(
            name = "Mahajanga (Majunga)",
            localName = "Mahajanga",
            category = "VILLE",
            latitude = -15.7167,
            longitude = 46.3167,
            region = "Boeny",
            description = "Cité des fleurs sur le canal du Mozambique, célèbre pour son baobab multicentenaire",
            popularityScore = 370
        ),
        SearchPoiEntity(
            name = "Fianarantsoa",
            localName = "Fianarantsoa",
            category = "VILLE",
            latitude = -21.4527,
            longitude = 47.0857,
            region = "Haute Matsiatra",
            description = "Capitale culturelle et intellectuelle des hauts plateaux du sud",
            popularityScore = 360
        ),
        SearchPoiEntity(
            name = "Toliara (Tuléar)",
            localName = "Toliara",
            category = "VILLE",
            latitude = -23.3516,
            longitude = 43.6672,
            region = "Atsimo-Andrefana",
            description = "Cité du soleil et terminus de la célèbre Route Nationale 7 (RN7)",
            popularityScore = 350
        ),
        SearchPoiEntity(
            name = "Antsiranana (Diego-Suarez)",
            localName = "Antsiranana",
            category = "VILLE",
            latitude = -12.2800,
            longitude = 49.2900,
            region = "Diana",
            description = "Deuxième plus grande baie naturelle au monde, mer d'Émeraude",
            popularityScore = 340
        ),
        SearchPoiEntity(
            name = "Morondava",
            localName = "Morondava",
            category = "VILLE",
            latitude = -20.2986,
            longitude = 44.2814,
            region = "Menabe",
            description = "Porte d'entrée vers l'Allée des Baobabs et les Tsingy de Bemaraha",
            popularityScore = 330
        ),
        SearchPoiEntity(
            name = "Nosy Be (Hell-Ville)",
            localName = "Andoany",
            category = "VILLE",
            latitude = -13.4000,
            longitude = 48.2700,
            region = "Diana",
            description = "L'île aux parfums (Ylang-ylang), station balnéaire paradisiaque",
            popularityScore = 390
        ),
        SearchPoiEntity(
            name = "Taolagnaro (Fort-Dauphin)",
            localName = "Faradofay",
            category = "VILLE",
            latitude = -25.0381,
            longitude = 46.9987,
            region = "Anosy",
            description = "Pointe sud-est de Madagascar, baies somptueuses et climat tropical",
            popularityScore = 310
        ),

        // Rues et Avenues de Madagascar
        SearchPoiEntity(
            name = "Avenue de l'Indépendance",
            localName = "Araben'ny Fahaleovantena",
            category = "RUE",
            latitude = -18.9102,
            longitude = 47.5255,
            region = "Antananarivo - Analakely",
            description = "Artère emblématique reliant la Gare Soarano au Pavillon d'Analakely",
            popularityScore = 480
        ),
        SearchPoiEntity(
            name = "Route Nationale 7 (RN7)",
            localName = "Lalam-pirenena faha-7",
            category = "RUE",
            latitude = -19.3833,
            longitude = 47.4167,
            region = "Madagascar Sud",
            description = "Axe routier majeur de 980 km reliant Antananarivo à Toliara",
            popularityScore = 460
        ),
        SearchPoiEntity(
            name = "Route Nationale 2 (RN2)",
            localName = "Lalam-pirenena faha-2",
            category = "RUE",
            latitude = -18.9489,
            longitude = 48.2294,
            region = "Madagascar Est",
            description = "Axe commercial vital reliant Antananarivo au Grand Port de Toamasina",
            popularityScore = 450
        ),
        SearchPoiEntity(
            name = "Route Nationale 4 (RN4)",
            localName = "Lalam-pirenena faha-4",
            category = "RUE",
            latitude = -18.3167,
            longitude = 47.1167,
            region = "Madagascar Nord-Ouest",
            description = "Axe de 570 km reliant Antananarivo au port maritime de Mahajanga",
            popularityScore = 430
        ),
        SearchPoiEntity(
            name = "Route des Hydrocarbures",
            localName = "Ankorondrano",
            category = "RUE",
            latitude = -18.8911,
            longitude = 47.5220,
            region = "Antananarivo",
            description = "Axe commercial majeur bordé de sièges d'entreprises et centres commerciaux",
            popularityScore = 320
        ),
        SearchPoiEntity(
            name = "Voie Rapide Tsarasaotra - Ivato",
            localName = "Lalana Tsarasaotra",
            category = "RUE",
            latitude = -18.8350,
            longitude = 47.4980,
            region = "Antananarivo",
            description = "Voie express moderne désengorgeant l'accès à l'aéroport international",
            popularityScore = 350
        ),
        SearchPoiEntity(
            name = "Boulevard Ratsimandrava",
            localName = "Boulevard Ratsimandrava",
            category = "RUE",
            latitude = -18.9180,
            longitude = 47.5180,
            region = "Antananarivo",
            description = "Boulevard urbain traversant Anosy et les quartiers administratifs",
            popularityScore = 310
        ),
        SearchPoiEntity(
            name = "Route Circulaire (Mahamasina)",
            localName = "Route Circulaire",
            category = "RUE",
            latitude = -18.9220,
            longitude = 47.5300,
            region = "Antananarivo",
            description = "Ceinture routière historique entourant les collines centrales de la ville",
            popularityScore = 300
        ),

        // Villages et Communes
        SearchPoiEntity(
            name = "Ambohimanga Rova",
            localName = "Ambohimanga",
            category = "VILLAGE",
            latitude = -18.7610,
            longitude = 47.5640,
            region = "Analamanga",
            description = "Colline royale sacrée inscrite au patrimoine mondial de l'UNESCO",
            popularityScore = 420
        ),
        SearchPoiEntity(
            name = "Ivato",
            localName = "Ivato Renivohitra",
            category = "VILLAGE",
            latitude = -18.7969,
            longitude = 47.4788,
            region = "Analamanga",
            description = "Commune abritant l'aéroport international et la base aérienne",
            popularityScore = 400
        ),
        SearchPoiEntity(
            name = "Ambatolampy",
            localName = "Ambatolampy",
            category = "VILLAGE",
            latitude = -19.3833,
            longitude = 47.4167,
            region = "Vakinankaratra",
            description = "Célèbre village fondeur d'aluminium au pied du massif de l'Ankaratra",
            popularityScore = 340
        ),
        SearchPoiEntity(
            name = "Foulpointe (Mahavelona)",
            localName = "Mahavelona",
            category = "VILLAGE",
            latitude = -17.6744,
            longitude = 49.5086,
            region = "Atsinanana",
            description = "Village balnéaire bordé par un lagon calme abrité d'un récif corallien",
            popularityScore = 360
        ),
        SearchPoiEntity(
            name = "Ifaty",
            localName = "Ifaty Mangily",
            category = "VILLAGE",
            latitude = -23.1480,
            longitude = 43.6210,
            region = "Atsimo-Andrefana",
            description = "Village de pêcheurs Vezo et forêts d'épineux côtières",
            popularityScore = 340
        ),
        SearchPoiEntity(
            name = "Ranohira",
            localName = "Ranohira",
            category = "VILLAGE",
            latitude = -22.5600,
            longitude = 45.4100,
            region = "Ihorombe",
            description = "Village camp de base pour explorer les canyons spectaculaires de l'Isalo",
            popularityScore = 350
        ),

        // Lieux d'intérêt, Tourisme et Nature
        SearchPoiEntity(
            name = "Allée des Baobabs",
            localName = "Lalan'ny Baobab",
            category = "TOURISME",
            latitude = -20.2510,
            longitude = 44.4184,
            region = "Menabe",
            description = "Célèbre groupe de Baobabs Grandidieri millénaires le long de la piste",
            popularityScore = 490
        ),
        SearchPoiEntity(
            name = "Rova de Manjakamiadana (Palais de la Reine)",
            localName = "Rovan'i Manjakamiadana",
            category = "LIEU",
            latitude = -18.9242,
            longitude = 47.5325,
            region = "Antananarivo - Haute Ville",
            description = "Ancien palais des souverains du Royaume Merina surplombant la capitale",
            popularityScore = 480
        ),
        SearchPoiEntity(
            name = "Lac Anosy",
            localName = "Farihin'Anosy",
            category = "NATURE",
            latitude = -18.9168,
            longitude = 47.5222,
            region = "Antananarivo Centre",
            description = "Lac artificiel en forme de cœur entouré de jacarandas avec l'Ange Noir",
            popularityScore = 450
        ),
        SearchPoiEntity(
            name = "Aéroport International d'Ivato (TNR)",
            localName = "Seranam-piaramanidina Ivato",
            category = "TRANSPORT",
            latitude = -18.7969,
            longitude = 47.4788,
            region = "Antananarivo",
            description = "Principal aéroport international desservant Madagascar avec terminal moderne",
            popularityScore = 470
        ),
        SearchPoiEntity(
            name = "Parc National de l'Isalo",
            localName = "Valan-javaboarin'Isalo",
            category = "NATURE",
            latitude = -22.4500,
            longitude = 45.3300,
            region = "Ihorombe",
            description = "Massif de grès jurassique sculpté, canyons profonds et piscines naturelles",
            popularityScore = 460
        ),
        SearchPoiEntity(
            name = "Tsingy de Bemaraha",
            localName = "Tsingin'ny Bemaraha",
            category = "NATURE",
            latitude = -19.1450,
            longitude = 44.8050,
            region = "Melaky",
            description = "Forêt minérale spectaculaire d'aiguilles calcaires acérées (UNESCO)",
            popularityScore = 460
        ),
        SearchPoiEntity(
            name = "Parc National d'Andasibe-Mantadia",
            localName = "Andasibe Périnet",
            category = "NATURE",
            latitude = -18.9250,
            longitude = 48.4160,
            region = "Alaotra-Mangoro",
            description = "Forêt pluvieuse tropicale sanctuaire du plus grand lémurien vivant, l'Indri-indri",
            popularityScore = 440
        ),
        SearchPoiEntity(
            name = "Lemurs' Park",
            localName = "Lemurs' Park Imerintsiatosika",
            category = "TOURISME",
            latitude = -18.9660,
            longitude = 47.3310,
            region = "RN1 Ouest",
            description = "Réserve botanique et zoologique privée abritant 9 espèces de lémuriens en liberté",
            popularityScore = 390
        ),
        SearchPoiEntity(
            name = "Marché d'Analakely & Pavillons",
            localName = "Tsenan'Analakely",
            category = "LIEU",
            latitude = -18.9080,
            longitude = 47.5240,
            region = "Antananarivo",
            description = "Grand marché historique au cœur de la ville basse",
            popularityScore = 420
        ),
        SearchPoiEntity(
            name = "Mont Passot (Nosy Be)",
            localName = "Bongo Pisa",
            category = "NATURE",
            latitude = -13.3167,
            longitude = 48.2333,
            region = "Nosy Be",
            description = "Point culminant de Nosy Be offrant un panorama à 360° sur les lacs de cratère",
            popularityScore = 380
        ),
        SearchPoiEntity(
            name = "Lac Tritriva",
            localName = "Farihy Tritriva",
            category = "NATURE",
            latitude = -19.9210,
            longitude = 46.9270,
            region = "Antsirabe",
            description = "Lac de cratère volcanique aux eaux vert émeraude et parois vertigineuses",
            popularityScore = 370
        )
    )
}
