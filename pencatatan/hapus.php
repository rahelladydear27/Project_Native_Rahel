<?php
include "../koneksi.php";
//$_POST=htmlentities($_POST,true);
$id_pencatatan=$_POST['id_pencatatan'];
$sql ="delete from pencatatan where id_pencatatan ='".$id_pencatatan."'";
//echo $sql;
$query = mysqli_query($db,$sql);
if($query) {
echo json_encode(array(
'status' => 'data_berhasil_di_hapus'));
} else {
echo "gagal di hapus";
}
?>