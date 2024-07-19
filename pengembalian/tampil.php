<?php

include "../koneksi.php";


// Mempersiapkan query SQL untuk mengambil data dari tabel jumlahhutang dan hutang
$sql = "SELECT pengembalian.id_pengembalian, 
pencatatan.nama_pemesan, 
kategori.nama_sepeda, 
petugas.nama_petugas, 
metode_pembayaran.jenis_pembayaran, 
pengembalian.waktu_pengembalian, 
pengembalian.harga 

FROM pengembalian 
JOIN pencatatan ON pengembalian.id_pencatatan = pencatatan.id_pencatatan 
JOIN stok ON pengembalian.id_stok = stok.id_stok
JOIN kategori ON stok.id_kategori = kategori.id_kategori
JOIN petugas ON pengembalian.id_petugas = petugas.id_petugas
JOIN metode_pembayaran ON pengembalian.id_pembayaran = metode_pembayaran.id_pembayaran";

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