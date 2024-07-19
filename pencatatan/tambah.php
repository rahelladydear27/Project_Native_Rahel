<?php
include "../koneksi.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Pastikan semua kunci yang diperlukan ada dalam array POST
    $required_keys = [
        'nama_petugas', 'nama_lokasi', 'nama_sepeda', 'nama_pemesan', 
        'nomor_hp_pemesan', 'jumlah_pemesanan'
    ];
    foreach ($required_keys as $key) {
        if (!isset($_POST[$key])) {
            echo json_encode(['status' => 'error', 'message' => "Key '$key' tidak ditemukan dalam request"]);
            exit;
        }
    }

    // Ambil nilai dari POST
    $nama_petugas = $_POST['nama_petugas'];
    $nama_lokasi = $_POST['nama_lokasi'];
    $nama_sepeda = $_POST['nama_sepeda'];
    $nama_pemesan = $_POST['nama_pemesan'];
    $nomor_hp_pemesan = $_POST['nomor_hp_pemesan'];
    $jumlah_pemesanan = $_POST['jumlah_pemesanan'];
    
    // Mendapatkan id_petugas berdasarkan nama_petugas dari tabel petugas
    $sql_petugas = "SELECT id_petugas FROM petugas WHERE BINARY nama_petugas = ?";
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

        // Mendapatkan id_lokasi berdasarkan nama_lokasi dari tabel lokasi
        $sql_lokasi = "SELECT id_lokasi FROM lokasi WHERE BINARY nama_lokasi = ?";
        $stmt_lokasi = $db->prepare($sql_lokasi);
        if ($stmt_lokasi === false) {
            echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
            exit;
        }
        $stmt_lokasi->bind_param('s', $nama_lokasi);
        $stmt_lokasi->execute();
        $result_lokasi = $stmt_lokasi->get_result();

        if ($result_lokasi->num_rows > 0) {
            $lokasi_data = $result_lokasi->fetch_assoc();
            $id_lokasi = $lokasi_data['id_lokasi'];

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

                    // Memastikan tidak ada entri duplikat di tabel pencatatan
                    $sql_check = "SELECT COUNT(*) as count FROM pencatatan WHERE id_petugas = ? AND id_lokasi = ? AND id_stok = ? AND mulai_sewa = ?";
                    $stmt_check = $db->prepare($sql_check);
                    if ($stmt_check === false) {
                        echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                        exit;
                    }
                    $stmt_check->bind_param('iiis', $id_petugas, $id_lokasi, $id_stok, $mulai_sewa);
                    $stmt_check->execute();
                    $result_check = $stmt_check->get_result();
                    $check_data = $result_check->fetch_assoc();

                    if ($check_data['count'] > 0) {
                        echo json_encode(['status' => 'error', 'message' => 'Data sudah ada di tabel pencatatan']);
                        exit;
                    }

                    // Simpan data ke tabel pencatatan
                    $sql_insert = "INSERT INTO pencatatan (
                        id_petugas, id_lokasi, id_stok, nama_pemesan, 
                        nomor_hp_pemesan, jumlah_pemesanan, mulai_sewa
                    ) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
                    $stmt_insert = $db->prepare($sql_insert);
                    if ($stmt_insert === false) {
                        echo json_encode(['status' => 'error', 'message' => 'Prepare statement gagal: ' . $db->error]);
                        exit;
                    }
                    $stmt_insert->bind_param('iiissi', 
                        $id_petugas, $id_lokasi, $id_stok, $nama_pemesan, 
                        $nomor_hp_pemesan, $jumlah_pemesanan
                    );

                    if ($stmt_insert->execute()) {
                        echo json_encode(['status' => 'success', 'message' => 'Data berhasil disimpan']);
                    } else {
                        echo json_encode(['status' => 'error', 'message' => 'Simpan gagal: ' . $stmt_insert->error]);
                    }
                } else {
                    echo json_encode(['status' => 'error', 'message' => 'Stok dengan id tersebut tidak ditemukan']);
                }
            } else {
                echo json_encode(['status' => 'error', 'message' => 'Kategori dengan nama tersebut tidak ditemukan']);
            }
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Lokasi dengan nama tersebut tidak ditemukan']);
        }
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Petugas dengan nama tersebut tidak ditemukan']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'Permintaan tidak valid']);
}

$db->close();
?>
