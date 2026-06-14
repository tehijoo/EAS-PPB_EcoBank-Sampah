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
- Nasabah tidak dapat memantau saldo poin secara real-time
- Membutuhkan biaya cetak dan operasional administrasi yang tidak efisien

Berdasarkan latar belakang tersebut, dikembangkan aplikasi **EcoBank** — sebuah platform digital untuk bank sampah yang memungkinkan pengelolaan nasabah, pencatatan setoran sampah, sistem poin otomatis, dan penukaran reward, seluruhnya dalam genggaman pengguna melalui smartphone Android.

### 1.2 Tujuan

Membangun aplikasi Android yang memungkinkan:

1. Registrasi dan pengelolaan data nasabah bank sampah
2. Penyimpanan data nasabah dan transaksi secara lokal menggunakan Room Database
3. Kartu membership digital untuk setiap nasabah
4. Sistem poin otomatis berdasarkan jenis dan berat sampah yang disetorkan
5. Riwayat transaksi setor sampah dan penukaran reward
6. Penukaran poin dengan berbagai reward (kebutuhan rumah tangga dan e-money)
7. Akses berbasis peran: Pengelola (admin) dan Nasabah (member)

---

## 2. Ruang Lingkup (Scope)

### In Scope (Fitur yang Diimplementasikan)
- Registrasi Nasabah (Member)
- Dashboard Pengelola
- Kartu Membership Digital
- Sistem Poin Otomatis
- Riwayat Transaksi
- Penukaran Reward
- Room Database (penyimpanan lokal)
- Role-Based Access (Pengelola vs Nasabah)
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
| Kotlin | 1.9.x | Bahasa pemrograman utama |
| Jetpack Compose | BOM 2024.x | Framework UI deklaratif |
| Material Design 3 | - | Komponen UI dan desain sistem |
| Room Database | 2.6.1 | Penyimpanan data lokal (SQLite wrapper) |
| Navigation Compose | 2.7.7 | Navigasi antar halaman |
| ViewModel + LiveData | 2.8.0 | Manajemen state UI |
| Kotlin Coroutines | 1.7.3 | Pemrograman asinkron |
| StateFlow / Flow | - | Reaktivitas data antara ViewModel dan UI |
| KAPT | - | Annotation processing untuk Room |
| Gradle Kotlin DSL | - | Build system |
| Material Icons Extended | - | Ikon material design |

**Spesifikasi Build:**
- `minSdk`: 24 (Android 7.0 Nougat)
- `targetSdk`: 36
- `compileSdk`: 36
- Kotlin Compiler Extension: 1.5.14

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
|  - Mengelola auth state (UserRole: ADMIN / NASABAH)       |
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
|  - MemberDao: CRUD nasabah, Flow queries                  |
|  - TransactionDao: Insert & query transaksi               |
|  - Tabel: members, transactions                           |
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

### Penggunaan `flatMapLatest`
Di ViewModel, `currentMember` dan `currentTransactions` menggunakan `flatMapLatest` pada `_currentMemberId`:

```kotlin
val currentMember = _currentMemberId.flatMapLatest { id ->
    if (id == -1) flowOf(null)
    else repository.getMemberById(id)
}
```

Ini memastikan ketika nasabah yang dipilih berubah, data yang ditampilkan ikut berubah secara reaktif tanpa perlu memanggil ulang secara manual.

---

## 5. Desain Database

### Diagram Relasi

```
+------------------+          +----------------------+
|     members      |          |     transactions     |
+------------------+          +----------------------+
| id (PK)          |<---------| id (PK)              |
| name             |  1    N  | memberId (FK)        |
| email            |          | amount               |
| phone            |          | pointEarned          |
| points           |          | date                 |
| joinDate         |          | type (SETOR/REDEEM)  |
+------------------+          | description          |
                              +----------------------+
```

### Detail Tabel

