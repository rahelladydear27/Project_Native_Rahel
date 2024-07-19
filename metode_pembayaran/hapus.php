<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_pembayaran=$_POST['id_pembayaran'];
$sql ="delete from metode_pembayaran where id_pembayaran ='".$id_pembayaran."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>