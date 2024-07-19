package com.example.pengembalian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;
import com.example.pengembalian.AdaptorPengembalian;
import com.example.pengembalian.GetDataPengembalian;
import com.example.sepeda.SimpanDataSepeda;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_pengembalian extends AppCompatActivity implements AdaptorPengembalian.DataChangeListener {
    private static final int REQUEST_CODE_SIMPAN_DATA = 1;
    private static final int EDIT_DATA_REQUEST = 2;
    private static final int RELOAD_DELAY_MS = 1000; // 1 second delay
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<GetDataPengembalian> model;
    private AdaptorPengembalian adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembalian);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.tambah_data);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_pengembalian.this, SimpanDataPengembalian.class); // Start SimpanData activity
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
        String url = new Configurasi().baseUrl() +"pengembalian/tampil.php";
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("Activity_sepeda", "Response received: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("data")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                model = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject getData = jsonArray.getJSONObject(i);
                                    model.add(new GetDataPengembalian(
                                            getData.getString("id_pengembalian"),
                                            getData.getString("nama_pemesan"),
                                            getData.getString("nama_petugas"),
                                            getData.getString("nama_sepeda"),
                                            getData.getString("jenis_pembayaran"),
                                            getData.getString("waktu_pengembalian"),
                                            getData.getInt("harga")
                                    ));
                                }

                                adaptor = new AdaptorPengembalian(Activity_pengembalian.this, model, progressBar, Activity_pengembalian.this);
                                listView.setAdapter(adaptor);
                                Log.d("Activity_admin", "Data loaded successfully");
                            } else {
                                Log.e("Activity_admin", "JSON does not contain 'data' key");
                                Toast.makeText(Activity_pengembalian.this, "Invalid data format", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Activity_admin", "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(Activity_pengembalian.this, "Error parsing data", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Activity_pengembalian.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
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