#### Tabel `members`
| Field | Type | Constraint | Keterangan |
|---|---|---|---|
| id | INTEGER | PK, AUTO | ID unik nasabah |
| name | TEXT | NOT NULL | Nama lengkap |
| email | TEXT | NOT NULL | Alamat email |
| phone | TEXT | NOT NULL | Nomor HP |
| points | INTEGER | DEFAULT 0 | Total poin saat ini |
| joinDate | TEXT | NOT NULL | Tanggal daftar (dd/MM/yyyy) |

**Extension Functions pada Member:**
- `Member.formattedId()` → menghasilkan format "NSB00001" (padStart 5 digit)
- `Member.level()` → "Pemula" (0–199), "Aktif" (200–499), "Champion" (500+)

#### Tabel `transactions`
| Field | Type | Constraint | Keterangan |
|---|---|---|---|
| id | INTEGER | PK, AUTO | ID unik transaksi |
| memberId | INTEGER | FK | Relasi ke nasabah |
| amount | REAL | NOT NULL | Berat (gram) untuk SETOR; 0 untuk REDEEM |
| pointEarned | INTEGER | NOT NULL | Poin didapat / dipakai |
| date | TEXT | NOT NULL | Waktu transaksi (dd/MM/yyyy HH:mm) |
| type | TEXT | NOT NULL | "SETOR" atau "REDEEM" |
| description | TEXT | NOT NULL | Jenis sampah / nama reward |

### Formula Poin
```
pointEarned = (beratGram * pointsPerKg) / 1000

Contoh: Botol Plastik 500 gram
pointEarned = (500 * 50) / 1000 = 25 poin
```

### Nilai Poin per Jenis Sampah
| Jenis Sampah | Poin/kg |
|---|---|
| Botol Plastik | 50 |
| Plastik Lembaran | 30 |
| Kardus / Karton | 30 |
| Kertas | 20 |
| Kaleng Aluminium | 100 |
| Besi / Logam | 40 |
| Kaca / Gelas | 30 |
| Elektronik (e-waste) | 150 |

---

## 6. Struktur File Project

