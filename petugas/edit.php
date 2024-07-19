<?php
require_once '../koneksi.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_petugas = isset($_POST['id_petugas']) ? $_POST['id_petugas'] : '';
$nama_petugas = isset($_POST['nama_petugas']) ? $_POST['nama_petugas'] : '';
$alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';
$nomor_hp = isset($_POST['nomor_hp']) ? $_POST['nomor_hp'] : '';



// Mengecek apakah ada ID pelanggan bulanan yang dikirimkan
if (!empty($id_petugas)) {
    // Jika ada ID mahasiswa, maka proses untuk update data
    $sql = "UPDATE petugas SET nama_petugas='" . $nama_petugas . "', alamat='" . $alamat . "', nomor_hp='" . $nomor_hp . "' WHERE id_petugas='" . $id_petugas . "'";
} else {
    // Jika tidak ada ID mahasiswa, maka proses untuk insert data baru
    $sql = "INSERT INTO petugas ( nama_petugas, alamat, nomor_hp) VALUES ('" . $nama_petugas . "','" . $alamat . "','" . $nomor_hp . "')";
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