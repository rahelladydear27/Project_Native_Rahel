package com.example.metode_pembayaran;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.EditDataActivity;
import com.example.eldestapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorMetodePembayaran extends RecyclerView.Adapter<AdaptorMetodePembayaran.ViewHolder> {
    private Context context;
    private ArrayList<GetDataMetodePembayaran> model;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorMetodePembayaran(Context context, ArrayList<GetDataMetodePembayaran> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_pembayaran, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetDataMetodePembayaran data = model.get(position);
        holder.jenis_pembayaran.setText(data.getJenis_pembayaran());
        holder.keterangan.setText(data.getKeterangan());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDataMetodePembayaran.class);
                intent.putExtra("id_pembayaran", data.getId_pembayaran());
                intent.putExtra("jenis_pembayaran", data.getJenis_pembayaran());
                intent.putExtra("keterangan", data.getKeterangan());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_pembayaran(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    private void deleteData(final String id_pembayaran, final int position) {
        String url = new Configurasi().baseUrl() + "metode_pembayaran/hapus.php";

        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if ("data_berhasil_di_hapus".equals(status)) {
                                Toast.makeText(context, "Data successfully deleted", Toast.LENGTH_SHORT).show();
                                model.remove(position);
                                notifyItemRemoved(position);
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            } else {
                                Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                            if (dataChangeListener != null) {
                                dataChangeListener.onDataChanged();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error deleting data", Toast.LENGTH_SHORT).show();
                        if (dataChangeListener != null) {
                            dataChangeListener.onDataChanged();
                        }
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("id_pembayaran", id_pembayaran);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView jenis_pembayaran, keterangan;
        ImageButton editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jenis_pembayaran = itemView.findViewById(R.id.nama);
            keterangan = itemView.findViewById(R.id.addres);
            editButton = itemView.findViewById(R.id.edit);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }
}
