package com.example.petugas;

import java.io.Serializable;

public class GetDataPetugas implements Serializable {
    private String id_petugas;
    private String nama_petugas;
    private String alamat;
    private String nomor_hp;



    public GetDataPetugas(String id_petugas, String nama_petugas, String alamat, String nomor_hp) {
        this.id_petugas = id_petugas;
        this.nama_petugas = nama_petugas;
        this.alamat = alamat;
        this.nomor_hp = nomor_hp;


    }

    public String getId_petugas() {
        return id_petugas;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public String getAlamat() {
        return alamat;
    }
    public String getNomor_hp() {
        return nomor_hp;
    }



}
