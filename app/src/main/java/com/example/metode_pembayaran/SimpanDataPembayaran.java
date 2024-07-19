package com.example.metode_pembayaran;

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

public class SimpanDataPembayaran extends AppCompatActivity {

    private EditText uploadNama, alamat;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);

        // Inisialisasi komponen UI
        uploadNama = findViewById(R.id.uploadnama);
        alamat = findViewById(R.id.uploadkapasitas);

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
        final String jenis_pembayaran = uploadNama.getText().toString().trim();
        final String keterangan = alamat.getText().toString().trim();


        // Validasi input
        if (jenis_pembayaran.isEmpty()|| keterangan.isEmpty()) {
            Toast.makeText(SimpanDataPembayaran.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "metode_pembayaran/tambah.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(SimpanDataPembayaran.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Send refresh flag to indicate data change
                        finish(); // Tutup aktivitas setelah sukses menyimpan data
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataPembayaran.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("jenis_pembayaran", jenis_pembayaran);
                params.put("keterangan", keterangan);

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