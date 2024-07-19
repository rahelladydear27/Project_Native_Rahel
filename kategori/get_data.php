<?php
include "../koneksi.php";
$id_kategori=$_POST['id_kategori'];
$sql ="select * from kategori where id_kategori='" . $id_kategori . "'";
//echo $sql;
//PERTEMUAN 7 MENGGUNAKAN POSTMAN 6
$query = mysqli_query($db,$sql);
$data=mysqli_fetch_assoc($query);
echo json_encode(array(
'nama_sepeda' => $data['nama_sepeda'],
'kapasitas' => $data['kapasitas']
));
?>