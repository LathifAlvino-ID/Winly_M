package com.example.winly.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Favorite
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
import com.example.winly.api.BookmarkedCompetition
import com.example.winly.api.BookmarkResponse
import com.example.winly.api.LoginResponse
import com.example.winly.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BookmarkScreen(userId: Int? = null) {
    val context = LocalContext.current

    val actualUserId = userId ?: run {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.getInt("user_id", 0)
    }

    var bookmarks by remember { mutableStateOf<List<BookmarkedCompetition>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var refreshKey by remember { mutableStateOf(0) }

    LaunchedEffect(refreshKey) {
        if (actualUserId <= 0) {
            errorMessage = "User ID tidak valid"
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        errorMessage = ""

        RetrofitClient.instance.getBookmarks(actualUserId)
            .enqueue(object : Callback<BookmarkResponse> {
                override fun onResponse(
                    call: Call<BookmarkResponse>,
                    response: Response<BookmarkResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful) {
                        bookmarks = response.body()?.data ?: emptyList()
                    } else {
                        errorMessage = "Gagal memuat bookmark"
                    }
                }

                override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                    isLoading = false
                    errorMessage = "Error: ${t.message}"
                }
            })
    }

    fun hapusBookmark(competitionId: Int) {
        RetrofitClient.instance.toggleBookmark(actualUserId, competitionId)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "✓ Bookmark dihapus", Toast.LENGTH_SHORT).show()
                        refreshKey++
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Bookmark Saya",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0061D1))
            }
            return@Column
        }

        if (errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage, color = Color.Gray, fontSize = 14.sp)
            }
            return@Column
        }

        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Belum Ada Bookmark",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Simpan lomba favorit untuk akses cepat 📌",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
            return@Column
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(bookmarks) { bookmark ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
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
                                    bookmark.judulLomba ?: "N/A",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0061D1)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    bookmark.namaPenyelenggara ?: "N/A",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }

                            IconButton(
                                onClick = { hapusBookmark(bookmark.id ?: 0) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.BookmarkRemove,
                                    contentDescription = "Hapus bookmark",
                                    tint = Color(0xFFE91E63),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = Color(0xFFE5F0FF),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    bookmark.kategori ?: "N/A",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0061D1),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                color = Color(0xFFFFF4E0),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    bookmark.tingkatLomba ?: "N/A",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFF9800),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Tutup Daftar", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    bookmark.tanggalTutupDaftar ?: "N/A",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.DarkGray
                                )
                            }

                            Text(
                                if (bookmark.biayaPendaftaran == 0) "Gratis" else "Rp ${String.format("%,d", bookmark.biayaPendaftaran ?: 0).replace(",", ".")}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (bookmark.biayaPendaftaran == 0) Color(0xFF4CAF50) else Color(0xFF0061D1)
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}