# LAPORAN FINAL PROJECT
## Pemrograman Perangkat Bergerak (PPB)

---

| | |
|---|---|
| **Nama Aplikasi** | EcoBank — Aplikasi Bank Sampah Digital |
| **Platform** | Android |
| **Versi** | 1.0 |
| **Nama Anggota Tim** | 1. Nadya Zafira (5025231310) <br> 2. Alya Rahmatillah Machmud (5025231315) |
| **Mata Kuliah** | Pemrograman Perangkat Bergerak (PPB) C |

---

## 1. Pendahuluan

### 1.1 Latar Belakang

Program bank sampah merupakan salah satu upaya pengelolaan sampah berbasis masyarakat yang semakin berkembang di Indonesia. Dalam pelaksanaannya, bank sampah masih banyak mengandalkan pencatatan manual dan kartu member fisik sebagai bukti keanggotaan. Pendekatan ini memiliki beberapa kelemahan:

- Kartu fisik mudah hilang atau rusak
- Pencatatan poin dan transaksi dilakukan secara manual sehingga rawan kesalahan
- Pengguna tidak dapat memantau saldo poin secara real-time
- Membutuhkan biaya cetak dan operasional administrasi yang tidak efisien

Berdasarkan latar belakang tersebut, dikembangkan aplikasi **EcoBank** — sebuah platform digital untuk bank sampah yang memungkinkan pengelolaan pengguna, pencatatan setoran sampah, sistem poin otomatis, dan penukaran reward, seluruhnya dalam genggaman pengguna melalui smartphone Android.

### 1.2 Tujuan

Membangun aplikasi Android yang memungkinkan:

1. Registrasi dan pengelolaan data pengguna bank sampah
2. Penyimpanan data pengguna dan transaksi secara lokal menggunakan Room Database
3. Kartu membership digital untuk setiap pengguna
4. Sistem poin otomatis berdasarkan jenis dan berat sampah yang disetorkan
5. Riwayat transaksi setor sampah dan penukaran reward
6. Penukaran poin dengan berbagai reward (kebutuhan rumah tangga dan e-money)
7. Akses berbasis peran: Pengelola (admin) dan Pengguna (member)

---

## 2. Ruang Lingkup (Scope)

### In Scope (Fitur yang Diimplementasikan)
- Registrasi Pengguna (Member)
- Dashboard Pengelola
- Kartu Membership Digital
- Sistem Poin Otomatis
- Riwayat Transaksi
- Penukaran Reward
- Room Database (penyimpanan lokal)
- Role-Based Access (Pengelola vs Pengguna)
- Katalog Sampah (8 jenis sampah + nilai poin)

### Out of Scope (Tidak Diimplementasikan)
- Online Payment
- Cloud Database / Firebase
- Login Google
- Push Notification
- Multi Device Sync

---

## 3. Teknologi yang Digunakan

| Teknologi | Versi | Kegunaan |
|---|---|---|
| Kotlin | 2.0.21 | Bahasa pemrograman utama |
| Jetpack Compose | BOM 2024.x | Framework UI deklaratif |
| Material Design 3 | - | Komponen UI dan desain sistem |
| Room Database | 2.6.1 | Penyimpanan data lokal (SQLite wrapper) |
| Navigation Compose | 2.7.7 | Navigasi antar halaman |
| ViewModel | 2.8.0 | Manajemen state UI |
| Kotlin Coroutines | 1.7.3 | Pemrograman asinkron |
| StateFlow / Flow | - | Reaktivitas data antara ViewModel dan UI |
| KAPT | - | Annotation processing untuk Room |
| Gradle Kotlin DSL | - | Build system |

**Spesifikasi Build:**
- `minSdk`: 24 (Android 7.0 Nougat)
- `targetSdk`: 36
- `compileSdk`: 36

---

## 4. Arsitektur Aplikasi (MVVM)

Aplikasi mengikuti pola arsitektur **MVVM (Model-View-ViewModel)** dengan **Repository Pattern**, sesuai best practice Android modern.

