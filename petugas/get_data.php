<?php
include "../koneksi.php";
$id_petugas=$_POST['id_petugas'];
$sql ="select * from petugas where id_petugas='" . $id_petugas . "'";
//echo $sql;
//PERTEMUAN 7 MENGGUNAKAN POSTMAN 6
$query = mysqli_query($db,$sql);
$data=mysqli_fetch_assoc($query);
echo json_encode(array(
'nama_petugas' => $data['nama_petugas'],
'alamat' => $data['alamat'],
'nomor_hp' => $data['nomor_hp']
));
?>