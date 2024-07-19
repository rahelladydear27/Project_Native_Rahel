package com.example.lokasi;

import java.io.Serializable;

public class GetDataLokasi implements Serializable {
    private String id_lokasi;
    private String nama_lokasi;
    private String alamat;



    public GetDataLokasi(String id_lokasi, String nama_lokasi, String alamat) {
        this.id_lokasi = id_lokasi;
        this.nama_lokasi = nama_lokasi;
        this.alamat = alamat;


    }

    public String getId_lokasi() {
        return id_lokasi;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public String getAlamat() {
        return alamat;
    }



}
