package com.example.metode_pembayaran;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class EditDataMetodePembayaran extends AppCompatActivity {

    private EditText editjenis_pembayaran, editketerangan;
    private Button updateButton;
    private String id_pembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lokasi);

        editjenis_pembayaran = findViewById(R.id.uploadnama);
        editketerangan=findViewById(R.id.uploadalamat);


        updateButton = findViewById(R.id.editbutton);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_pembayaran = intent.getStringExtra("id_pembayaran");
            editjenis_pembayaran.setText(intent.getStringExtra("jenis_pembayaran"));
            editketerangan.setText(intent.getStringExtra("keterangan"));
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateData();
            }
        });
    }

    private void updateData() {
        final String jenis_pembayaran = editjenis_pembayaran.getText().toString().trim();
        final String keterangan = editketerangan.getText().toString().trim();



        // Validasi input
        if (jenis_pembayaran.isEmpty() || keterangan.isEmpty()) {
            Toast.makeText(EditDataMetodePembayaran.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "metode_pembayaran/edit.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivity", "Response: " + response);
                        Toast.makeText(EditDataMetodePembayaran.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivity", "Error: " + error.getMessage());
                        Toast.makeText(EditDataMetodePembayaran.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", id_pembayaran);
                params.put("jenis_pembayaran", jenis_pembayaran);
                params.put("keterangan", keterangan);


                Log.d("EditDataActivity", "Params: " + params.toString()); // Log parameter untuk debugging
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