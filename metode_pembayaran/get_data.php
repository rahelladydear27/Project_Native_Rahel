<?php
include "../koneksi.php";
$id_pembayaran=$_POST['id_pembayaran'];
$sql ="select * from metode_pembayaran where id_pembayaran='" . $id_pembayaran . "'";
//echo $sql;
//PERTEMUAN 7 MENGGUNAKAN POSTMAN 6
$query = mysqli_query($db,$sql);
$data=mysqli_fetch_assoc($query);
echo json_encode(array(
'jenis_pembayaran' => $data['jenis_pembayaran'],
'keterangan' => $data['keterangan']
));
?>