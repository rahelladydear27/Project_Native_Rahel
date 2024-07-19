package com.example.eldestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kategori.ActivityKategori;
import com.example.lokasi.ActivityLokasi;
import com.example.metode_pembayaran.ActivityMetodePembayaran;
import com.example.petugas.ActivityPetugas;
import com.example.sepeda.Activity_sepeda;

public class MenuLainnya extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_master);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout petugas = findViewById(R.id.framepetugas);

        petugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityPetugas.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout kategori = findViewById(R.id.frameLayout2);

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityKategori.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout lokasi = findViewById(R.id.framelokasi);

        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityLokasi.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout metode_pembayaran = findViewById(R.id.framepembayaran);

        metode_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMetodePembayaran.class);
                startActivity(intent);
            }
        });


    }
}