```
PPB-Tugas-13-Registrasi-Siswa-/
|-- README.md
|-- LAPORAN.md
|-- build.gradle.kts                    (root build file)
|-- settings.gradle.kts
|-- gradle.properties
|
+-- app/
    |-- build.gradle.kts                (dependencies & build config)
    |
    +-- src/main/
        |-- AndroidManifest.xml         (deklarasi Activity, launcher, theme)
        |
        +-- java/com/example/registrasisiswa/
        |   |
        |   |-- MainActivity.kt
        |   |   Fungsi: Entry point aplikasi. Membuat instance AppDatabase,
        |   |   Repository, ViewModelFactory, dan menjalankan NavGraph dalam
        |   |   EcoBankTheme.
        |   |
        |   +-- data/
        |   |   |-- AppDatabase.kt
        |   |   |   Fungsi: Definisi Room Database. Singleton pattern dengan
        |   |   |   @Volatile INSTANCE. Nama DB: "ecobank_database", version 2.
        |   |   |   Menggunakan fallbackToDestructiveMigration().
        |   |   |
        |   |   +-- entity/
        |   |   |   |-- Member.kt
        |   |   |   |   Fungsi: @Entity untuk tabel "members". Berisi field data
        |   |   |   |   nasabah dan extension functions formattedId() & level().
        |   |   |   |
        |   |   |   +-- Transaction.kt
        |   |   |       Fungsi: @Entity untuk tabel "transactions". Menyimpan
        |   |   |       detail setiap transaksi SETOR dan REDEEM.
        |   |   |
        |   |   +-- dao/
        |   |   |   |-- MemberDao.kt
        |   |   |   |   Fungsi: @Dao untuk operasi CRUD nasabah.
        |   |   |   |   Queries: getAllMembers() (Flow), getMemberById() (Flow),
        |   |   |   |   getTotalMembers() (Flow), insertMember (returns Long),
        |   |   |   |   updateMember, deleteMember.
        |   |   |   |
        |   |   |   +-- TransactionDao.kt
        |   |   |       Fungsi: @Dao untuk transaksi.
        |   |   |       Queries: insertTransaction, getTransactionsByMemberId() (Flow).
        |   |   |
        |   |   +-- repository/
        |   |       +-- EcoBankRepository.kt
        |   |           Fungsi: Abstraksi akses data + business logic.
        |   |           - addWasteTransaction(): hitung poin, insert transaksi,
        |   |             update poin nasabah dalam 1 coroutine scope.
        |   |           - redeemReward(): validasi poin cukup, insert REDEEM,
        |   |             kurangi poin nasabah. Return Boolean sukses/gagal.
        |   |           - insertMemberAndGetId(): insert nasabah + return ID baru.
        |   |
        |   +-- viewmodel/
        |   |   +-- EcoBankViewModel.kt
        |   |       Fungsi: Bridge antara UI dan Repository.
        |   |       - UserRole enum (ADMIN, NASABAH)
        |   |       - StateFlows: allMembers, totalMembers, userRole,
        |   |         currentMember, currentTransactions, uiMessage
        |   |       - Auth: loginAsAdmin(), loginAsNasabah(), logout()
        |   |       - registerAndLoginAsNasabah(): daftar + auto-login nasabah
        |   |       - CRUD: addMember(), deleteMember()
        |   |       - Transaksi: addTransaction(), redeemReward()
        |   |       - EcoBankViewModelFactory (manual DI)
        |   |
        |   +-- ui/
        |       +-- model/
        |       |   +-- EcoBankData.kt
        |       |       Fungsi: Data statis untuk UI.
        |       |       - WasteType data class (8 items): nama, emoji, poin/kg,
        |       |         deskripsi, kategori
        |       |       - EcoReward data class (7 items): nama, emoji, poin,
        |       |         deskripsi, kategori
        |       |       - WASTE_TYPES & ECO_REWARDS lists
        |       |
        |       +-- navigation/
        |       |   +-- NavGraph.kt
        |       |       Fungsi: Mengatur seluruh navigasi aplikasi.
        |       |       - Sealed class Screen (12 routes)
        |       |       - NavHost dengan startDestination = Splash
        |       |       - Mengelola backstack: popUpTo untuk logout
        |       |
        |       +-- screens/
        |       |   +-- SplashScreen.kt
        |       |   |   Tampil: Logo EcoBank dengan animasi spring + tween
        |       |   |   Navigasi: Otomatis ke RoleSelection setelah 2 detik
        |       |   |
        |       |   +-- RoleSelectionScreen.kt
        |       |   |   Tampil: Dua kartu role (Pengelola / Nasabah)
        |       |   |   Aksi: Navigasi ke AdminLogin atau NasabahLogin
        |       |   |
        |       |   +-- AdminLoginScreen.kt
        |       |   |   Tampil: Form username + password dengan show/hide
        |       |   |   Validasi: Username "admin", password "ecobank123"
        |       |   |   Aksi: loginAsAdmin() + navigasi ke Home
        |       |   |
        |       |   +-- NasabahLoginScreen.kt
        |       |   |   Tampil: TabRow dengan 2 tab (Masuk / Daftar Baru)
        |       |   |   Tab Masuk: Search nasabah by nama/ID, tap untuk login
        |       |   |   Tab Daftar: Form nama+email+HP, registerAndLoginAsNasabah()
        |       |   |
        |       |   +-- HomeScreen.kt
        |       |   |   Tampil: Total nasabah, list semua nasabah (LazyColumn)
        |       |   |   Aksi: FAB tambah nasabah, tap item ke detail, logout
        |       |   |
        |       |   +-- AddMemberScreen.kt
        |       |   |   Tampil: Form nama, email, nomor HP
        |       |   |   Validasi: Field wajib diisi, email harus mengandung @
        |       |   |   Aksi: addMember() + kembali ke Home
        |       |   |
        |       |   +-- MemberDetailScreen.kt
        |       |   |   Tampil: Header profile (avatar, nama, ID, poin, level)
        |       |   |   Grid 2x2: Kartu Member, Setor Sampah, Riwayat, Reward
        |       |   |   Full-width: Katalog Sampah
        |       |   |   Role-aware: Admin dapat hapus; Nasabah dapat logout
        |       |   |
        |       |   +-- MemberCardScreen.kt
        |       |   |   Tampil: Kartu member digital bergaya kartu kredit
        |       |   |   Info: Nama, ID (NSB format), level, total poin, join date
        |       |   |
        |       |   +-- AddTransactionScreen.kt
        |       |   |   Tampil: Grid 2 kolom pilihan jenis sampah (8 item)
        |       |   |   Input: Berat sampah dalam gram
        |       |   |   Aksi: addTransaction() → poin otomatis dihitung
        |       |   |
        |       |   +-- TransactionHistoryScreen.kt
        |       |   |   Tampil: Summary (total poin, jumlah setor, total kg)
        |       |   |   List: Setiap transaksi dengan badge SETOR/REDEEM
        |       |   |
        |       |   +-- RewardScreen.kt
        |       |   |   Tampil: Grid reward per kategori (Rumah Tangga, E-Money)
        |       |   |   Aksi: Konfirmasi via AlertDialog, feedback via Snackbar
        |       |   |   Validasi: Tombol disabled jika poin tidak cukup
        |       |   |
        |       |   +-- WasteCatalogScreen.kt
        |       |       Tampil: List 8 jenis sampah dengan poin/kg dan deskripsi
        |       |       Akses: Dari HomeScreen (admin) dan MemberDetailScreen (nasabah)
        |       |
        |       +-- theme/
        |           +-- Color.kt         Palet warna: rose gold (#BF7182), cream, dll
        |           +-- Theme.kt         EcoBankTheme (Material 3, light mode only)
        |           +-- Type.kt          Konfigurasi tipografi
        |
        +-- res/
            +-- drawable/
            |   +-- ic_launcher_background.xml  Warna rose gold gradient
            |   +-- ic_launcher_foreground.xml  Simbol daur ulang + daun putih
            +-- mipmap-anydpi-v26/
            |   +-- ic_launcher.xml             Adaptive icon (Android 8.0+)
            |   +-- ic_launcher_round.xml       Adaptive icon bulat
            +-- values/
                +-- strings.xml          app_name = "EcoBank"
                +-- colors.xml
                +-- themes.xml
```

