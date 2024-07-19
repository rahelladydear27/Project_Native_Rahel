package com.example.eldestapp;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<GetData> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public Adaptor(Context context, ArrayList<GetData> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.activity_list_admin, parent, false);
            holder = new ViewHolder();
            holder.nama = convertView.findViewById(R.id.nama);
            holder.nomorhp = convertView.findViewById(R.id.Phone);
            holder.alamat = convertView.findViewById(R.id.addres);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetData data = model.get(position);
        holder.nama.setText(data.getNama());
        holder.alamat.setText(data.getAlamat());
        holder.nomorhp.setText(data.getNomor_hp());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menjalankan EditDataActivity dengan mengirimkan data yang dibutuhkan
                Intent intent = new Intent(context, EditDataActivity.class);
                intent.putExtra("id_admin", data.getId_admin());
                intent.putExtra("nama", data.getNama());
                intent.putExtra("nomor_hp", data.getAlamat());
                intent.putExtra("alamat", data.getNomor_hp());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(data.getId_admin(), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_admin, final int position) {
        String url = new Configurasi().baseUrl() + "admin/hapus.php";

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
                form.put("id_admin", id_admin);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama, alamat, nomorhp;
        ImageButton editButton, deleteButton;

    }
}
