package com.example.stok;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;
import com.example.kategori.EditDataKategori;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditDataStok extends AppCompatActivity {

    private EditText uploadNama, uploadJumlah;
    private Button saveButton;
    private Spinner spinnerNama;
    private ArrayList<String> namaList;
    private Map<String, String> namaToIdMap;
    private ImageView gambarTanggal;
    private String idKategori;
    private ArrayAdapter<String> adapterNama;
    String id_stok, nama_sepeda, jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stok);

        // Initialize UI components
        uploadNama = findViewById(R.id.uploadnama);
        spinnerNama = findViewById(R.id.spinnernama);
        uploadJumlah = findViewById(R.id.uploadjumlah);
        saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        if (intent != null) {
            id_stok = intent.getStringExtra("id_stok");
            uploadNama.setText(intent.getStringExtra("nama_sepeda"));
            uploadJumlah.setText(intent.getStringExtra("jumlah"));
        }

        // Fetch data from the database for spinnerNama and spinnerJenisBayar
        namaList = new ArrayList<>();
        namaToIdMap = new HashMap<>();


        fetchNamaData();


        // Spinner item selected listener for nama
        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadNama.setText(item);
                    Toast.makeText(EditDataStok.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Set listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void fetchNamaData() {
        String url = new Configurasi().baseUrl() + "kategori/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    namaList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_kategori");
                        String nama = jsonObject.optString("nama_sepeda");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama);
                        namaList.add(nama);
                        namaToIdMap.put(nama, id);
                    }
                    adapterNama = new ArrayAdapter<>(EditDataStok.this, android.R.layout.simple_spinner_item, namaList);
                    adapterNama.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama.setAdapter(adapterNama);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FetchNamaData", "JSON parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FetchNamaData", "Volley error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void updateData() {
        final String IdStok = id_stok;
        final String namaSepeda = uploadNama.getText().toString();
        final String Jumlah = uploadJumlah.getText().toString().trim();

        String url = new Configurasi().baseUrl() + "stok/edit.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("EditDataActivity", "Response: " + response);
                        Toast.makeText(EditDataStok.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        finish(); // Tutup aktivitas
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("EditDataActivity", "Error: " + error.getMessage());
                        Toast.makeText(EditDataStok.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("id_stok", IdStok);
                params.put("nama_sepeda", namaSepeda);
                params.put("jumlah", Jumlah);


                Log.d("EditDataActivity", "Params: " + params.toString()); // Log parameter untuk debugging
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}