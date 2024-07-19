<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_pengembalian=$_POST['id_pengembalian'];
$sql ="delete from pengembalian where id_pengembalian ='".$id_pengembalian."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>