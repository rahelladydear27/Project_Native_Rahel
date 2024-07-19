<?php

include "../koneksi.php";

$id_lokasi=$_POST['id_lokasi'];
$nama_lokasi=$_POST['nama_lokasi'];
$alamat=$_POST['alamat'];

$sql="INSERT INTO lokasi (id_lokasi,nama_lokasi,alamat) VALUES (NULL,'$nama_lokasi','$alamat')";

$query=mysqli_query($db,$sql);

if($query){
    echo json_encode(array('status' => 'data_tersimpan'));
}else{
    echo json_encode(array('status' => 'gagal_tersimpan'));
}
?>