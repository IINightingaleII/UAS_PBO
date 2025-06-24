# Sistem Informasi Manajemen Rumah Sakit (SistemRS)

## Deskripsi

**SistemRS** adalah aplikasi desktop yang dirancang untuk mengelola data fundamental dalam sebuah institusi kesehatan skala kecil. Aplikasi ini dibangun menggunakan bahasa pemrograman Java dengan antarmuka pengguna (GUI) yang dikembangkan menggunakan JavaFX. Sistem ini memfasilitasi manajemen data pasien, data dokter, serta pencatatan rekam medis yang menghubungkan kedua entitas tersebut.

Aplikasi ini menerapkan pola desain yang terinspirasi dari Model-View-Controller (MVC) untuk memastikan kode yang terstruktur, mudah dirawat, dan dapat dikembangkan lebih lanjut.

## Fitur Utama

  * **Autentikasi Pengguna**: Sistem dilengkapi dengan fungsionalitas login untuk membatasi akses.
  * **Dashboard Utama**: Halaman utama yang berfungsi sebagai pusat navigasi untuk mengakses berbagai modul.
  * **Manajemen Data Pasien**: Fitur CRUD (Create, Read, Update, Delete) yang lengkap untuk mengelola data pasien.
  * **Manajemen Data Dokter**: Fitur CRUD yang lengkap untuk mengelola data dokter dan spesialisasinya.
  * **Manajemen Rekam Medis**: Fitur CRUD untuk mencatat riwayat medis pasien, termasuk diagnosa, tindakan, serta dokter yang menangani.

## Teknologi yang Digunakan

  * **Bahasa**: Java (JDK 11 atau lebih tinggi)
  * **Framework UI**: JavaFX
  * **Database**: MySQL atau MariaDB
  * **Konektivitas Database**: JDBC (Java Database Connectivity)
  * **Desain Antarmuka**: FXML
  * **IDE**: Dikembangkan dan diuji menggunakan Intelliij

## Prasyarat (Requirements)

Sebelum menjalankan proyek ini, pastikan sistem Anda telah terpasang perangkat lunak berikut:

1.  **Java Development Kit (JDK)**: Versi 11 atau yang lebih baru.
2.  **JavaFX SDK**: Versi yang sesuai dengan JDK Anda. Sejak JDK 11, JavaFX tidak lagi menjadi bagian dari JDK dan harus diunduh secara terpisah.
3.  **Database Server**: MySQL atau MariaDB Server.
4.  **IDE (Opsional, tapi direkomendasikan)**: Apache NetBeans atau Intellij untuk kemudahan konfigurasi dan menjalankan proyek.

## Instalasi dan Konfigurasi

Ikuti langkah-langkah berikut untuk menjalankan proyek ini secara lokal:

1.  **Clone Repositori**

    ```bash
    git clone [URL_REPOSITORI_ANDA]
    cd [NAMA_FOLDER_PROYEK]
    ```

2.  **Konfigurasi Database**

      * Jalankan server MySQL/MariaDB Anda.
      * Buka alat manajemen database Anda (misalnya, phpMyAdmin, DBeaver, atau command line).
      * Buat sebuah database baru dengan nama `rs_db`.
      * Impor file `rs_db.sql` yang ada di dalam repositori ini ke dalam database `rs_db` yang baru saja Anda buat. Ini akan membuat semua tabel yang diperlukan beserta data awalnya.

3.  **Konfigurasi Proyek di IDE**

      * Buka proyek ini menggunakan IDE NetBeans.
      * Pastikan library JavaFX SDK telah ditambahkan ke dalam *project libraries*.
      * Buka file `src/service/DatabaseService.java`.
      * Sesuaikan konfigurasi koneksi database jika diperlukan, terutama pada bagian `DB_USER` dan `DB_PASS` agar sesuai dengan pengaturan server database lokal Anda.
        ```java
        private static final String DB_URL = "jdbc:mysql://localhost:3306/rs_db";
        private static final String DB_USER = "root";
        private static final String DB_PASS = ""; // Sesuaikan jika root Anda memiliki password
        ```

4.  **Jalankan Aplikasi**

      * Build dan Run proyek dari IDE NetBeans (biasanya dengan menekan `F6` atau melalui menu `Run > Run Project`).

## Cara Menggunakan Aplikasi

1.  **Login**: Saat aplikasi pertama kali dijalankan, Anda akan disambut oleh halaman login. Gunakan kredensial default yang telah tersedia di database:

      * **Username**: `admin`
      * **Password**: `1234`

2.  **Dashboard**: Setelah berhasil login, Anda akan diarahkan ke dashboard utama. Dashboard ini berisi menu navigasi untuk mengakses fitur-fitur utama.

3.  **Manajemen Data**:

      * Klik tombol **"Data Pasien"**, **"Data Dokter"**, atau **"Rekam Medis"** untuk membuka jendela manajemen data yang sesuai.
      * Di setiap jendela manajemen, Anda dapat melakukan operasi berikut:
          * **Menambah Data**: Isi semua field pada form input, lalu klik tombol **"Tambah"**.
          * **Mengubah Data**: Klik salah satu baris data pada tabel. Data akan otomatis muncul di form input. Ubah data sesuai kebutuhan, lalu klik tombol **"Update"** atau **"Edit"**.
          * **Menghapus Data**: Klik salah satu baris data pada tabel, lalu klik tombol **"Hapus"**. Akan muncul dialog konfirmasi sebelum data dihapus secara permanen.
          * **Membersihkan Form**: Klik tombol **"Clear"** untuk mengosongkan semua field pada form input.

4.  **Logout**: Klik tombol **"Logout"** di dashboard untuk keluar dari sesi Anda dan kembali ke halaman login.
