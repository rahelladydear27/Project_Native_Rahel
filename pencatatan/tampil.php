<?php

include "../koneksi.php";


// Mempersiapkan query SQL untuk mengambil data dari tabel jumlahhutang dan hutang
$sql = "SELECT pencatatan.id_pencatatan, 
petugas.nama_petugas, 
lokasi.nama_lokasi, 
kategori.nama_sepeda,
pencatatan.nama_pemesan,
pencatatan.nomor_hp_pemesan,
pencatatan.jumlah_pemesanan,
pencatatan.mulai_sewa

FROM pencatatan 
JOIN petugas ON pencatatan.id_petugas = petugas.id_petugas 
JOIN lokasi ON pencatatan.id_lokasi = lokasi.id_lokasi
JOIN stok ON pencatatan.id_stok = stok.id_stok
JOIN kategori ON stok.id_kategori = kategori.id_kategori";

$query = $db->query($sql);

// Memeriksa apakah query berhasil dijalankan
if (!$query) {
    // Mengembalikan pesan kesalahan dalam format JSON jika query gagal
    echo json_encode(array('status' => 'error', 'message' => 'Query failed: ' . $db->error));
    exit;
}

// Membuat array untuk menyimpan data yang diambil
$list_data = array();

// Mengambil data dari hasil query dan menyimpannya dalam array
while ($row = $query->fetch_assoc()) {
    $list_data[] = $row;
}

// Mengatur header agar browser mengetahui bahwa respons berformat JSON
header('Content-Type: application/json');

// Mengembalikan data dalam format JSON
echo json_encode(array('data' => $list_data));

// Menutup koneksi ke database
$db->close();