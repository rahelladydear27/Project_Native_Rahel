<?php
require_once '../koneksi.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_pembayaran = isset($_POST['id_pembayaran']) ? $_POST['id_pembayaran'] : '';
$jenis_pembayaran = isset($_POST['jenis_pembayaran']) ? $_POST['jenis_pembayaran'] : '';
$keterangan = isset($_POST['keterangan']) ? $_POST['keterangan'] : '';



// Mengecek apakah ada ID pelanggan bulanan yang dikirimkan
if (!empty($id_pembayaran)) {
    // Jika ada ID mahasiswa, maka proses untuk update data
    $sql = "UPDATE metode_pembayaran SET jenis_pembayaran='" . $jenis_pembayaran . "',keterangan='" . $keterangan . "' WHERE id_pembayaran='" . $id_pembayaran . "'";
} else {
    // Jika tidak ada ID mahasiswa, maka proses untuk insert data baru
    $sql = "INSERT INTO metode_pembayaran ( jenis_pembayaran, keterangan) VALUES ('" . $jenis_pembayaran . "','" . $keterangan . "')";
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