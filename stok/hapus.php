<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_stok=$_POST['id_stok'];
$sql ="delete from stok where id_stok ='".$id_stok."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>