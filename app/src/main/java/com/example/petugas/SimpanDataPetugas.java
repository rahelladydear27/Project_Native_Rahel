package com.example.petugas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;

import java.util.HashMap;
import java.util.Map;

public class SimpanDataPetugas extends AppCompatActivity {

    private EditText uploadNama, alamat, nomor_hp;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_petugas);

        // Inisialisasi komponen UI
        uploadNama = findViewById(R.id.uploadnama);
        alamat = findViewById(R.id.uploadalamat);
        nomor_hp = findViewById(R.id.uploadnomor_hp);

        saveButton = findViewById(R.id.saveButton);

        // Setel listener tombol simpan
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void simpanData() {
        // Ambil input pengguna
        final String nama_petugas = uploadNama.getText().toString().trim();
        final String alamat_petugas = alamat.getText().toString().trim();
        final String no_hp = nomor_hp.getText().toString().trim();


        // Validasi input
        if (nama_petugas.isEmpty()|| alamat_petugas.isEmpty()|| no_hp.isEmpty()) {
            Toast.makeText(SimpanDataPetugas.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "petugas/tambah.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(SimpanDataPetugas.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataPetugas.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_petugas", nama_petugas);
                params.put("alamat", alamat_petugas);
                params.put("nomor_hp", no_hp);

                Log.d("SimpanData", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void sendRefreshFlag(boolean refresh) {
        Intent intent = new Intent();
        intent.putExtra("refreshflag", refresh);
        setResult(RESULT_OK, intent);
    }
}