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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SimpanDataStok extends AppCompatActivity {

    private EditText uploadNama, uploadJumlah;
    private Button saveButton;
    private Spinner spinnerNama;
    private ArrayList<String> namaList;
    private Map<String, String> namaToIdMap;
    private ImageView gambarTanggal;
    private String idKategori;
    private ArrayAdapter<String> adapterNama;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_stok);

        // Initialize UI components
        uploadNama = findViewById(R.id.uploadnama);
        spinnerNama = findViewById(R.id.spinnernama);
        uploadJumlah = findViewById(R.id.uploadjumlah);
        saveButton = findViewById(R.id.saveButton);

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
                    Toast.makeText(SimpanDataStok.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
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
                simpanData();
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
                    adapterNama = new ArrayAdapter<>(SimpanDataStok.this, android.R.layout.simple_spinner_item, namaList);
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

    private void simpanData() {
        final String nama = uploadNama.getText().toString().trim();
        final String jumlah = uploadJumlah.getText().toString().trim();

        // Validate input
        if (nama.isEmpty() || jumlah.isEmpty()) {
            Toast.makeText(SimpanDataStok.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the selected name already exists in the table jumlah hutang
        String idKategori = namaToIdMap.get(nama);
        if (idKategori == null) {
            Toast.makeText(SimpanDataStok.this, "Nama hutang tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL for the PHP script to save data
        String url = new Configurasi().baseUrl() + "stok/tambah.php";

        // Create a new request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SimpanData", "Response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if (status.equals("error")) {
                                Toast.makeText(SimpanDataStok.this, nama + " tidak ditemukan!", Toast.LENGTH_SHORT).show();
                            } else if (status.equals("success")) {
                                Toast.makeText(SimpanDataStok.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
//                                sendRefreshFlag(true);
                                finish();
                            } else {
                                Toast.makeText(SimpanDataStok.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("SimpanData", "JSON parsing error: " + e.getMessage());
                            Toast.makeText(SimpanDataStok.this, "Gagal memproses data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataStok.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_sepeda", nama);  // Perbaiki key menjadi "nama_sepeda"
                params.put("jumlah", jumlah);
                Log.d("SimpanData", "Params: " + params.toString());
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}