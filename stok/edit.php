<?php
require_once '../koneksi.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['id_stok', 'nama_sepeda', 'jumlah'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $id_stok = intval($_POST['id_stok']);
    $nama_sepeda = trim($_POST['nama_sepeda']);
    $jumlah = trim($_POST['jumlah']);

    // Mendapatkan id berdasarkan nama dari tabel customer
    $sql_kategori = "SELECT id_kategori FROM kategori WHERE BINARY nama_sepeda = ?";
    $stmt_kategori = $db->prepare($sql_kategori);
    if ($stmt_kategori === false) {
        echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error));
        exit;
    }
    $stmt_kategori->bind_param('s', $nama_sepeda);
    $stmt_kategori->execute();
    $result_kategori = $stmt_kategori->get_result();

    if ($result_kategori->num_rows > 0) {
        $kategori_data = $result_kategori->fetch_assoc();
        $id_kategori = $kategori_data['id_kategori'];

        // Memastikan id dan roomid unik dalam tabel booking
        $sql_unique = "SELECT COUNT(*) as count FROM stok WHERE id_stok = ? AND id_kategori != ?";
        $stmt_unique = $db->prepare($sql_unique);
        if ($stmt_unique === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error));
            exit;
        }
        // $stmt_unique->bind_param('iii', $id, $roomid, $booking_id);
        // $stmt_unique->execute();
        // $result_unique = $stmt_unique->get_result();
        // $unique_data = $result_unique->fetch_assoc();

        // if ($unique_data['count'] > 0) {
        //     echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel booking'));
        //     exit;
        // }

        // Update data di tabel booking tanpa mengubah id dan roomid
        $sql_update = "UPDATE stok SET id_kategori = ?, jumlah = ? WHERE id_stok = ?";
        $stmt_update = $db->prepare($sql_update);
        if ($stmt_update === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error));
            exit;
        }
        $stmt_update->bind_param('ssi', $id_kategori,$jumlah, $id_stok);

        if ($stmt_update->execute()) {
            if ($stmt_update->affected_rows > 0) {
                echo json_encode(array('status' => 'success', 'message' => 'Data berhasil diperbarui'));
            } else {
                echo json_encode(array('status' => 'error', 'message' => 'Tidak ada perubahan dilakukan pada data'));
            }
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Update gagal: ' . $stmt_update->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama customer atau nama kamar tidak ditemukan di tabel terkait'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

$db->close();
?>
