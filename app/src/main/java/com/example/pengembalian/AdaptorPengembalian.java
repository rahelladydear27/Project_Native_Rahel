package com.example.pengembalian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eldestapp.Configurasi;
import com.example.eldestapp.R;
import com.example.pengembalian.GetDataPengembalian;
import com.example.sepeda.EditDataSepeda;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorPengembalian extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPengembalian> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPengembalian(Context context, ArrayList<GetDataPengembalian> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
        this.context = context;
        this.model = model;
        this.progressBar = progressBar;
        this.dataChangeListener = dataChangeListener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_pengembalian, parent, false);
            holder = new ViewHolder();
            holder.nama_pemesan = convertView.findViewById(R.id.nama1);
            holder.nama_petugas= convertView.findViewById(R.id.nama2);
            holder.nama_sepeda= convertView.findViewById(R.id.nama3);
            holder.jenis_pembayaran= convertView.findViewById(R.id.nama4);
            holder.waktu_pengembalian= convertView.findViewById(R.id.nama5);
            holder.harga= convertView.findViewById(R.id.nama6);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPengembalian data = model.get(position);
        holder.nama_pemesan.setText(data.getNama_pemesan());
        holder.nama_petugas.setText(data.getNama_petugas());
        holder.nama_sepeda.setText(data.getNama_sepeda());
        holder.jenis_pembayaran.setText(data.getJenis_pembayaran());
        holder.waktu_pengembalian.setText(data.getWaktu_pengembalian());
        holder.harga.setText(String.valueOf(data.getHarga()));// Assuming getjumlah returns an integer

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDataPengembalian.class);
                intent.putExtra("id_pengembalian", data.getId_pengembalian());
                intent.putExtra("nama_pemesan", data.getNama_pemesan());
                intent.putExtra("nama_petugas", data.getNama_petugas());
                intent.putExtra("nama_sepeda", data.getNama_sepeda());
                intent.putExtra("jenis_pembayaran", data.getJenis_pembayaran());
                intent.putExtra("waktu_pengembalian", data.getWaktu_pengembalian());
                intent.putExtra("harga", String.valueOf(data.getHarga()));
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(String.valueOf(data.getId_pengembalian()), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_pengembalian, final int position) {
        String url = new Configurasi().baseUrl() + "pengembalian/hapus.php";

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
                                notifyDataSetChanged();
                                // Notify data change
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            } else {
                                Toast.makeText(context, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                                // Reload data even if deletion fails
                                if (dataChangeListener != null) {
                                    dataChangeListener.onDataChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error parsing response", Toast.LENGTH_SHORT).show();
                            // Reload data even if deletion fails
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
                        // Reload data even if deletion fails
                        if (dataChangeListener != null) {
                            dataChangeListener.onDataChanged();
                        }
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> form = new HashMap<>();
                form.put("id_pengembalian", id_pengembalian);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama_pemesan, nama_petugas, nama_sepeda, jenis_pembayaran, waktu_pengembalian, harga;
        ImageButton editButton, deleteButton;
    }
}
