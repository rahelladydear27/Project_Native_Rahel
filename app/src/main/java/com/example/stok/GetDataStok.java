package com.example.stok;

import java.io.Serializable;

public class GetDataStok implements Serializable {
    private String id_stok;
    private String nama_sepeda;
    private int jumlah;

    public GetDataStok(String id_stok, String nama_sepeda, Integer jumlah) {
        this.id_stok = id_stok;
        this.nama_sepeda = nama_sepeda;
        this.jumlah = jumlah;

    }

    public String getid_stok() {
        return id_stok;
    }
    public String getnama_sepeda() {
        return nama_sepeda;
    }
    public Integer getjumlah() {
        return jumlah;
    }
}
