<?php

include "../koneksi.php";

$id_pembayaran=$_POST['id_pembayaran'];
$jenis_pembayaran=$_POST['jenis_pembayaran'];
$keterangan=$_POST['keterangan'];

$sql="INSERT INTO metode_pembayaran (id_pembayaran,jenis_pembayaran,keterangan) VALUES (NULL,'$jenis_pembayaran','$keterangan')";

$query=mysqli_query($db,$sql);

if($query){
    echo json_encode(array('status' => 'data_tersimpan'));
}else{
    echo json_encode(array('status' => 'gagal_tersimpan'));
}
?>