<?php
require_once '../koneksi.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_kategori = isset($_POST['id_kategori']) ? $_POST['id_kategori'] : '';
$nama_sepeda = isset($_POST['nama_sepeda']) ? $_POST['nama_sepeda'] : '';
$kapasitas = isset($_POST['kapasitas']) ? $_POST['kapasitas'] : '';



// Mengecek apakah ada ID pelanggan bulanan yang dikirimkan
if (!empty($id_kategori)) {
    // Jika ada ID mahasiswa, maka proses untuk update data
    $sql = "UPDATE kategori SET nama_sepeda='" . $nama_sepeda . "', kapasitas='" . $kapasitas . "' WHERE id_kategori='" . $id_kategori . "'";
} else {
    // Jika tidak ada ID mahasiswa, maka proses untuk insert data baru
    $sql = "INSERT INTO kategori ( nama_sepeda, kapasitas, nomor_hp) VALUES ('" . $nama_sepeda . "','" . $kapasitas . "')";
}

// Eksekusi query ke database
$query = mysqli_query($db, $sql);

// Mengecek apakah query berhasil dieksekusi
if ($query) {
    // Jika berhasil, kirimkan status 'data_tersimpan'
    echo json_encode(array('status' => 'data_tersimpan'));
} else {
    // Jika gagal, kirimkan status 'gagal_tersimpan'
    echo json_encode(array('status' => 'gagal_tersimpan'));
}