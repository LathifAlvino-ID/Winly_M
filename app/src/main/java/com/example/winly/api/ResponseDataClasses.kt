package com.example.winly.api

import com.google.gson.annotations.SerializedName

// USER & KUOTA
data class UserResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("sisa_kuota")
    val sisaKuota: Int? = 0,
    @SerializedName("message")
    val message: String? = null
)

// BOOKMARK
data class BookmarkResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("total")
    val total: Int? = 0,
    @SerializedName("data")
    val data: List<BookmarkedCompetition>? = emptyList()
)

data class BookmarkedCompetition(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("judul_lomba")
    val judul_lomba: String? = null,
    @SerializedName("kategori")
    val kategori: String? = null,
    @SerializedName("tingkat_pendidikan")
    val tingkat_pendidikan: String? = null,
    @SerializedName("tingkat_lomba")
    val tingkat_lomba: String? = null,
    @SerializedName("biaya_pendaftaran")
    val biaya_pendaftaran: Int? = 0,
    @SerializedName("tanggal_tutup_daftar")
    val tanggal_tutup_daftar: String? = null,
    @SerializedName("tanggal_pelaksanaan")
    val tanggal_pelaksanaan: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("nama_penyelenggara")
    val nama_penyelenggara: String? = null
)

// CERTIFICATE & PORTFOLIO
data class CertificateResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("total")
    val total: Int? = 0,
    @SerializedName("data")
    val data: List<Certificate>? = emptyList(),
    @SerializedName("message")
    val message: String? = null
)

data class Certificate(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("nama_pemenang")
    val nama_pemenang: String? = null,
    @SerializedName("predikat")
    val predikat: String? = null,
    @SerializedName("file_sertifikat")
    val file_sertifikat: String? = null,
    @SerializedName("nama_penyelenggara")
    val nama_penyelenggara: String? = null,
    @SerializedName("tahun")
    val tahun: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("judul_lomba")
    val judul_lomba: String? = null,
    @SerializedName("kategori")
    val kategori: String? = null,
    @SerializedName("tingkat_lomba")
    val tingkat_lomba: String? = null
)