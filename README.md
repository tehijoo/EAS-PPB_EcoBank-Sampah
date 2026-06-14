# EcoBank — Aplikasi Bank Sampah Digital

Aplikasi Android untuk pengelolaan **Bank Sampah** berbasis sistem membership digital. User dapat mendaftar, menyetorkan sampah, mengumpulkan poin, dan menukar poin dengan reward. Pengelola bank sampah dapat mengelola seluruh data User dan transaksi.

> Dikembangkan sebagai **Final Project** mata kuliah Pemrograman Perangkat Bergerak (PPB) menggunakan Kotlin + Jetpack Compose + Room Database + MVVM Architecture.

---

## Fitur Utama

### Untuk Pengelola (Admin)
| Fitur | Deskripsi |
|---|---|
| Login Admin | Autentikasi dengan username & password |
| Dashboard Member | Lihat total User + daftar semua User |
| Tambah User | Registrasi User baru (nama, email, nomor HP) |
| Detail User | Lihat profil + poin + level User |
| Hapus User | Hapus data User beserta riwayat transaksinya |
| Katalog Sampah | Lihat jenis sampah dan nilai poin per kg |

### Untuk User (Member)
| Fitur | Deskripsi |
|---|---|
| Login / Daftar | Cari akun dengan nama/ID atau daftar sebagai User baru |
| Kartu Member Digital | Tampilkan kartu membership digital (ID NSB, nama, level, poin) |
| Setor Sampah | Pilih jenis sampah + input berat → poin dihitung otomatis |
| Riwayat Transaksi | Lihat semua riwayat setor dan penukaran reward |
| Tukar Reward | Tukar poin dengan hadiah (kebutuhan rumah tangga & e-money) |
| Katalog Sampah | Lihat 8 kategori jenis sampah beserta nilai poinnya |

### Sistem Poin
- Setiap jenis sampah memiliki nilai poin berbeda per kg
- Contoh: Kaleng Aluminium = 100 poin/kg, Botol Plastik = 50 poin/kg
- Level User otomatis naik berdasarkan total poin:
  - Pemula  — 0–199 poin
  - Aktif   — 200–499 poin
  - Champion — 500+ poin

### Reward yang Tersedia
| Reward | Poin |
|---|---|
| Sabun Mandi | 50 poin |
| Deterjen | 100 poin |
| Minyak Goreng | 150 poin |
| Beras | 300 poin |
| E-Money Rp500 | 500 poin |
| E-Money Rp2.000 | 2.000 poin |
| E-Money Rp5.000 | 5.000 poin |

---

## Teknologi

| Komponen | Teknologi |
|---|---|
| Bahasa | Kotlin |
| UI Framework | Jetpack Compose + Material Design 3 |
| Database | Room Database v2.6.1 |
| Arsitektur | MVVM + Repository Pattern |
| Navigasi | Navigation Compose v2.7.7 |
| State Management | Kotlin Coroutines + StateFlow + Flow |
| Build System | Gradle Kotlin DSL |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |

---

## Persyaratan

### Menjalankan Source Code
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17+
- Android SDK API 24–36 terinstall
- Device / emulator Android 7.0+

### Install APK Langsung
- Smartphone Android 7.0 (Nougat) atau lebih baru
- Aktifkan **Install dari Sumber Tidak Dikenal** di Pengaturan HP

---

## Cara Menjalankan

### Opsi A — Dari Android Studio

```bash
1. Buka Android Studio
2. File → Open → pilih folder project ini
3. Tunggu Gradle sync selesai
4. Hubungkan device / jalankan emulator
5. Klik Run (Shift+F10)
```

### Opsi B — Install APK

```
1. Copy file EcoBank.apk ke smartphone
2. Buka File Manager → tap file APK
3. Izinkan instalasi dari sumber tidak dikenal
4. Tap Install → Buka
```

### Kredensial Login Admin
```
Username : admin
Password : ecobank123
```

### Cara Login User
- Tab **Masuk** → ketik nama atau ID (NSB00001) → tap akun → masuk
- Tab **Daftar Baru** → isi nama, email, nomor HP → daftar & langsung masuk

---

## Alur Aplikasi

```
[Splash Screen]
      |
[Role Selection]  <--------------------------------- (logout)
      |                                                  |
      +---> [Login Pengelola]                            |
      |           |                                      |
      |     [Home / Dashboard]                           |
      |           |-- Tambah User                     |
      |           |-- Katalog Sampah                     |
      |           |-- Detail User                     |
      |                   |-- Kartu Member               |
      |                   |-- Setor Sampah               |
      |                   |-- Riwayat Transaksi          |
      |                   |-- Tukar Reward               |
      |                   |-- Katalog Sampah             |
      |                                                  |
      +---> [Login / Daftar User]                     |
                  |                                      |
            [Detail User (akun sendiri)]              |
                   |-- Kartu Member                      |
                   |-- Setor Sampah                      |
                   |-- Riwayat Transaksi                 |
                   |-- Tukar Reward                      |
                   |-- Katalog Sampah                    |
                   +-- [Logout] ----------------------->>+
```

