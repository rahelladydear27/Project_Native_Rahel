<?php
$server = "localhost";
$user = "root";
$password = "root"; // MAMP default password
$nama_db = "db_sewa";
$port = 8889; 
$db = mysqli_connect($server, $user, $password, $nama_db);
if($db) {
    // echo "Koneksi mantap ";
}
