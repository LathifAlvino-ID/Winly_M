package com.example.winly.api

import com.google.gson.annotations.SerializedName

// ========== LOGIN & REGISTER ==========
data class LoginResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("otp") val otp: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("data") val data: UserData? = null
)

data class UserData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("sisa_kuota") val sisaKuota: Int? = 0
)

// ========== USER INFO ==========
data class UserResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("sisa_kuota") val sisaKuota: Int? = 0,
    @SerializedName("message") val message: String? = null
)

// ========== COMPETITIONS ==========
data class CompetitionResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<Competition>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

data class Competition(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("judul_lomba") val judul_lomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_pendidikan") val tingkat_pendidikan: String? = null,
    @SerializedName("tingkat_lomba") val tingkat_lomba: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null,
    @SerializedName("link_pendaftaran") val link_pendaftaran: String? = null,
    @SerializedName("poster_url") val poster_url: String? = null,
    @SerializedName("biaya_pendaftaran") val biaya_pendaftaran: Int? = 0,
    @SerializedName("tanggal_buka_daftar") val tanggal_buka_daftar: String? = null,
    @SerializedName("tanggal_tutup_daftar") val tanggal_tutup_daftar: String? = null,
    @SerializedName("tanggal_pelaksanaan") val tanggal_pelaksanaan: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("is_verified") val is_verified: Boolean? = false,
    @SerializedName("nama_penyelenggara") val nama_penyelenggara: String? = null
)

// ========== BOOKMARKS ==========
data class BookmarkResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<BookmarkedCompetition>? = emptyList()
)

data class BookmarkedCompetition(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("judul_lomba") val judul_lomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_pendidikan") val tingkat_pendidikan: String? = null,
    @SerializedName("tingkat_lomba") val tingkat_lomba: String? = null,
    @SerializedName("biaya_pendaftaran") val biaya_pendaftaran: Int? = 0,
    @SerializedName("tanggal_tutup_daftar") val tanggal_tutup_daftar: String? = null,
    @SerializedName("tanggal_pelaksanaan") val tanggal_pelaksanaan: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("nama_penyelenggara") val nama_penyelenggara: String? = null,
    @SerializedName("bookmark_id") val bookmark_id: Int? = null,
    @SerializedName("bookmarked_at") val bookmarked_at: String? = null
)

// ========== CERTIFICATES ==========
data class CertificateResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<Certificate>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

data class Certificate(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nama_pemenang") val nama_pemenang: String? = null,
    @SerializedName("predikat") val predikat: String? = null,
    @SerializedName("file_sertifikat") val file_sertifikat: String? = null,
    @SerializedName("nama_penyelenggara") val nama_penyelenggara: String? = null,
    @SerializedName("tahun") val tahun: String? = null,
    @SerializedName("created_at") val created_at: String? = null,
    @SerializedName("judul_lomba") val judul_lomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_lomba") val tingkat_lomba: String? = null
)