package com.example.pencatatan;

import java.io.Serializable;

public class GetDataPencatatan implements Serializable {
    private String id_pencatatan;
    private String nama_petugas;
    private String nama_lokasi;
    private String nama_sepeda;
    private String nama_pemesan;
    private String nomor_hp_pemesan;
    private String jumlah_pemesanan;
    private String mulai_sewa;

    public GetDataPencatatan(String id_pencatatan,
                             String nama_petugas,
                             String nama_lokasi,
                             String nama_sepeda,
                             String nama_pemesan,
                             String nomor_hp_pemesan,
                             String jumlah_pemesanan,
                             String mulai_sewa) {
        this.id_pencatatan = id_pencatatan;
        this.nama_petugas = nama_petugas;
        this.nama_lokasi = nama_lokasi;
        this.nama_sepeda = nama_sepeda;
        this.nama_pemesan = nama_pemesan;
        this.nomor_hp_pemesan = nomor_hp_pemesan;
        this.jumlah_pemesanan = jumlah_pemesanan;
        this.mulai_sewa = mulai_sewa;

    }

    public String getId_pencatatan() {
        return id_pencatatan;
    }
    public String getNama_petugas() {
        return nama_petugas;
    }
    public String getNama_lokasi() {
        return nama_lokasi;
    }
    public String getNama_sepeda() {
        return nama_sepeda;
    }
    public String getNama_pemesan() {
        return nama_pemesan;
    }
    public String getNomor_hp_pemesan() {
        return nomor_hp_pemesan;
    }
    public String getjumlah_pemesan() {
        return jumlah_pemesanan;
    }
    public String getmulai_sewa() {
        return mulai_sewa;
    }
}
