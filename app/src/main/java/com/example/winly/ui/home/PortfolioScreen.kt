package com.example.winly.ui.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winly.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ====================================================================
// DATA CLASS
// ====================================================================
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

data class CertificateResponse(
    val status: String,
    val total: Int,
    val data: List<Certificate> = emptyList(),
    val message: String = ""
)

// ====================================================================
// MAIN SCREEN
// ====================================================================
@Composable
fun PortfolioScreen(userId: Int? = null) {
    val context = LocalContext.current

    // Ambil user ID dari SharedPref jika tidak diberikan
    val actualUserId = userId ?: run {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.getInt("user_id", 0)
    }

    var certificates by remember { mutableStateOf<List<Certificate>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // ===== LOAD DATA SAAT SCREEN PERTAMA KALI DIBUKA =====
    LaunchedEffect(Unit) {
        if (actualUserId <= 0) {
            errorMessage = "User ID tidak valid"
            isLoading = false
            return@LaunchedEffect
        }

        RetrofitClient.instance.getCertificates(actualUserId)
            .enqueue(object : Callback<CertificateResponse> {
                override fun onResponse(
                    call: Call<CertificateResponse>,
                    response: Response<CertificateResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful) {
                        certificates = response.body()?.data ?: emptyList()
                    } else {
                        errorMessage = "Gagal memuat sertifikat"
                    }
                }

                override fun onFailure(call: Call<CertificateResponse>, t: Throwable) {
                    isLoading = false
                    errorMessage = "Error: ${t.message}"
                }
            })
    }

    // ===== UI =====
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color(0xFF0061D1),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Portofolio Saya",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== LOADING STATE =====
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0061D1))
            }
            return@Column
        }

        // ===== ERROR STATE =====
        if (errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        errorMessage,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            return@Column
        }

        // ===== EMPTY STATE =====
        if (certificates.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Belum Ada Sertifikat",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Ikuti perlombaan dan menangkan hadiah untuk mendapatkan sertifikat! 🎉",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
            return@Column
        }

        // ===== LIST SERTIFIKAT =====
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(certificates) { cert ->
                CertificateCard(cert = cert)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ====================================================================
// CERTIFICATE CARD COMPONENT
// ====================================================================
@Composable
fun CertificateCard(cert: Certificate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ===== HEADER: JUDUL & LEVEL =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        cert.judul_lomba,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0061D1)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${cert.tingkat_lomba} • ${cert.kategori}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                // Badge Predikat
                Surface(
                    color = when (cert.predikat.lowercase()) {
                        "juara 1" -> Color(0xFFFFD700)
                        "juara 2" -> Color(0xFFC0C0C0)
                        "juara 3" -> Color(0xFFCD7F32)
                        else -> Color(0xFFE0E0E0)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        cert.predikat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== INFO: PENYELENGGARA & TAHUN =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Penyelenggara", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        cert.nama_penyelenggara,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
                Column {
                    Text("Tahun", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        cert.tahun,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== DIVIDER =====
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // ===== FOOTER: DOWNLOAD BUTTON =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Atas Nama: ${cert.nama_pemenang}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = { /* TODO: Download sertifikat */ },
                    modifier = Modifier
                        .height(36.dp)
                        .wrapContentWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0061D1)
                    )
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Download",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}