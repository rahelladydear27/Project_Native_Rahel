<?php
include "../koneksi.php";
$sql = "Select * from petugas";
$query = mysqli_query($db, $sql);
$list_data = array();
while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_petugas' => $data['id_petugas'],
        'nama_petugas' => $data['nama_petugas'],
        'alamat' => $data['alamat'],
        'nomor_hp' => $data['nomor_hp']
    );
}
echo json_encode(array(
'data'=>$list_data));
?>