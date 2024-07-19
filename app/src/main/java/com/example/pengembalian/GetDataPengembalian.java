package com.example.pengembalian;

import java.io.Serializable;

public class GetDataPengembalian implements Serializable {
    private String id_pengembalian;
    private String nama_pemesan;
    private String nama_petugas;
    private String nama_sepeda;
    private String jenis_pembayaran;
    private String waktu_pengembalian;
    private Integer harga;

    public GetDataPengembalian(String id_pengembalian,
                               String nama_pemesan,
                               String nama_petugas,
                               String nama_sepeda,
                               String jenis_pembayaran,
                               String waktu_pengembalian,
                               Integer harga) {
        this.id_pengembalian = id_pengembalian;
        this.nama_pemesan = nama_pemesan;
        this.nama_petugas = nama_petugas;
        this.nama_sepeda = nama_sepeda;
        this.jenis_pembayaran = jenis_pembayaran;
        this.waktu_pengembalian = waktu_pengembalian;
        this.harga = harga;

    }

    public String getId_pengembalian() {
        return id_pengembalian;
    }
    public String getNama_pemesan() {return nama_pemesan;}
    public String getNama_petugas() {
        return nama_petugas;
    }
    public String getNama_sepeda() {
        return nama_sepeda;
    }
    public String getJenis_pembayaran() {
        return jenis_pembayaran;
    }
    public String getWaktu_pengembalian() {
        return waktu_pengembalian;
    }
    public Integer getHarga() {
        return harga;
    }
}
