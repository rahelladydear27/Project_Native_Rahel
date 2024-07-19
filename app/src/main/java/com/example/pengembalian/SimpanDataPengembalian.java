package com.example.pengembalian;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpanDataPengembalian extends AppCompatActivity {

    private EditText uploadNama_pemesan, uploadNama_sepeda, uploadJenis_pembayaran, uploadNama_petugas, uploadHarga;
    private Button saveButton;
    private Spinner spinnerNama_pemesan, spinnerNama_sepeda, spinnerJenis_pembayaran, spinnerNama_petugas;
    private ArrayList<String> nama_pemesanList, nama_sepedaList, jenis_pembayaranList, nama_petugasList;
    private Map<String, String> namaToIdMap;

    private ArrayAdapter<String> adapterNama_pemesan, adapterNama_sepeda, adapterJenis_pembayaran, adapterNama_petugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengembalian);

        // Initialize UI components
        uploadNama_pemesan = findViewById(R.id.pemesan);
        spinnerNama_pemesan = findViewById(R.id.spinner1);
        uploadNama_sepeda = findViewById(R.id.sepeda);
        spinnerNama_sepeda = findViewById(R.id.spinner2);
        uploadJenis_pembayaran = findViewById(R.id.pembayaran);
        spinnerJenis_pembayaran = findViewById(R.id.spinner3);
        uploadNama_petugas = findViewById(R.id.petugas);
        spinnerNama_petugas = findViewById(R.id.spinner4);
        uploadHarga = findViewById(R.id.harga);
        saveButton = findViewById(R.id.saveButton);

        // Fetch data from the database for spinnerNama and spinnerJenisBayar
        nama_pemesanList = new ArrayList<>();
        nama_sepedaList = new ArrayList<>();
        jenis_pembayaranList = new ArrayList<>();
        nama_petugasList = new ArrayList<>();

        namaToIdMap = new HashMap<>();


        fetchPencatatanData();
        fetchStokData();
        fetchMetode_PembayaranData();
        fetchPetugasData();

        // Spinner item selected listener for nama
        spinnerNama_pemesan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadNama_pemesan.setText(item);
//                    Toast.makeText(SimpanDataPengembalian.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(SimpanDataPengembalian.this, "Nama Hutang: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerJenis_pembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadJenis_pembayaran.setText(item);
//                    Toast.makeText(SimpanDataPengembalian.this, "Nama Sepeda: " + item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerNama_petugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    String item = adapterView.getItemAtPosition(position).toString();
                    uploadNama_petugas.setText(item);
//                    Toast.makeText(SimpanDataPengembalian.this, "Petugas: " + item, Toast.LENGTH_SHORT).show();
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
        final String nama = uploadNama_pemesan.getText().toString().trim();
        final String nama_sepeda = uploadNama_sepeda.getText().toString().trim();
        final String jenis_pembayaran = uploadJenis_pembayaran.getText().toString().trim();
        final String nama_petugas = uploadNama_petugas.getText().toString().trim();
        final String harga = uploadHarga.getText().toString().trim();

        // URL untuk skrip PHP yang akan menyimpan data
        String url = new Configurasi().baseUrl() + "pengembalian/tambah.php";

        // Buat request baru
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani respons dari server
                        Log.d("SimpanData", "Response: " + response);
                        Toast.makeText(SimpanDataPengembalian.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan
                        Log.e("SimpanData", "Error: " + error.getMessage());
                        Toast.makeText(SimpanDataPengembalian.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Buat parameter untuk request
                Map<String, String> params = new HashMap<>();
                params.put("nama_pemesan", nama);
                params.put("nama_sepeda", nama_sepeda);
                params.put("jenis_pembayaran", jenis_pembayaran);
                params.put("nama_petugas", nama_petugas);  // Perbaiki key menjadi "nama_pemesan"
                params.put("harga", harga);
                params.put("waktu_pengembalian","");
                Log.d("SimpanData", "Params: " + params.toString());
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void fetchPencatatanData() {
        String url = new Configurasi().baseUrl() + "pencatatan/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    nama_pemesanList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_pencatatan");
                        String nama_pemesan = jsonObject.optString("nama_pemesan");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama_pemesan);
                        nama_pemesanList.add(nama_pemesan);
                    }
                    adapterNama_pemesan = new ArrayAdapter<>(SimpanDataPengembalian.this, android.R.layout.simple_spinner_item, nama_pemesanList);
                    adapterNama_pemesan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNama_pemesan.setAdapter(adapterNama_pemesan);
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

    private void fetchStokData() {
        String url = new Configurasi().baseUrl() + "stok/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    nama_sepedaList.add("Pilih Nama");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.optString("id_sepeda");
                        String nama_sepeda = jsonObject.optString("nama_sepeda");
                        Log.d("FetchNamaData", "ID: " + id + ", Nama: " + nama_sepeda);
                        nama_sepedaList.add(nama_sepeda);
                    }
                    adapterNama_sepeda = new ArrayAdapter<>(SimpanDataPengembalian.this, android.R.layout.simple_spinner_item, nama_sepedaList);
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

    private void fetchMetode_PembayaranData() {
        String url = new Configurasi().baseUrl() + "metode_pembayaran/tampil.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    jenis_pembayaranList.add("Pilih Jenis_pembayaran");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String id = jsonObject.optString("id_pencatatan");
                        String nama_jenis = jsonObject.optString("jenis_pembayaran");
                        jenis_pembayaranList.add(nama_jenis);
                    }
                    adapterJenis_pembayaran = new ArrayAdapter<>(SimpanDataPengembalian.this, android.R.layout.simple_spinner_item, jenis_pembayaranList);
                    adapterJenis_pembayaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerJenis_pembayaran.setAdapter(adapterJenis_pembayaran);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("FetchNamaData", "JSON parsing error: " + e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchNamaData", "Volley error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchPetugasData() {
            String url = new Configurasi().baseUrl() + "petugas/tampil.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        nama_petugasList.add("Pilih Petugas");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.optString("id_petugas");
                            String nama_petugas = jsonObject.optString("nama_petugas");
                            nama_petugasList.add(nama_petugas);
                        }
                        adapterNama_petugas = new ArrayAdapter<>(SimpanDataPengembalian.this, android.R.layout.simple_spinner_item, nama_petugasList);
                        adapterNama_petugas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerNama_petugas.setAdapter(adapterNama_petugas);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("FetchNamaData", "JSON parsing error: " + e.getMessage());
                    }
                }
            },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FetchNamaData", "Volley error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}