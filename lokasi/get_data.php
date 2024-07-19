<?php
include "../koneksi.php";
$id_lokasi=$_POST['id_lokasi'];
$sql ="select * from lokasi where id_pembayaran='" . $id_lokasi . "'";
//echo $sql;
//PERTEMUAN 7 MENGGUNAKAN POSTMAN 6
$query = mysqli_query($db,$sql);
$data=mysqli_fetch_assoc($query);
echo json_encode(array(
'nama_lokasi' => $data['nama_lokasi'],
'alamat' => $data['alamat']
));
?>