---

## Struktur Project

```
app/src/main/java/com/example/registrasisiswa/
|
+-- MainActivity.kt                     # Entry point, setup ViewModel
|
+-- data/
|   +-- AppDatabase.kt                  # Room Database singleton (version 2)
|   +-- entity/
|   |   +-- Member.kt                   # Entity User + formattedId() + level()
|   |   +-- Transaction.kt              # Entity transaksi (SETOR/REDEEM)
|   +-- dao/
|   |   +-- MemberDao.kt                # CRUD + Flow queries untuk User
|   |   +-- TransactionDao.kt           # Insert + query transaksi per member
|   +-- repository/
|       +-- EcoBankRepository.kt        # Business logic poin & redeem reward
|
+-- viewmodel/
|   +-- EcoBankViewModel.kt             # StateFlow, auth (UserRole), operasi CRUD
|
+-- ui/
    +-- model/
    |   +-- EcoBankData.kt              # Data statis: 8 WasteType, 7 EcoReward
    +-- navigation/
    |   +-- NavGraph.kt                 # 12 routes, NavHost, sealed class Screen
    +-- screens/
    |   +-- SplashScreen.kt             # Animasi logo dengan spring + tween
    |   +-- RoleSelectionScreen.kt      # Pilih role: Pengelola / User
    |   +-- AdminLoginScreen.kt         # Form login username + password
    |   +-- UserLoginScreen.kt       # Cari akun / daftar User baru
    |   +-- HomeScreen.kt               # Dashboard admin: list semua User
    |   +-- AddMemberScreen.kt          # Form tambah User (admin only)
    |   +-- MemberDetailScreen.kt       # Profil User + grid menu aksi
    |   +-- MemberCardScreen.kt         # Kartu member digital
    |   +-- AddTransactionScreen.kt     # Pilih jenis sampah + input berat
    |   +-- TransactionHistoryScreen.kt # Riwayat SETOR & REDEEM
    |   +-- RewardScreen.kt             # Daftar reward + konfirmasi penukaran
    |   +-- WasteCatalogScreen.kt       # Katalog 8 jenis sampah & nilai poin
    +-- theme/
        +-- Color.kt                    # Rose gold palette + utility colors
        +-- Theme.kt                    # EcoBankTheme (Material 3, light mode)
        +-- Type.kt                     # Typography

app/src/main/res/
+-- drawable/
|   +-- ic_launcher_background.xml      # Background icon: rose gold
|   +-- ic_launcher_foreground.xml      # Foreground: simbol daur ulang + daun
+-- mipmap-anydpi-v26/
|   +-- ic_launcher.xml                 # Adaptive icon (API 26+)
|   +-- ic_launcher_round.xml           # Adaptive icon versi bulat
+-- values/
    +-- strings.xml                     # app_name = "EcoBank"
    +-- colors.xml
    +-- themes.xml
```

---

## Database Design

### Tabel `members`
| Field | Type | Keterangan |
|---|---|---|
| id | INTEGER | Primary Key, auto generate |
| name | TEXT | Nama lengkap User |
| email | TEXT | Email User |
| phone | TEXT | Nomor HP |
| points | INTEGER | Total poin (default 0) |
| joinDate | TEXT | Tanggal bergabung (dd/MM/yyyy) |

### Tabel `transactions`
| Field | Type | Keterangan |
|---|---|---|
| id | INTEGER | Primary Key, auto generate |
| memberId | INTEGER | Relasi ke tabel members |
| amount | REAL | Berat sampah dalam gram (SETOR) / 0 (REDEEM) |
| pointEarned | INTEGER | Poin diperoleh (SETOR) atau digunakan (REDEEM) |
| date | TEXT | Tanggal & jam (dd/MM/yyyy HH:mm) |
| type | TEXT | "SETOR" atau "REDEEM" |
| description | TEXT | Nama jenis sampah / nama reward |

---

## Checklist Definition of Done

| Requirement | Status | Implementasi |
|---|---|---|
| Registrasi member | DONE | AddMemberScreen + UserLoginScreen (tab Daftar) |
| Data tersimpan di Room Database | DONE | AppDatabase + MemberDao + TransactionDao |
| Membership card tampil | DONE | MemberCardScreen |
| Poin dihitung otomatis | DONE | EcoBankRepository.addWasteTransaction() |
| Riwayat transaksi tampil | DONE | TransactionHistoryScreen |
| Reward dapat ditukar | DONE | RewardScreen + EcoBankRepository.redeemReward() |
| Navigasi antar halaman | DONE | NavGraph.kt (12 routes) |
| Tidak error saat pengujian | DONE | Diuji pada emulator & device fisik |

---

## Catatan

- Aplikasi menggunakan penyimpanan **lokal** (Room Database) — data tidak tersinkronisasi ke cloud
- Login Google, Push Notification, dan Online Payment berada di luar scope (sesuai PRD)
- Pada device Android < 8.0, icon launcher menggunakan mipmap PNG default; pada Android 8.0+ menggunakan adaptive icon rose gold