```
+-----------------------------------------------------------+
|                    PRESENTATION LAYER                     |
|                                                           |
|  Compose UI Screens (12 screens)                          |
|  - Menampilkan state dari ViewModel                       |
|  - Mengirim event (klik, input) ke ViewModel              |
+----------------------------+------------------------------+
                             | observes StateFlow
                             v
+-----------------------------------------------------------+
|                     VIEWMODEL LAYER                       |
|                                                           |
|  EcoBankViewModel.kt                                      |
|  - Menyimpan UI state (StateFlow)                         |
|  - Mengatur business logic tingkat UI                     |
|  - Memanggil Repository untuk operasi data                |
|  - Mengelola auth state (UserRole: ADMIN / PENGGUNA)      |
+----------------------------+------------------------------+
                             | calls suspend functions
                             v
+-----------------------------------------------------------+
|                    REPOSITORY LAYER                       |
|                                                           |
|  EcoBankRepository.kt                                     |
|  - Single source of truth untuk data                      |
|  - Mengabstraksi akses ke DAO                             |
|  - Business logic: hitung poin, validasi redeem           |
+----------------------------+------------------------------+
                             | calls DAO
                             v
+-----------------------------------------------------------+
|                    DATABASE LAYER                         |
|                                                           |
|  Room Database (AppDatabase.kt)                           |
|  - PenggunaDao: CRUD pengguna, Flow queries               |
|  - TransactionDao: Insert & query transaksi               |
|  - Tabel: pengguna, transactions                          |
+-----------------------------------------------------------+
```

### Aliran Data (Data Flow)

**Membaca data (reactive):**
```
Room DB --> DAO (Flow) --> Repository --> ViewModel (StateFlow) --> Compose UI
```

**Menulis data (suspend):**
```
Compose UI --> ViewModel --> Repository --> DAO --> Room DB
(update otomatis melalui Flow yang sama)
```

---

## 5. Desain Database

### Diagram Relasi

```
+------------------+          +----------------------+
|     pengguna     |          |     transactions     |
+------------------+          +----------------------+
| id (PK)          |<---------| id (PK)              |
| name             |  1    N  | penggunaId (FK)      |
| email            |          | amount               |
| phone            |          | pointEarned          |
| points           |          | date                 |
| joinDate         |          | type (SETOR/REDEEM)  |
+------------------+          | description          |
                              +----------------------+
```

### Detail Tabel

#### Tabel `pengguna`
| Field | Type | Constraint | Keterangan |
|---|---|---|---|
| id | INTEGER | PK, AUTO | ID unik pengguna |
| name | TEXT | NOT NULL | Nama lengkap |
| email | TEXT | NOT NULL | Alamat email |
| phone | TEXT | NOT NULL | Nomor HP |
| points | INTEGER | DEFAULT 0 | Total poin saat ini |
| joinDate | TEXT | NOT NULL | Tanggal daftar (dd/MM/yyyy) |

**Extension Functions pada Pengguna:**
- `Pengguna.formattedId()` → menghasilkan format "PGN00001" (padStart 5 digit)
- `Pengguna.level()` → "Pemula" (0–199), "Aktif" (200–499), "Champion" (500+)

#### Tabel `transactions`
| Field | Type | Constraint | Keterangan |
|---|---|---|---|
| id | INTEGER | PK, AUTO | ID unik transaksi |
| penggunaId | INTEGER | FK | Relasi ke pengguna |
| amount | REAL | NOT NULL | Berat (gram) untuk SETOR; 0 untuk REDEEM |
| pointEarned | INTEGER | NOT NULL | Poin didapat / dipakai |
| date | TEXT | NOT NULL | Waktu transaksi (dd/MM/yyyy HH:mm) |
| type | TEXT | NOT NULL | "SETOR" atau "REDEEM" |
| description | TEXT | NOT NULL | Jenis sampah / nama reward |

---

## 6. Struktur File Project (Summary)

- `data/entity/Pengguna.kt`: Entity database untuk pengguna.
- `data/dao/PenggunaDao.kt`: Data Access Object untuk operasi CRUD pengguna.
- `viewmodel/EcoBankViewModel.kt`: Manajemen state dan logika aplikasi.
- `ui/screens/`: Berisi seluruh tampilan UI (PenggunaLoginScreen, HomeScreen, dll).
- `ui/navigation/NavGraph.kt`: Pengaturan rute navigasi aplikasi.

---

## 7. Kesimpulan

Aplikasi **EcoBank** telah berhasil dimigrasi menggunakan terminologi **Pengguna** untuk menggantikan Nasabah, sesuai dengan permintaan pembaruan identitas brand. Seluruh fungsi utama mulai dari registrasi, setoran sampah, hingga penukaran reward tetap berjalan normal dengan sistem database Room yang reaktif.