---

## 7. Pemenuhan Functional Requirements

### FR-01 — Registrasi Member

**Deskripsi PRD:** Pengguna dapat membuat akun member baru dengan input: Nama, Email, Nomor HP.

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| Form UI (Admin) | `AddMemberScreen.kt` | OutlinedTextField untuk nama, email, HP; validasi semua field wajib + format email |
| Form UI (Nasabah) | `NasabahLoginScreen.kt` | Tab "Daftar Baru" dengan form yang sama; auto-login setelah daftar |
| Business logic | `EcoBankViewModel.kt` | `addMember()` dan `registerAndLoginAsNasabah()` |
| Penyimpanan | `MemberDao.kt` | `insertMember(member): Long` |
| Repository | `EcoBankRepository.kt` | `insertMemberAndGetId()` mengembalikan ID nasabah baru |

**Acceptance Criteria:**
- [x] Semua field wajib diisi — divalidasi sebelum submit
- [x] Email harus valid — dicek dengan `.contains("@")`
- [x] Data member tersimpan — via Room + DAO
- [x] Nasabah langsung login setelah daftar — via `registerAndLoginAsNasabah()`

---

### FR-02 — Daftar Member

**Deskripsi PRD:** Menampilkan seluruh member dari Room Database, data otomatis update.

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| UI | `HomeScreen.kt` | `LazyColumn` menampilkan semua nasabah |
| State | `EcoBankViewModel.kt` | `allMembers: StateFlow<List<Member>>` |
| Query | `MemberDao.kt` | `getAllMembers(): Flow<List<Member>>` (ORDER BY id DESC) |
| Count | `MemberDao.kt` | `getTotalMembers(): Flow<Int>` untuk header dashboard |
| Reaktivitas | `EcoBankViewModel.kt` | `.stateIn(viewModelScope, SharingStarted.WhileSubscribed, emptyList())` |

