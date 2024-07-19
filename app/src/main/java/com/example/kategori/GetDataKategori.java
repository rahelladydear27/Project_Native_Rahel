package com.example.kategori;

import java.io.Serializable;

public class GetDataKategori implements Serializable {
    private String id_kategori;
    private String nama_sepeda;
    private String kapasitas;



    public GetDataKategori(String id_kategori, String nama_sepeda, String kapasitas) {
        this.id_kategori = id_kategori;
        this.nama_sepeda = nama_sepeda;
        this.kapasitas = kapasitas;


    }

    public String getId_kategori() {
        return id_kategori;
    }

    public String getNama_sepeda() {
        return nama_sepeda;
    }

    public String getKapasitas() {
        return kapasitas;
    }



}
