package com.example.winly.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // 1. PINTU LOGIN
    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // 2. PINTU REGISTRASI
    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<LoginResponse>

    // 3. PINTU VERIFIKASI OTP
    @FormUrlEncoded
    @POST("verify.php")
    fun verifyUser(
        @Field("email") email: String,
        @Field("code") code: String
    ): Call<LoginResponse>

    // 4. PINTU AMBIL DATA LOMBA (UNTUK BERANDA PESERTA)
    @GET("get_competitions.php")
    fun getCompetitions(): Call<CompetitionResponse>

    // 5. PINTU UPLOAD LOMBA BARU (UNTUK PENYELENGGARA)
    @FormUrlEncoded
    @POST("create_competition.php")
    fun createCompetition(
        @Field("penyelenggara_id") penyelenggaraId: Int,
        @Field("judul_lomba") judul: String,
        @Field("kategori") kategori: String,
        @Field("tingkat_pendidikan") tingkatPendidikan: String,
        @Field("tingkat_lomba") tingkatLomba: String,
        @Field("deskripsi") deskripsi: String,
        @Field("link_pendaftaran") linkPendaftaran: String,
        @Field("link_panduan") linkPanduan: String,
        @Field("poster_url") posterUrl: String,
        @Field("biaya_pendaftaran") biaya: Int,
        @Field("tanggal_tutup_daftar") tanggalTutupDaftar: String,
        @Field("tanggal_pelaksanaan") tanggal: String
    ): Call<LoginResponse>

    // 6. PINTU AMBIL INFO USER (CEK SISA KUOTA)
    @GET("get_user_info.php")
    fun getUserInfo(
        @Query("user_id") userId: Int
    ): Call<UserResponse>

    // 7. PINTU AMBIL LOMBA KHUSUS PENYELENGGARA TERTENTU
    @GET("get_my_competitions.php")
    fun getMyCompetitions(
        @Query("penyelenggara_id") id: Int
    ): Call<CompetitionResponse>

    // 8. PINTU AMBIL BOOKMARKS (BARU)
    @GET("bookmark.php")
    fun getBookmarks(
        @Query("user_id") userId: Int
    ): Call<BookmarkResponse>

    // 9. PINTU TOGGLE BOOKMARK (BARU)
    @FormUrlEncoded
    @POST("bookmark.php")
    fun toggleBookmark(
        @Field("user_id") userId: Int,
        @Field("competition_id") competitionId: Int
    ): Call<LoginResponse>

    // 10. PINTU AMBIL CERTIFICATES (BARU)
    @GET("get_certificates.php")
    fun getCertificates(
        @Query("user_id") userId: Int
    ): Call<CertificateResponse>
}