**Acceptance Criteria:**
- [x] Data tampil dari Room Database — diambil via Flow dari DAO
- [x] Data otomatis update — menggunakan Kotlin Flow; UI recompose saat data berubah

---

### FR-03 — Membership Card

**Deskripsi PRD:** Menampilkan kartu member digital berisi: Nama, ID Member, Level Member, Total Poin.

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| UI Kartu | `MemberCardScreen.kt` | Kartu bergaya digital card dengan gradient rose gold |
| ID Format | `Member.kt` | Extension: `formattedId()` → "NSB00001" |
| Level | `Member.kt` | Extension: `level()` → "Pemula" / "Aktif" / "Champion" |
| Data sumber | `EcoBankViewModel.kt` | `currentMember: StateFlow<Member?>` |
| Navigasi | `MemberDetailScreen.kt` | Tombol "Kartu Nasabah" → MemberCardScreen |

**Acceptance Criteria:**
- [x] Data tampil sesuai database — kartu menampilkan data real-time dari `currentMember`
- [x] Semua informasi ada (Nama, ID, Level, Poin) — terlihat di MemberCardScreen

---

### FR-04 — Tambah Transaksi (Setor Sampah)

**Deskripsi PRD:** Mencatat transaksi pembelian, poin dihitung otomatis (1 poin = Rp10.000).

**Adaptasi (Bank Sampah):** Input adalah berat sampah per jenis, bukan nominal pembelian. Poin dihitung berdasarkan `pointsPerKg` setiap jenis sampah.

**Formula:**
```
poin = (beratGram × pointsPerKg) / 1000
```

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| UI Pilihan Sampah | `AddTransactionScreen.kt` | Grid 2 kolom dari `WASTE_TYPES` (8 item) |
| Input Berat | `AddTransactionScreen.kt` | TextField dengan validasi angka positif |
| Kalkulasi Poin | `EcoBankRepository.kt` | `addWasteTransaction()`: formula poin per jenis |
| Update Poin | `EcoBankRepository.kt` | `memberDao.updateMember(member.copy(points = member.points + pointEarned))` |
| Data Jenis Sampah | `EcoBankData.kt` | `WASTE_TYPES` list dengan `pointsPerKg` per item |

**Acceptance Criteria:**
- [x] Poin dihitung otomatis — via formula di Repository
- [x] Riwayat tersimpan — `transactionDao.insertTransaction()` dipanggil bersamaan

---

### FR-05 — Riwayat Transaksi

**Deskripsi PRD:** Menampilkan transaksi member (tanggal, nominal/berat, poin).

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| UI | `TransactionHistoryScreen.kt` | LazyColumn dengan item per transaksi |
| Summary Header | `TransactionHistoryScreen.kt` | Total poin, jumlah setor, total berat (kg) |
| Badge Type | `TransactionHistoryScreen.kt` | Badge hijau "SETOR" / badge merah "REDEEM" |
| Query | `TransactionDao.kt` | `getTransactionsByMemberId(id): Flow<List<Transaction>>` |
| State | `EcoBankViewModel.kt` | `currentTransactions: StateFlow<List<Transaction>>` via `flatMapLatest` |

