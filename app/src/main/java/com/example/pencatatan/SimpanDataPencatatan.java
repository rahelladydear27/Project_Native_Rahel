package com.example.pencatatan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.metode_pembayaran.SimpanDataPembayaran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpanDataPencatatan extends AppCompatActivity {

    private EditText uploadNama_petugas, uploadNomor_hp_pemesan, uploadJumlah, uploadNama_pemesan, uploadNama_sepeda, uploadnama_lokasi;
    private Button saveButton;
    private Spinner spinnerNama_petugas, spinnerNama_lokasi, spinnerNama_sepeda;
    private ArrayList<String> nama_petugasList, nama_lokasiList, nama_sepedaList;
    private Map<String, String> namaToIdMap;
    private ArrayAdapter<String> adapterNama_petugas, adapterNama_lokasi, adapterNama_sepeda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pencatatan);

        // Initialize UI components
        uploadNama_petugas = findViewById(R.id.uploadnama1);
        spinnerNama_petugas = findViewById(R.id.spinnerPetugas);
        uploadNama_pemesan = findViewById(R.id.uploadpemesan);
        spinnerNama_lokasi = findViewById(R.id.spinnerLokasi);
        uploadnama_lokasi = findViewById(R.id.uploadnama2);
        uploadNama_sepeda = findViewById(R.id.uploadnama3);
        spinnerNama_sepeda = findViewById(R.id.spinnerSepeda);
        uploadNomor_hp_pemesan = findViewById(R.id.uploadnomor_hp);
        uploadJumlah = findViewById(R.id.uploadjumlah);
        saveButton = findViewById(R.id.saveButton);

        // Fetch data from the database for spinnerNama and spinnerJenisBayar
        nama_petugasList = new ArrayList<>();
        nama_lokasiList = new ArrayList<>();
        nama_sepedaList = new ArrayList<>();

        namaToIdMap = new HashMap<>();


        fetchPetugasData();
        fetchLokasiData();
        fetchSepedaData();

        // Spinner item selected listener for nama
        spinnerNama_petugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadNama_petugas.setText(item);
//                    Toast.makeText(SimpanDataPencatatan.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerNama_lokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadnama_lokasi.setText(item);
//                    Toast.makeText(SimpanDataPencatatan.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerNama_sepeda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadNama_sepeda.setText(item);
//                    Toast.makeText(SimpanDataPencatatan.this, "Nama Sepeda: " + item, Toast.LENGTH_SHORT).show();
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

    private void simpanData() {
        // Ambil input pengguna
        final String nama = uploadNama_petugas.getText().toString().trim();
        final String nama_lokasi = uploadnama_lokasi.getText().toString().trim();
        final String nama_sepeda = uploadNama_sepeda.getText().toString().trim();
        final String nama_pemesan = uploadNama_pemesan.getText().toString().trim();
        final String nomor = uploadNomor_hp_pemesan.getText().toString().trim();
        final String jumlah = uploadJumlah.getText().toString().trim();

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "pencatatan/tambah.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(SimpanDataPencatatan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataPencatatan.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_petugas", nama);
                params.put("nama_lokasi", nama_lokasi);
                params.put("nama_sepeda", nama_sepeda);
                params.put("nama_pemesan", nama_pemesan);  // Perbaiki key menjadi "nama_pemesan"
                params.put("nomor_hp_pemesan", nomor);
                params.put("jumlah_pemesanan", jumlah);
                Log.d("SimpanData", "Params: " + params.toString());
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchPetugasData() {
        String url = new Configurasi().baseUrl() + "petugas/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    nama_petugasList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_petugas");
                        String nama = jsonObject.optString("nama_petugas");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama);
                        nama_petugasList.add(nama);
                        namaToIdMap.put(nama, id);
                    }
                    adapterNama_petugas = new ArrayAdapter<>(SimpanDataPencatatan.this, android.R.layout.simple_spinner_item, nama_petugasList);
                    adapterNama_petugas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama_petugas.setAdapter(adapterNama_petugas);
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

    private void fetchLokasiData() {
        String url = new Configurasi().baseUrl() + "lokasi/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    nama_lokasiList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_lokasi");
                        String nama = jsonObject.optString("nama_lokasi");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama);
                        nama_lokasiList.add(nama);
                        namaToIdMap.put(nama, id);
                    }
                    adapterNama_lokasi = new ArrayAdapter<>(SimpanDataPencatatan.this, android.R.layout.simple_spinner_item, nama_lokasiList);
                    adapterNama_lokasi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama_lokasi.setAdapter(adapterNama_lokasi);
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

    private void fetchSepedaData() {
        String url = new Configurasi().baseUrl() + "stok/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    nama_sepedaList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_kategori");
                        String nama = jsonObject.optString("nama_sepeda");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama);
                        nama_sepedaList.add(nama);
                        namaToIdMap.put(nama, id);
                    }
                    adapterNama_sepeda = new ArrayAdapter<>(SimpanDataPencatatan.this, android.R.layout.simple_spinner_item, nama_sepedaList);
                    adapterNama_sepeda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama_sepeda.setAdapter(adapterNama_sepeda);
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

}