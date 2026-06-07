package com.example.winly.ui.home

import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winly.api.LoginResponse
import com.example.winly.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateCompetitionScreen(onBack: () -> Unit, onUploadSuccess: () -> Unit) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // ===== STATE: AMBIL DATA USER DARI SHARED PREFERENCES =====
    var userId by remember { mutableIntStateOf(0) }
    var sisaKuotaGratis by remember { mutableIntStateOf(5) }
    var userRole by remember { mutableStateOf("peserta") }

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        userId = sharedPref.getInt("user_id", 0)
        sisaKuotaGratis = sharedPref.getInt("sisa_kuota", 5)
        userRole = sharedPref.getString("role", "peserta") ?: "peserta"
    }

    // ===== STATE: FORM INPUTS =====
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Teknologi & IT") }
    var tingkatPendidikan by remember { mutableStateOf("SMA / SMK") }
    var wilayah by remember { mutableStateOf("Umum") }
    var tanggal by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var linkJuknis by remember { mutableStateOf("") }
    var hargaDaftar by remember { mutableStateOf("") }

    // ===== STATE: UI INTERACTION =====
    var isWilayahDropdownExpanded by remember { mutableStateOf(false) }
    var showQrisDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            tanggal = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // ====================================================================
    // FUNGSI HELPER: Cek apakah perlu bayar
    // ====================================================================
    fun dapatkaBiayaPembayaran(): Int {
        return if (wilayah in listOf("Provinsi", "Nasional", "Internasional")) {
            50000 // Premium level: 50rb
        } else {
            0 // Gratis atau sudah bayar kuota
        }
    }

    fun dapatkanyaNeedsBayar(): Boolean {
        val isPremiumLevel = wilayah in listOf("Provinsi", "Nasional", "Internasional")
        val isGratisHabis = (wilayah in listOf("Umum", "Kota")) && sisaKuotaGratis <= 0
        return isPremiumLevel || isGratisHabis
    }

    // ====================================================================
    // FUNGSI UPLOAD KE DATABASE
    // ====================================================================
    fun uploadLombaKeDatabase() {
        // Validasi input
        if (judul.isEmpty() || tanggal.isEmpty()) {
            Toast.makeText(context, "⚠️ Judul dan tanggal pelaksanaan wajib diisi!", Toast.LENGTH_SHORT).show()
            isLoading = false
            return
        }

        isLoading = true

        // Siapkan data untuk dikirim
        val biayaToSubmit = if (dapatkanyaNeedsBayar()) {
            // Kalau perlu bayar, set biaya ke 0 (karena sudah bayar di QRIS)
            // Backend akan handle kuota berdasarkan wilayah
            0
        } else {
            0 // Gratis
        }

        // Panggil API
        RetrofitClient.instance.createCompetition(
            penyelenggara_id = userId,
            judul_lomba = judul,
            kategori = kategori,
            tingkat_pendidikan = tingkatPendidikan,
            tingkat_lomba = wilayah, // PENTING: gunakan wilayah, bukan parameter lain
            deskripsi = deskripsi.ifEmpty { linkJuknis },
            link_pendaftaran = "", // Bisa kosong untuk sekarang
            link_panduan = linkJuknis,
            poster_url = "", // Akan diisi dari Cloudinary nanti
            biaya_pendaftaran = biayaToSubmit,
            tanggal_tutup_daftar = tanggal,
            tanggal_pelaksanaan = tanggal
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading = false
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(context, "🎉 Lomba berhasil diterbitkan!", Toast.LENGTH_SHORT).show()
                    showQrisDialog = false

                    // ===== UPDATE KUOTA LOKAL =====
                    if (wilayah in listOf("Umum", "Kota") && sisaKuotaGratis > 0) {
                        sisaKuotaGratis--
                        val sharedPref = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                        sharedPref.edit().putInt("sisa_kuota", sisaKuotaGratis).apply()
                    }

                    // ===== TUNGGU SEBENTAR BIAR SERVER SEMPAT SIMPAN, BARU REFRESH =====
                    Handler(Looper.getMainLooper()).postDelayed({
                        onUploadSuccess() // Refresh list di HomeScreen
                    }, 1200)
                } else {
                    Toast.makeText(
                        context,
                        "❌ Gagal: ${response.body()?.message ?: "Terjadi kesalahan"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "⚠️ Error jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ====================================================================
    // UI UTAMA
    // ====================================================================
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Buat Lomba Baru",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ===== ALERT KUOTA GRATIS =====
            if (sisaKuotaGratis <= 0 && userRole == "penyelenggara") {
                Surface(
                    color = Color(0xFFFEF2F2),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Kuota gratis (Umum/Kota) sudah habis. Untuk lomba level Provinsi ke atas, ada biaya admin Rp 50.000.",
                            color = Color(0xFF991B1B),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ===== INFORMASI DASAR =====
            SectionHeader("INFORMASI DASAR")

            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul Perlombaan *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // ===== KATEGORI =====
            Column {
                Text("Kategori *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                val kategoriList = listOf("Teknologi & IT", "Seni & Desain", "Olahraga", "Akademik", "Lainnya")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    kategoriList.forEach { kat ->
                        SelectableChip(
                            text = kat,
                            isSelected = kategori == kat
                        ) { kategori = kat }
                    }
                }
            }

            // ===== TINGKAT PENDIDIKAN =====
            Column {
                Text("Tingkat Pendidikan *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                val pendidikanList = listOf("Sekolah Dasar (SD)", "SMP / Sederajat", "SMA / SMK", "Mahasiswa / Umum")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pendidikanList.forEach { pend ->
                        SelectableChip(
                            text = pend,
                            isSelected = tingkatPendidikan == pend
                        ) { tingkatPendidikan = pend }
                    }
                }
            }

            // ===== TINGKAT WILAYAH =====
            Column {
                Text("Tingkat Wilayah Lomba *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = isWilayahDropdownExpanded,
                    onExpandedChange = { isWilayahDropdownExpanded = !isWilayahDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = wilayah,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWilayahDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isWilayahDropdownExpanded,
                        onDismissRequest = { isWilayahDropdownExpanded = false }
                    ) {
                        listOf("Umum", "Kota", "Provinsi", "Nasional", "Internasional").forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection) },
                                onClick = {
                                    wilayah = selection
                                    isWilayahDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // ===== TANGGAL PELAKSANAAN =====
            OutlinedTextField(
                value = tanggal,
                onValueChange = {},
                label = { Text("Tanggal Pelaksanaan *") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                enabled = false,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black),
                trailingIcon = { Icon(Icons.Default.DateRange, null) }
            )

            // ===== DESKRIPSI =====
            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi Lomba") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // ===== LINK JUKNIS =====
            OutlinedTextField(
                value = linkJuknis,
                onValueChange = { linkJuknis = it },
                label = { Text("Link GDrive Juknis / Panduan") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===== TOMBOL SUBMIT =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("Batal", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        if (judul.isEmpty() || tanggal.isEmpty()) {
                            Toast.makeText(
                                context,
                                "⚠️ Judul dan tanggal pelaksanaan wajib diisi!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // CEK APAKAH PERLU BAYAR
                            if (dapatkanyaNeedsBayar()) {
                                showQrisDialog = true
                            } else {
                                // LANGSUNG UPLOAD (GRATIS & ADA KUOTA)
                                uploadLombaKeDatabase()
                            }
                        }
                    },
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0061D1)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Terbitkan Lomba Sekarang", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // ====================================================================
        // DIALOG PEMBAYARAN QRIS
        // ====================================================================
        if (showQrisDialog) {
            val biayaAdmin = dapatkaBiayaPembayaran()
            AlertDialog(
                onDismissRequest = { showQrisDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5F0FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = Color(0xFF0061D1)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Pembayaran Layanan",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (wilayah in listOf("Provinsi", "Nasional", "Internasional")) {
                                "Penerbitan lomba untuk tingkat $wilayah memerlukan biaya admin platform."
                            } else {
                                "Kuota gratis Anda sudah habis. Untuk melanjutkan, diperlukan pembayaran admin platform."
                            },
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(Color.White)
                                .border(2.dp, Color(0xFF0061D1), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QrCode2,
                                contentDescription = "QRIS",
                                modifier = Modifier.size(120.dp),
                                tint = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Total Tagihan", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Rp ${String.format("%,d", biayaAdmin).replace(",", ".")}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color(0xFF0061D1)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { uploadLombaKeDatabase() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0061D1)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Saya Sudah Bayar & Terbitkan", fontWeight = FontWeight.Bold)
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showQrisDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Batalkan", color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

// ====================================================================
// KOMPONEN UI BANTUAN
// ====================================================================
@Composable
fun SectionHeader(title: String, paddingBottom: Int = 8) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(18.dp)
                .background(Color(0xFF0061D1), RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
    Spacer(modifier = Modifier.height(paddingBottom.dp))
}

@Composable
fun SelectableChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) Color(0xFFE5F0FF) else Color.White,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF0061D1) else Color.LightGray
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF0061D1) else Color.Gray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}