**Acceptance Criteria:**
- [x] Riwayat dapat dilihat kapan saja — data diambil dari Room, persisten
- [x] Data mencakup tanggal, berat/jumlah, poin — tersimpan di kolom `date`, `amount`, `pointEarned`

---

### FR-06 — Redeem Reward

**Deskripsi PRD:** Menukarkan poin dengan hadiah. Reward: Espresso (50), Cappuccino (100), Latte (150).

**Adaptasi (Bank Sampah):** Reward berupa kebutuhan rumah tangga dan e-money (bukan kopi), sesuai konteks bank sampah.

**Implementasi:**

| Komponen | File | Detail |
|---|---|---|
| UI Daftar Reward | `RewardScreen.kt` | Grid reward per kategori dari `ECO_REWARDS` |
| Konfirmasi | `RewardScreen.kt` | `AlertDialog` sebelum tukar |
| Feedback | `RewardScreen.kt` | `SnackbarHost` sukses/gagal |
| Validasi | `EcoBankRepository.kt` | Cek `member.points >= reward.pointCost` |
| Proses Redeem | `EcoBankRepository.kt` | `redeemReward()`: insert REDEEM transaction + kurangi poin |
| Disabled State | `RewardScreen.kt` | Tombol disabled + teks "Kurang" jika poin tidak cukup |

**Acceptance Criteria:**
- [x] Poin berkurang setelah redeem — `member.points - reward.pointCost` di-update ke DB
- [x] Tidak bisa redeem jika poin kurang — validasi di Repository, tombol disabled di UI

---

## 8. Pemenuhan Non-Functional Requirements

| Requirement | Target | Implementasi | Status |
|---|---|---|---|
| Startup | < 3 detik | SplashScreen dengan delay 2 detik; Room DB dibuat lazy | DONE |
| Query Database | < 500 ms | Room dengan Kotlin Flow (async, tidak blocking UI) | DONE |
| Data persisten | Setelah app tutup | Room Database menyimpan ke SQLite lokal di device | DONE |
| UI sederhana | Material Design 3 | Jetpack Compose + Material3 components | DONE |
| MVVM Architecture | Separation of concerns | ViewModel + Repository + DAO terpisah | DONE |
| Repository Pattern | Single source of truth | EcoBankRepository sebagai satu-satunya akses data | DONE |

---

## 9. Alur Navigasi Aplikasi

### Diagram Navigasi Lengkap

```
[Splash Screen]  (2 detik, animasi logo)
        |
        v
[Role Selection Screen]
        |
        +----------------+
        |                |
        v                v
[Admin Login]     [Nasabah Login]
        |                |
        v                +-- Tab: Masuk (cari akun)
[Home Screen]     |      +-- Tab: Daftar Baru (form)
   |  |  |        |
   |  |  +- FAB   v
   |  |    [Add Member]     [Member Detail]
   |  |                          |
   |  +---> [Waste Catalog]      +---> [Member Card]
   |                             +---> [Add Transaction]
   +---> [Member Detail]         +---> [Transaction History]
              |                  +---> [Reward]
              +---> [Member Card]       +---> [Waste Catalog]
              +---> [Add Transaction]
              +---> [Transaction History]
              +---> [Reward]
              +---> [Waste Catalog]
              +---> [Logout] --> [Role Selection]
```

### Detail Routes (NavGraph.kt)

| Route | Screen | Parameter |
|---|---|---|
| `splash` | SplashScreen | - |
| `role_selection` | RoleSelectionScreen | - |
| `admin_login` | AdminLoginScreen | - |
| `nasabah_login` | NasabahLoginScreen | - |
| `home` | HomeScreen | - |
| `add_member` | AddMemberScreen | - |
| `waste_catalog` | WasteCatalogScreen | - |
| `member_detail/{memberId}` | MemberDetailScreen | memberId: Int |
| `member_card/{memberId}` | MemberCardScreen | memberId: Int |
| `add_transaction/{memberId}` | AddTransactionScreen | memberId: Int |
| `transaction_history/{memberId}` | TransactionHistoryScreen | memberId: Int |
| `reward/{memberId}` | RewardScreen | memberId: Int |

