<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_petugas=$_POST['id_petugas'];
$sql ="delete from petugas where id_petugas ='".$id_petugas."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>