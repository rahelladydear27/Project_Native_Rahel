package com.example.sepeda;

import java.io.Serializable;

public class GetDataSepeda implements Serializable {
    private String id_sepeda;
    private String nama_sepeda;

    public GetDataSepeda(String id_sepeda, String nama_sepeda) {
        this.id_sepeda = id_sepeda;
        this.nama_sepeda = nama_sepeda;

    }

    public String getid_sepeda() {
        return id_sepeda;
    }

    public String getNama_sepeda() {
        return nama_sepeda;
    }


}
