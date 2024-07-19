<?php
require_once '../koneksi.php';

header('Content-Type: application/json');

// Mengambil data dari POST
$id_lokasi = isset($_POST['id_lokasi']) ? $_POST['id_lokasi'] : '';
$nama_lokasi = isset($_POST['nama_lokasi']) ? $_POST['nama_lokasi'] : '';
$alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';



// Mengecek apakah ada ID pelanggan bulanan yang dikirimkan
if (!empty($id_lokasi)) {
    // Jika ada ID mahasiswa, maka proses untuk update data
    $sql = "UPDATE lokasi SET nama_lokasi='" . $nama_lokasi . "',alamat='" . $alamat . "' WHERE id_lokasi='" . $id_lokasi . "'";
} else {
    // Jika tidak ada ID mahasiswa, maka proses untuk insert data baru
    $sql = "INSERT INTO lokasi ( nama_lokasi, alamat,) VALUES ('" . $nama_lokasi . "','" . $alamat . "')";
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