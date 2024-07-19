package com.example.eldestapp;

import java.io.Serializable;

public class GetData implements Serializable {
    private String id_admin;
    private String nama;
    private String nomor_hp;
    private String alamat;


    public GetData(String id_admin, String nama, String nomor_hp, String alamat) {
        this.id_admin = id_admin;
        this.nama = nama;
        this.nomor_hp = nomor_hp;
        this.alamat = alamat;

    }

    public String getId_admin() {
        return id_admin;
    }

    public String getNama() {
        return nama;
    }

    public String getNomor_hp() {
        return nomor_hp;
    }

    public String getAlamat() {
        return alamat;
    }

}
