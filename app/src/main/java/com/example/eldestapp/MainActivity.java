package com.example.eldestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metode_pembayaran.ActivityMetodePembayaran;
import com.example.pencatatan.Activity_pencatatan;
import com.example.pengembalian.Activity_pengembalian;
import com.example.sepeda.Activity_sepeda;
import com.example.stok.Activity_stok;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout lainnya = findViewById(R.id.framelainnya);

        lainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuLainnya.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout stok = findViewById(R.id.framestok);

        stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_stok.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout pencatatan = findViewById(R.id.framepencatatan);

        pencatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_pencatatan.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) FrameLayout pengembalian= findViewById(R.id.framepengembalian);

        pengembalian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_pengembalian.class);
                startActivity(intent);
            }
        });
    }
}