<?php
include "../koneksi.php";
$sql = "Select * from metode_pembayaran";
$query = mysqli_query($db, $sql);
$list_data = array();
while ($data = mysqli_fetch_assoc($query)) {
    $list_data[] = array(
        'id_pembayaran' => $data['id_pembayaran'],
        'jenis_pembayaran' => $data['jenis_pembayaran'],
        'keterangan' => $data['keterangan'],
    );
}
echo json_encode(array(
'data'=>$list_data));
?>