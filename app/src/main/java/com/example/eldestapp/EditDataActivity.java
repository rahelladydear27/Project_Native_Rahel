package com.example.eldestapp;


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

import java.util.HashMap;
import java.util.Map;

public class EditDataActivity extends AppCompatActivity {

    private EditText editNama, editAlamat, editNomorHp, editJumlahHutang;
    private Button updateButton;
    private String id_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin);

        editNama = findViewById(R.id.uploadnama);
        editNomorHp = findViewById(R.id.uploadnomor_hp);
        editAlamat = findViewById(R.id.uploadalamat);


        updateButton = findViewById(R.id.editbutton);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        if (intent != null) {
            id_admin = intent.getStringExtra("id_admin");
            editNama.setText(intent.getStringExtra("nama"));
            editNomorHp.setText(intent.getStringExtra("nomor_hp"));
            editAlamat.setText(intent.getStringExtra("alamat"));

        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateData();
            }
        });
    }

    private void updateData() {
        final String nama = editNama.getText().toString().trim();
        final String nomorHp = editNomorHp.getText().toString().trim();
        final String alamat = editAlamat.getText().toString().trim();



        // Validasi input
        if (nama.isEmpty() || alamat.isEmpty() || nomorHp.isEmpty()) {
            Toast.makeText(EditDataActivity.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL untuk skrip PHP yang akan melakukan update data
        String url = new Configurasi().baseUrl() + "admin/edit.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivity", "Response: " + response);
                        Toast.makeText(EditDataActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        sendRefreshFlag(true); // Mengirim flag refresh untuk memuat ulang data
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivity", "Error: " + error.getMessage());
                        Toast.makeText(EditDataActivity.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_admin", id_admin);
                params.put("nama", nama);
                params.put("nomor_hp", nomorHp);
                params.put("alamat", alamat);


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