### Manajemen Backstack
- **Login admin** → `popUpTo(RoleSelection)` agar back dari Home tidak kembali ke Login
- **Login nasabah** → `popUpTo(NasabahLogin) { inclusive = true }` sehingga back dari Detail ke RoleSelection
- **Logout** → `navController.popBackStack(Screen.RoleSelection.route, false)` kembali ke pemilihan role

---

## 10. Deskripsi Setiap Screen

### 1. Splash Screen
- **Fungsi:** Menampilkan identitas aplikasi saat pertama dibuka
- **Animasi:** Scale + alpha animation menggunakan `spring` dan `tween`
- **Transisi:** Otomatis ke RoleSelection setelah 2 detik

### 2. Role Selection Screen
- **Fungsi:** User memilih akan masuk sebagai Pengelola atau Nasabah
- **UI:** Dua kartu dengan ikon, deskripsi peran, dan animasi fade-in
- **Tujuan:** Memisahkan akses fitur berdasarkan peran pengguna

### 3. Admin Login Screen
- **Fungsi:** Autentikasi pengelola bank sampah
- **UI:** Field username + password dengan toggle show/hide
- **Keamanan:** Kredensial hardcoded, tidak ditampilkan di UI (hint card dihapus)
- **Validasi:** Pesan error inline jika salah username/password

### 4. Nasabah Login Screen
- **Fungsi:** Login atau registrasi nasabah baru
- **Tab Masuk:** Search bar untuk cari nasabah by nama/ID → tampil list → tap untuk login
- **Tab Daftar:** Form nama, email, HP → `registerAndLoginAsNasabah()` → auto-login
- **Edge case:** Jika belum ada nasabah, tampil pesan kosong

### 5. Home Screen (Dashboard Admin)
- **Fungsi:** Overview semua nasabah bank sampah
- **UI:** Card statistik total nasabah + LazyColumn daftar nasabah
- **Aksi:** FAB tambah nasabah, tap nasabah ke Detail, icon logout

### 6. Add Member Screen
- **Fungsi:** Formulir pendaftaran nasabah baru oleh admin
- **Input:** Nama, email, nomor HP
- **Validasi:** Semua field wajib, email format, nomor HP minimal 9 digit

### 7. Member Detail Screen
- **Fungsi:** Halaman utama profil nasabah dengan semua menu aksi
- **Header:** Avatar inisial, nama, ID (NSB format), poin, level dengan emoji
- **Menu:** Grid 2x2 (Kartu, Setor, Riwayat, Reward) + full-width Katalog Sampah
- **Role-aware:** Admin: tombol hapus; Nasabah: tombol logout

### 8. Member Card Screen
- **Fungsi:** Kartu membership digital bergaya kartu perbankan
- **Info:** Nama nasabah, ID (NSBxxxxx), level, total poin, tanggal bergabung
- **Desain:** Gradient rose gold, chip "MEMBER"

### 9. Add Transaction Screen
- **Fungsi:** Mencatat setoran sampah nasabah
- **UI:** Grid pilihan jenis sampah (8 item dengan emoji + nilai poin)
- **Input:** Berat sampah dalam gram
- **Preview:** Estimasi poin yang akan didapat ditampilkan sebelum konfirmasi

### 10. Transaction History Screen
- **Fungsi:** Riwayat lengkap semua transaksi nasabah
- **Header:** Ringkasan (total poin, jumlah setor, total berat kg)
- **List:** Badge hijau SETOR / merah REDEEM, nama item, poin, tanggal

### 11. Reward Screen
- **Fungsi:** Penukaran poin dengan reward
- **Kategori:** Rumah Tangga (Sabun, Deterjen, Minyak, Beras) + E-Money
- **UX:** AlertDialog konfirmasi, Snackbar feedback, tombol disabled jika poin kurang

