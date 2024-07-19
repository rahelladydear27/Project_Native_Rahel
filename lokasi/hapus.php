<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_lokasi=$_POST['id_lokasi'];
$sql ="delete from lokasi where id_lokasi ='".$id_lokasi."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>