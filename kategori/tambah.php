<?php

include "../koneksi.php";

$id_kategori=$_POST['id_kategori'];
$nama_sepeda=$_POST['nama_sepeda'];
$kapasitas=$_POST['kapasitas'];

$sql="INSERT INTO kategori (id_kategori,nama_sepeda,kapasitas) VALUES (NULL,'$nama_sepeda','$kapasitas')";

$query=mysqli_query($db,$sql);

if($query){
    echo json_encode(array('status' => 'data_tersimpan'));
}else{
    echo json_encode(array('status' => 'gagal_tersimpan'));
}
?>