### 12. Waste Catalog Screen
- **Fungsi:** Referensi jenis sampah yang diterima dan nilai poinnya
- **Akses:** Dari HomeScreen (admin) DAN MemberDetailScreen (nasabah)
- **Info per item:** Emoji, nama, nilai poin/kg, deskripsi

---

## 11. Fitur Tambahan (Beyond PRD)

Berikut fitur yang ditambahkan melebihi requirement dasar PRD:

| Fitur | Deskripsi |
|---|---|
| Role-Based Access | Pemisahan akses Pengelola vs Nasabah dengan auth masing-masing |
| Nasabah Self-Register | Nasabah dapat mendaftar sendiri tanpa melalui admin |
| Sistem Level | Level otomatis naik (Pemula/Aktif/Champion) berdasarkan total poin |
| Katalog Sampah | 8 jenis sampah dengan nilai poin berbeda-beda |
| Reward Beragam | 7 jenis reward (kebutuhan RT + e-money) vs 3 reward di PRD |
| Custom App Icon | Icon aplikasi bertema EcoBank (simbol daur ulang + daun, rose gold) |
| Adaptive Icon | Mendukung adaptive icon Android 8.0+ |

---

## 12. Definition of Done (DoD) Checklist

| # | Kriteria | Status | Bukti Implementasi |
|---|---|---|---|
| 1 | Registrasi member berjalan | DONE | `AddMemberScreen.kt`, `NasabahLoginScreen.kt` (tab Daftar) |
| 2 | Data tersimpan di Room Database | DONE | `AppDatabase.kt`, `MemberDao.kt`, `TransactionDao.kt` |
| 3 | Membership card tampil | DONE | `MemberCardScreen.kt` |
| 4 | Poin dihitung otomatis | DONE | `EcoBankRepository.addWasteTransaction()` |
| 5 | Riwayat transaksi tampil | DONE | `TransactionHistoryScreen.kt` |
| 6 | Reward dapat ditukar | DONE | `RewardScreen.kt`, `EcoBankRepository.redeemReward()` |
| 7 | Navigasi antar halaman berfungsi | DONE | `NavGraph.kt` dengan 12 routes |
| 8 | Tidak terdapat error saat pengujian | DONE | Diuji pada emulator API 34 |

---

## 13. Deliverables

| # | Deliverable | Status |
|---|---|---|
| 1 | Source Code Kotlin | DONE — 12 screen + data layer + ViewModel |
| 2 | Room Database Implementation | DONE — AppDatabase v2, 2 tabel, 2 DAO |
| 3 | Jetpack Compose UI | DONE — Seluruh UI menggunakan Compose + Material 3 |
| 4 | MVVM Architecture | DONE — ViewModel + Repository + DAO terstruktur |
| 5 | APK File | DONE — Dapat di-build dari Android Studio |
| 6 | User Manual | DONE — README.md |
| 7 | Laporan Project | DONE — File ini (LAPORAN.md) |

---

## 14. Kesimpulan

Aplikasi **EcoBank** berhasil dibangun sebagai solusi digital untuk pengelolaan bank sampah, mengimplementasikan seluruh functional requirement yang tercantum dalam PRD dengan adaptasi konteks dari coffee shop membership ke bank sampah. Semua kriteria Definition of Done telah terpenuhi.

Aplikasi menggunakan arsitektur MVVM yang bersih dengan pemisahan jelas antara layer presentasi (Compose UI), layer logika (ViewModel), layer akses data (Repository + DAO), dan layer penyimpanan (Room Database). Reaktivitas data dijamin melalui penggunaan Kotlin Flow dan StateFlow sehingga UI selalu sinkron dengan data tanpa perlu refresh manual.

Pengembangan ke depan (Version 2.0) dapat mencakup Firebase Authentication, Cloud Sync, QR Code untuk kartu member, dan fitur analytics dashboard.
