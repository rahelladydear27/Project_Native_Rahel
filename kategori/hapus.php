<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_kategori=$_POST['id_kategori'];
$sql ="delete from kategori where id_kategori ='".$id_kategori."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>