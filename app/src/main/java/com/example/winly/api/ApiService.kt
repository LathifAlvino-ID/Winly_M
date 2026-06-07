package com.example.winly.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ===== AUTH =====
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("instansi") instansi: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("verify.php")
    fun verifyOtp(
        @Field("email") email: String,
        @Field("code") code: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")
    fun sendOtp(
        @Field("action") action: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")
    fun resetPassword(
        @Field("action") action: String,
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("new_password") newPassword: String
    ): Call<LoginResponse>

    // ===== COMPETITIONS =====
    @GET("get_competitions.php")
    fun getCompetitions(
        @Query("search") search: String? = null,
        @Query("kategori") kategori: String? = null,
        @Query("tingkat_pendidikan") tingkatPendidikan: String? = null,
        @Query("tingkat_lomba") tingkatLomba: String? = null
    ): Call<CompetitionResponse>

    @GET("get_competition_detail.php")
    fun getCompetitionDetail(
        @Query("id") id: Int
    ): Call<CompetitionDetailResponse>

    @FormUrlEncoded
    @POST("create_competition.php")
    fun createCompetition(
        @Field("penyelenggara_id") penyelenggara_id: Int,
        @Field("judul_lomba") judul_lomba: String,
        @Field("kategori") kategori: String,
        @Field("tingkat_pendidikan") tingkat_pendidikan: String,
        @Field("tingkat_lomba") tingkat_lomba: String,
        @Field("deskripsi") deskripsi: String,
        @Field("link_pendaftaran") link_pendaftaran: String,
        @Field("link_panduan") link_panduan: String,
        @Field("poster_url") poster_url: String,
        @Field("biaya_pendaftaran") biaya_pendaftaran: Int,
        @Field("tanggal_tutup_daftar") tanggal_tutup_daftar: String,
        @Field("tanggal_pelaksanaan") tanggal_pelaksanaan: String
    ): Call<LoginResponse>

    @GET("get_my_competitions.php")
    fun getMyCompetitions(
        @Query("penyelenggara_id") penyelenggaraId: Int
    ): Call<CompetitionResponse>

    @FormUrlEncoded
    @POST("delete_competition.php")
    fun deleteCompetition(
        @Field("competition_id") competitionId: Int,
        @Field("penyelenggara_id") penyelenggaraId: Int
    ): Call<LoginResponse>

    // ===== BOOKMARKS =====
    @GET("bookmark.php")
    fun getBookmarks(
        @Query("user_id") userId: Int
    ): Call<BookmarkResponse>

    @FormUrlEncoded
    @POST("bookmark.php")
    fun toggleBookmark(
        @Field("user_id") userId: Int,
        @Field("competition_id") competitionId: Int
    ): Call<LoginResponse>

    // ===== REGISTRATIONS =====
    @GET("get_registrations.php")
    fun getRegistrations(
        @Query("competition_id") competitionId: Int
    ): Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("update_registration.php")
    fun updateRegistration(
        @Field("registration_id") registrationId: Int,
        @Field("status") status: String,
        @Field("catatan") catatan: String
    ): Call<LoginResponse>

    // ===== CERTIFICATES (BARU) =====
    @GET("get_certificates.php")
    fun getCertificates(
        @Query("user_id") userId: Int
    ): Call<CertificateResponse>

    // ===== USER =====
    @GET("get_user_info.php")
    fun getUserInfo(
        @Query("user_id") userId: Int
    ): Call<UserInfoResponse>
}

// ===== RESPONSE DATA CLASSES =====
data class LoginResponse(
    val status: String,
    val message: String = "",
    val otp: String? = null,
    val email: String? = null,
    val data: UserData? = null
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val sisa_kuota: Int,
    val instansi: String?,
    val phone: String?,
    val avatar_url: String?
)

data class CompetitionResponse(
    val status: String,
    val total: Int = 0,
    val data: List<Competition> = emptyList(),
    val message: String = ""
)

data class Competition(
    val id: Int,
    val judul_lomba: String,
    val kategori: String,
    val tingkat_pendidikan: String,
    val tingkat_lomba: String,
    val deskripsi: String,
    val link_pendaftaran: String?,
    val poster_url: String?,
    val biaya_pendaftaran: Int,
    val tanggal_buka_daftar: String?,
    val tanggal_tutup_daftar: String,
    val tanggal_pelaksanaan: String,
    val status: String,
    val is_verified: Boolean,
    val nama_penyelenggara: String,
    val instansi_penyelenggara: String?
)

data class CompetitionDetailResponse(
    val status: String,
    val data: Competition? = null,
    val message: String = ""
)

data class BookmarkResponse(
    val status: String,
    val total: Int = 0,
    val data: List<BookmarkedCompetition> = emptyList()
)

data class BookmarkedCompetition(
    val bookmark_id: Int,
    val bookmarked_at: String,
    val id: Int,
    val judul_lomba: String,
    val kategori: String,
    val tingkat_pendidikan: String,
    val tingkat_lomba: String,
    val biaya_pendaftaran: Int,
    val tanggal_tutup_daftar: String,
    val tanggal_pelaksanaan: String,
    val status: String,
    val nama_penyelenggara: String
)

data class RegistrationResponse(
    val status: String,
    val total: Int = 0,
    val data: List<Registration> = emptyList()
)

data class Registration(
    val id: Int,
    val peserta_id: Int,
    val competition_id: Int,
    val status_pendaftaran: String,
    val bukti_follow: String?,
    val bukti_share: String?,
    val catatan: String?,
    val created_at: String,
    val nama_peserta: String,
    val email_peserta: String,
    val instansi_peserta: String?
)

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

data class UserInfoResponse(
    val status: String,
    val data: UserProfile? = null,
    val message: String = ""
)

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val sisa_kuota: Int,
    val instansi: String,
    val phone: String,
    val avatar_url: String,
    val created_at: String
)