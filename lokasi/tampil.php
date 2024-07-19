<?php
include "../koneksi.php";
$sql = "Select * from lokasi";
$query = mysqli_query($db, $sql);
$list_data = array();
while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_lokasi' => $data['id_lokasi'],
        'nama_lokasi' => $data['nama_lokasi'],
        'alamat' => $data['alamat'],
    );
}
echo json_encode(array(
'data'=>$list_data));
?>