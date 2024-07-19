<?php

require_once '../koneksi.php';

header('Content-Type: application/json');

// Memeriksa apakah semua data POST diterima
if (isset($_POST['nama_petugas'], $_POST['alamat'], $_POST['nomor_hp'])) {

    $id_petugas = uniqid('pel_'); // Menghasilkan ID unik dengan prefix 'pel_'
    $nama_petugas = $_POST['nama_petugas'];
    $alamat = $_POST['alamat'];
    $nomor_hp = $_POST['nomor_hp'];
   

    // Query SQL yang dikoreksi
    $sql = "INSERT INTO petugas (id_petugas, nama_petugas, nomor_hp, alamat) 
            VALUES ( NULL, '$nama_petugas', '$nomor_hp', '$alamat')";

    $query = mysqli_query($db, $sql);

    if ($query) {
        echo json_encode(array('status' => 'data_tersimpan', 'id_petugas' => $id_petugas));
    } else {
        echo json_encode(array('status' => 'gagal_tersimpan', 'error' => mysqli_error($db)));
    }

} else {
    echo json_encode(array('status' => 'data_tidak_lengkap', 'received' => $_POST));
}

mysqli_close($db);