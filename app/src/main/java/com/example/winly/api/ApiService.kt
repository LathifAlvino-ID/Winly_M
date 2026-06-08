package com.example.winly.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ===== AUTH =====
    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("instansi") instansi: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("verify.php")
    fun verifyUser(
        @Field("email") email: String,
        @Field("code") code: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")
    fun sendForgotOtp(
        @Field("email") email: String,
        @Field("action") action: String = "send_otp"
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("forgot_password.php")
    fun resetPassword(
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("new_password") new_password: String,
        @Field("action") action: String = "reset_password"
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
