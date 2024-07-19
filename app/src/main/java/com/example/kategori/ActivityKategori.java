package com.example.kategori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;
import com.example.eldestapp.SimpanData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityKategori extends AppCompatActivity implements AdaptorKategori.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private RecyclerView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataKategori> model;
    private AdaptorKategori adaptor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        listView = findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.tambah_data);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityKategori.this, SimpanDataKategori.class); // Start SimpanData activity
                startActivityForResult(intent, REQUEST_CODE_SIMPAN_DATA);
            }
        });

        loadData(); // Initial data load
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Load data again when the activity resumes
    }

    private void loadData() {
        String url = new Configurasi().baseUrl() +"kategori/tampil.php";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("Activity_admin", "Response received: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("data")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                model = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject getDataKategori = jsonArray.getJSONObject(i);
                                    model.add(new GetDataKategori(
                                            getDataKategori.getString("id_kategori"),
                                            getDataKategori.getString("nama_sepeda"),
                                            getDataKategori.getString("kapasitas")
                                    ));
                                }

                                adaptor = new AdaptorKategori(ActivityKategori.this, model, progressBar, ActivityKategori.this);
                                listView.setAdapter(adaptor);
                                Log.d("Activity_admin", "Data loaded successfully");
                            } else {
                                Log.e("Activity_admin", "JSON does not contain 'data' key");
                                Toast.makeText(ActivityKategori.this, "Invalid data format", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Activity_admin", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(ActivityKategori.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            // Reload data on error
                            reloadData();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Activity_admin", "Volley error: " + volleyError.getMessage());
                        Toast.makeText(ActivityKategori.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                        // Reload data on error
                        reloadData();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onDataChanged() {
        Log.d("Activity_admin", "Data change detected, reloading data");
        reloadData(); // Reload data when data changes
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIMPAN_DATA) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    Log.d("Activity_admin", "Refresh flag received, reloading data...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(); // Reload data
                        }
                    }, RELOAD_DELAY_MS);
                }
            }
        } else if (requestCode == EDIT_DATA_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean refresh = data.getBooleanExtra("refreshflag", false);
                if (refresh) {
                    Log.d("Activity_admin", "Refresh flag received from edit activity, reloading data...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(); // Reload data
                        }
                    }, RELOAD_DELAY_MS);
                }
            }
        }
    }

    private void reloadData() {
        loadData();
    }
}
