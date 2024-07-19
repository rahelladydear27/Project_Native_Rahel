package com.example.metode_pembayaran;

import java.io.Serializable;

public class GetDataMetodePembayaran implements Serializable {
    private String id_pembayaran;
    private String jenis_pembayaran;
    private String keterangan;



    public GetDataMetodePembayaran(String id_pembayaran, String jenis_pembayaran, String keterangan) {
        this.id_pembayaran = id_pembayaran;
        this.jenis_pembayaran = jenis_pembayaran;
        this.keterangan = keterangan;


    }

    public String getId_pembayaran() {
        return id_pembayaran;
    }

    public String getJenis_pembayaran() {
        return jenis_pembayaran;
    }

    public String getKeterangan() {
        return keterangan;
    }



}
