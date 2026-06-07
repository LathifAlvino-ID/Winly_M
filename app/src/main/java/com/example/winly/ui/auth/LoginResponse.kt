package com.example.winly.ui.auth

class LoginResponse {
    // TAMBAH KE FILE YANG SUDAH ADA atau BUAT FILE BARU: CertificateResponse.kt

    data class CertificateResponse(
        val status: String,
        val total: Int = 0,
        val data: List<Certificate> = emptyList(),
        val message: String = ""
    )

    data class Certificate(
        val id: Int,
        val nama_pemenang: String,
        val predikat: String,
        val file_sertifikat: String,
        val nama_penyelenggara: String,
        val tahun: String,
        val created_at: String,
        val judul_lomba: String,
        val kategori: String,
        val tingkat_lomba: String
    )
}