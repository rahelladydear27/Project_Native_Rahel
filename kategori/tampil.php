<?php
include "../koneksi.php";
$sql = "Select * from kategori";
$query = mysqli_query($db, $sql);
$list_data = array();
while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_kategori' => $data['id_kategori'],
        'nama_sepeda' => $data['nama_sepeda'],
        'kapasitas' => $data['kapasitas'],
    );
}
echo json_encode(array(
'data'=>$list_data));
?>