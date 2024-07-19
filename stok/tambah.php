<?php
include "../koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = ['nama_sepeda', 'jumlah'];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(array('status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"));
            exit;
        }
    }

    // Ambil nilai dari POST
    $nama_sepeda = trim($_POST['nama_sepeda']);
    $jumlah = $_POST['jumlah'];

    // Mendapatkan id_kategori berdasarkan nama_sepeda dari tabel kategori
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

        // Memastikan tidak ada entri duplikat di tabel stok
        $sql_check = "SELECT COUNT(*) as count FROM stok WHERE id_kategori = ?";
        $stmt_check = $db->prepare($sql_check);
        if ($stmt_check === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error));
            exit;
        }
        $stmt_check->bind_param('i', $id_kategori);
        $stmt_check->execute();
        $result_check = $stmt_check->get_result();
        $check_data = $result_check->fetch_assoc();

        if ($check_data['count'] > 0) {
            echo json_encode(array('status' => 'error', 'message' => 'Data sudah ada di tabel stok'));
            exit;
        }

        // Simpan data ke tabel stok
        $sql_insert = "INSERT INTO stok (id_kategori, jumlah) VALUES (?, ?)";
        $stmt_insert = $db->prepare($sql_insert);
        if ($stmt_insert === false) {
            echo json_encode(array('status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error));
            exit;
        }
        $stmt_insert->bind_param('ii', $id_kategori, $jumlah);

        if ($stmt_insert->execute()) {
            echo json_encode(array('status' => 'success', 'message' => 'Data berhasil disimpan'));
        } else {
            echo json_encode(array('status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error));
        }
    } else {
        echo json_encode(array('status' => 'error', 'message' => 'Nama sepeda tidak ditemukan di tabel kategori'));
    }
} else {
    echo json_encode(array('status' => 'error', 'message' => 'Permintaan tidak valid'));
}

$db->close();
?>
