package com.example.winly.api

import com.google.gson.annotations.SerializedName

// ========== 1. MANAJEMEN AKUN ==========

data class LoginResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("otp") val otp: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("is_bookmarked") val isBookmarked: Boolean? = null,
    @SerializedName("data") val data: UserData? = null
)

data class UserData(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("sisa_kuota") val sisaKuota: Int? = 0,
    @SerializedName("instansi") val instansi: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null
)

data class UserResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val data: UserData? = null,
    @SerializedName("message") val message: String? = null
)

data class UserInfoResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val data: UserProfile? = null,
    @SerializedName("message") val message: String? = null
)

data class UserProfile(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("sisa_kuota") val sisaKuota: Int? = 0,
    @SerializedName("instansi") val instansi: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// ========== 2. MANAJEMEN LOMBA ==========

data class CompetitionResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<Competition>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

typealias CompetitionModel = Competition
typealias CompetitionDetailModel = Competition

data class Competition(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("judul_lomba") val judulLomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_pendidikan") val tingkatPendidikan: String? = null,
    @SerializedName("tingkat_lomba") val tingkatLomba: String? = null,
    @SerializedName("deskripsi") val deskripsi: String? = null,
    @SerializedName("link_pendaftaran") val linkPendaftaran: String? = null,
    @SerializedName("poster_url") val posterUrl: String? = null,
    @SerializedName("biaya_pendaftaran") val biayaPendaftaran: Int? = 0,
    @SerializedName("tanggal_buka_daftar") val tanggalBukaDaftar: String? = null,
    @SerializedName("tanggal_tutup_daftar") val tanggalTutupDaftar: String? = null,
    @SerializedName("tanggal_pelaksanaan") val tanggalPelaksanaan: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("is_verified") val isVerified: Boolean? = false,
    @SerializedName("nama_penyelenggara") val namaPenyelenggara: String? = null,
    @SerializedName("instansi_penyelenggara") val instansiPenyelenggara: String? = null,
    @SerializedName("email_penyelenggara") val emailPenyelenggara: String? = null,
    @SerializedName("is_bookmarked") val isBookmarked: Boolean? = false
)

data class CompetitionDetailResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val data: Competition? = null,
    @SerializedName("message") val message: String? = null
)

// ========== 3. BOOKMARK & REMINDER ==========

data class BookmarkResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<BookmarkedCompetition>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

data class BookmarkedCompetition(
    @SerializedName("bookmark_id") val bookmarkId: Int? = null,
    @SerializedName("bookmarked_at") val bookmarkedAt: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("judul_lomba") val judulLomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_pendidikan") val tingkatPendidikan: String? = null,
    @SerializedName("tingkat_lomba") val tingkatLomba: String? = null,
    @SerializedName("biaya_pendaftaran") val biayaPendaftaran: Int? = 0,
    @SerializedName("tanggal_tutup_daftar") val tanggalTutupDaftar: String? = null,
    @SerializedName("tanggal_pelaksanaan") val tanggalPelaksanaan: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("nama_penyelenggara") val namaPenyelenggara: String? = null
)

// ========== 5. MANAJEMEN SERTIFIKAT ==========

data class CertificateResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<Certificate>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

data class Certificate(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nama_pemenang") val namaPemenang: String? = null,
    @SerializedName("predikat") val predikat: String? = null,
    @SerializedName("file_sertifikat") val fileSertifikat: String? = null,
    @SerializedName("nama_penyelenggara") val namaPenyelenggara: String? = null,
    @SerializedName("tahun") val tahun: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("judul_lomba") val judulLomba: String? = null,
    @SerializedName("kategori") val kategori: String? = null,
    @SerializedName("tingkat_lomba") val tingkatLomba: String? = null
)

// ========== REGISTRATIONS ==========

data class RegistrationResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("total") val total: Int? = 0,
    @SerializedName("data") val data: List<Registration>? = emptyList(),
    @SerializedName("message") val message: String? = null
)

typealias RegistrationModel = Registration

data class Registration(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("peserta_id") val pesertaId: Int? = null,
    @SerializedName("competition_id") val competitionId: Int? = null,
    @SerializedName("status_pendaftaran") val statusPendaftaran: String? = null,
    @SerializedName("bukti_follow") val buktiFollow: String? = null,
    @SerializedName("bukti_share") val buktiShare: String? = null,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("nama_peserta") val namaPeserta: String? = null,
    @SerializedName("email_peserta") val emailPeserta: String? = null,
    @SerializedName("instansi_peserta") val instansiPeserta: String? = null
)
