package com.example.registrasisiswa.ui.model

/**
 * Data jenis sampah yang diterima Bank Sampah EcoBank.
 *
 * CARA MENAMBAH GAMBAR:
 * 1. Tambahkan file gambar ke folder: app/src/main/res/drawable/
 * 2. Nama file sesuai imageResName di bawah (misal: ic_plastic_bottle.png)
 * 3. Di WasteCatalogScreen.kt, ganti composable WasteImagePlaceholder
 *    dengan Image(painterResource(R.drawable.<imageResName>))
 *
 * Format gambar yang disarankan: PNG 256x256px atau SVG vector
 */
data class WasteType(
    val name: String,
    val emoji: String,
    val pointsPerKg: Int,   // poin per 1 kg sampah
    val description: String,
    val category: String,
    val imageResName: String  // nama file di res/drawable/ (tanpa ekstensi)
)

data class EcoReward(
    val name: String,
    val emoji: String,
    val pointCost: Int,
    val description: String,
    val category: String
)

val WASTE_TYPES = listOf(
    // PLASTIK
    // Gambar: app/src/main/res/drawable/ic_plastic_bottle.png
    WasteType(
        name = "Botol Plastik",
        emoji = "🍶",
        pointsPerKg = 50,
        description = "Botol PET/HDPE, botol air minum",
        category = "Plastik",
        imageResName = "ic_plastic_bottle"
    ),
    // Gambar: app/src/main/res/drawable/ic_plastic_bag.png
    WasteType(
        name = "Plastik Lembaran",
        emoji = "🛍️",
        pointsPerKg = 30,
        description = "Kantong plastik, kemasan sachet",
        category = "Plastik",
        imageResName = "ic_plastic_bag"
    ),
    // KERTAS
    // Gambar: app/src/main/res/drawable/ic_cardboard.png
    WasteType(
        name = "Kardus/Karton",
        emoji = "📦",
        pointsPerKg = 30,
        description = "Kardus bekas, karton tebal, dus",
        category = "Kertas",
        imageResName = "ic_cardboard"
    ),
    // Gambar: app/src/main/res/drawable/ic_paper.png
    WasteType(
        name = "Kertas",
        emoji = "📰",
        pointsPerKg = 20,
        description = "Koran, majalah, buku, HVS bekas",
        category = "Kertas",
        imageResName = "ic_paper"
    ),
    // LOGAM
    // Gambar: app/src/main/res/drawable/ic_aluminum_can.png
    WasteType(
        name = "Kaleng Aluminium",
        emoji = "🥫",
        pointsPerKg = 100,
        description = "Kaleng minuman, kaleng makanan",
        category = "Logam",
        imageResName = "ic_aluminum_can"
    ),
    // Gambar: app/src/main/res/drawable/ic_scrap_metal.png
    WasteType(
        name = "Besi/Logam",
        emoji = "⚙️",
        pointsPerKg = 40,
        description = "Pipa besi, besi tua, logam campur",
        category = "Logam",
        imageResName = "ic_scrap_metal"
    ),
    // KACA
    // Gambar: app/src/main/res/drawable/ic_glass.png
    WasteType(
        name = "Kaca/Gelas",
        emoji = "🫙",
        pointsPerKg = 30,
        description = "Botol kaca, gelas pecah (bungkus aman!)",
        category = "Kaca",
        imageResName = "ic_glass"
    ),
    // ELEKTRONIK
    // Gambar: app/src/main/res/drawable/ic_electronic.png
    WasteType(
        name = "Elektronik",
        emoji = "📱",
        pointsPerKg = 150,
        description = "HP, laptop, charger, kabel, baterai",
        category = "Elektronik",
        imageResName = "ic_electronic"
    ),
)

val ECO_REWARDS = listOf(
    // RUMAH TANGGA
    EcoReward(
        name = "Sabun Mandi",
        emoji = "🧼",
        pointCost = 50,
        description = "Sabun mandi batang 75gr dari mitra",
        category = "Rumah Tangga"
    ),
    EcoReward(
        name = "Deterjen 500gr",
        emoji = "🧺",
        pointCost = 100,
        description = "Deterjen bubuk kemasan 500gr",
        category = "Rumah Tangga"
    ),
    EcoReward(
        name = "Minyak Goreng 1L",
        emoji = "🫙",
        pointCost = 150,
        description = "Minyak goreng kemasan 1 liter",
        category = "Rumah Tangga"
    ),
    EcoReward(
        name = "Beras 1kg",
        emoji = "🌾",
        pointCost = 300,
        description = "Beras medium kemasan 1kg",
        category = "Rumah Tangga"
    ),
    // E-MONEY
    EcoReward(
        name = "E-Money Rp500",
        emoji = "💳",
        pointCost = 500,
        description = "Transfer ke GoPay/OVO/Dana",
        category = "E-Money"
    ),
    EcoReward(
        name = "E-Money Rp2.000",
        emoji = "💳",
        pointCost = 2000,
        description = "Transfer ke GoPay/OVO/Dana",
        category = "E-Money"
    ),
    EcoReward(
        name = "E-Money Rp5.000",
        emoji = "💳",
        pointCost = 5000,
        description = "Transfer ke GoPay/OVO/Dana",
        category = "E-Money"
    ),
)
