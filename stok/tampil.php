<?php

include "../koneksi.php";

// Memeriksa apakah koneksi berhasil
if ($db->connect_error) {
    die("Connection failed: " . $db->connect_error);
}

// Mempersiapkan query SQL untuk mengambil data dari tabel jumlahhutang dan hutang
$sql = "SELECT stok.id_stok, kategori.nama_sepeda, stok.jumlah
        FROM stok 
        JOIN kategori ON stok.id_kategori = kategori.id_kategori ";

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