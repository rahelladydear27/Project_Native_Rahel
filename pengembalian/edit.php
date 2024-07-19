<?php
include "../koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = [
        'nama_pemesan', 'nama_sepeda', 'jenis_pembayaran', 'nama_petugas', 'waktu_pengembalian', 'harga'
    ];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(['status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"]);
            exit;
        }
    }

    // Ambil nilai dari POST
    $id_pengembalian = $_POST['id_pengembalian'];
    $nama_pemesan = $_POST['nama_pemesan'];
    $nama_sepeda = $_POST['nama_sepeda'];
    $jenis_pembayaran = $_POST['jenis_pembayaran'];
    $nama_petugas = $_POST['nama_petugas'];
    // $waktu_pengembalian = $_POST['waktu_pengembalian'];
    $harga = $_POST['harga'];

    // Mendapatkan id_pencatatan berdasarkan nama_pemesan dari tabel pencatatan
    $sql_pencatatan = "SELECT id_pencatatan FROM pencatatan WHERE BINARY nama_pemesan = ?";
    $stmt_pencatatan = $db->prepare($sql_pencatatan);
    if ($stmt_pencatatan === false) {
        echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
        exit;
    }
    $stmt_pencatatan->bind_param('s', $nama_pemesan);
    $stmt_pencatatan->execute();
    $result_pencatatan = $stmt_pencatatan->get_result();

    if ($result_pencatatan->num_rows > 0) {
        $pencatatan_data = $result_pencatatan->fetch_assoc();
        $id_pencatatan = $pencatatan_data['id_pencatatan'];

        // Mendapatkan id_kategori berdasarkan nama_sepeda dari tabel kategori
        $sql_kategori = "SELECT id_kategori FROM kategori WHERE BINARY nama_sepeda = ?";
        $stmt_kategori = $db->prepare($sql_kategori);
        if ($stmt_kategori === false) {
            echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
            exit;
        }
        $stmt_kategori->bind_param('s', $nama_sepeda);
        $stmt_kategori->execute();
        $result_kategori = $stmt_kategori->get_result();

        if ($result_kategori->num_rows > 0) {
            $kategori_data = $result_kategori->fetch_assoc();
            $id_kategori = $kategori_data['id_kategori'];

            // Pastikan id_kategori ada di tabel stok
            $sql_stok = "SELECT id_stok FROM stok WHERE id_kategori = ?";
            $stmt_stok = $db->prepare($sql_stok);
            if ($stmt_stok === false) {
                echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                exit;
            }
            $stmt_stok->bind_param('i', $id_kategori);
            $stmt_stok->execute();
            $result_stok = $stmt_stok->get_result();

            if ($result_stok->num_rows > 0) {
                $stok_data = $result_stok->fetch_assoc();
                $id_stok = $stok_data['id_stok'];

                // Mendapatkan id_pembayaran berdasarkan jenis_pembayaran dari tabel metode_pembayaran
                $sql_metode_pembayaran = "SELECT id_pembayaran FROM metode_pembayaran WHERE jenis_pembayaran = ?";
                $stmt_metode_pembayaran = $db->prepare($sql_metode_pembayaran);
                if ($stmt_metode_pembayaran === false) {
                    echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                    exit;
                }
                $stmt_metode_pembayaran->bind_param('s', $jenis_pembayaran);
                $stmt_metode_pembayaran->execute();
                $result_metode_pembayaran = $stmt_metode_pembayaran->get_result();

                if ($result_metode_pembayaran->num_rows > 0) {
                    $metode_pembayaran_data = $result_metode_pembayaran->fetch_assoc();
                    $id_pembayaran = $metode_pembayaran_data['id_pembayaran'];

                    // Mendapatkan id_petugas berdasarkan nama_petugas dari tabel petugas
                    $sql_petugas = "SELECT id_petugas FROM petugas WHERE nama_petugas = ?";
                    $stmt_petugas = $db->prepare($sql_petugas);
                    if ($stmt_petugas === false) {
                        echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                        exit;
                    }
                    $stmt_petugas->bind_param('s', $nama_petugas);
                    $stmt_petugas->execute();
                    $result_petugas = $stmt_petugas->get_result();

                    if ($result_petugas->num_rows > 0) {
                        $petugas_data = $result_petugas->fetch_assoc();
                        $id_petugas = $petugas_data['id_petugas'];

                        // Memastikan tidak ada entri duplikat di tabel pengembalian
                        $sql_check = "SELECT COUNT(*) as count FROM pengembalian WHERE id_pencatatan = ? AND id_stok = ? AND id_pembayaran = ? AND id_petugas = ? AND waktu_pengembalian = ? AND harga = ?";
                        $stmt_check = $db->prepare($sql_check);
                        if ($stmt_check === false) {
                            echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                            exit;
                        }
                        $stmt_check->bind_param('iiiisi', $id_pencatatan, $id_stok, $id_pembayaran, $id_petugas, $waktu_pengembalian, $harga);
                        $stmt_check->execute();
                        $result_check = $stmt_check->get_result();
                        $check_data = $result_check->fetch_assoc();

                        if ($check_data['count'] > 0) {
                            echo json_encode(['status' => 'error', 'message' => 'Data sudah ada di tabel pengembalian']);
                            exit;
                        }

                        // Simpan data ke tabel pengembalian
                        $sql_insert = "UPDATE pengembalian SET id_pencatatan=?, id_stok=?, id_pembayaran=?, id_petugas=?, waktu_pengembalian=CURRENT_TIMESTAMP, harga=? WHERE id_pengembalian =$id_pengembalian";
                        $stmt_insert = $db->prepare($sql_insert);
                        if ($stmt_insert === false) {
                            echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                            exit;
                        }
                        $stmt_insert->bind_param('iiiii', $id_pencatatan, $id_stok, $id_pembayaran, $id_petugas, $harga);

                        if ($stmt_insert->execute()) {
                            echo json_encode(['status' => 'success', 'message' => 'Data berhasil diedit']);
                        } else {
                            echo json_encode(['status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error]);
                        }
                    } else {
                        echo json_encode(['status' => 'error', 'message' => 'Petugas dengan nama tersebut tidak ditemukan']);
                    }
                } else {
                    echo json_encode(['status' => 'error', 'message' => 'Metode pembayaran dengan jenis tersebut tidak ditemukan']);
                }
            } else {
                echo json_encode(['status' => 'error', 'message' => 'Stok dengan id_kategori tersebut tidak ditemukan']);
            }
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Kategori dengan nama sepeda tersebut tidak ditemukan']);
        }
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Pencatatan dengan nama pemesan tersebut tidak ditemukan']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'Permintaan tidak valid']);
}

$db->close();
?>
