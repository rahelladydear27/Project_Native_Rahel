package com.example.pencatatan;

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
import com.example.sepeda.EditDataSepeda;
import com.example.pencatatan.GetDataPencatatan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdaptorPencatatan extends BaseAdapter {
    private Context context;
    private ArrayList<GetDataPencatatan> model;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DataChangeListener dataChangeListener;

    public interface DataChangeListener {
        void onDataChanged();
    }

    public AdaptorPencatatan(Context context, ArrayList<GetDataPencatatan> model, ProgressBar progressBar, DataChangeListener dataChangeListener) {
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
            convertView = inflater.inflate(R.layout.activity_list_pencatatan, parent, false);
            holder = new ViewHolder();
            holder.id_pencatatan = convertView.findViewById(R.id.Id_pencatatan);
            holder.nama_petugas = convertView.findViewById(R.id.nama1);
            holder.nama_lokasi= convertView.findViewById(R.id.nama2);
            holder.nama_sepeda= convertView.findViewById(R.id.nama3);
            holder.nama_pemesan= convertView.findViewById(R.id.nama4);
            holder.nomor_hp_pemesan= convertView.findViewById(R.id.nama5);
            holder.jumlah_pemesanan= convertView.findViewById(R.id.nama6);
            holder.mulai_sewa=convertView.findViewById(R.id.nama7);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GetDataPencatatan data = model.get(position);
        holder.id_pencatatan.setText(data.getId_pencatatan());
        holder.nama_petugas.setText(data.getNama_petugas());
        holder.nama_lokasi.setText(data.getNama_lokasi());
        holder.nama_sepeda.setText(data.getNama_sepeda());
        holder.nama_pemesan.setText(data.getNama_pemesan());
        holder.nomor_hp_pemesan.setText(data.getNomor_hp_pemesan());
        holder.jumlah_pemesanan.setText(String.valueOf(data.getjumlah_pemesan()));
        holder.mulai_sewa.setText(data.getmulai_sewa());// Assuming getjumlah returns an integer

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDataPencatatan.class);
                intent.putExtra("id_pencatatan", data.getId_pencatatan());
                intent.putExtra("nama_petugas", data.getNama_petugas());
                intent.putExtra("nama_lokasi", data.getNama_lokasi());
                intent.putExtra("nama_sepeda", data.getNama_sepeda());
                intent.putExtra("nama_pemesan", data.getNama_pemesan());
                intent.putExtra("nomor_hp_pemesan", data.getNomor_hp_pemesan());
                intent.putExtra("jumlah_pemesanan", data.getjumlah_pemesan());
                intent.putExtra("mulai_sewa", data.getmulai_sewa());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(String.valueOf(data.getId_pencatatan()), position);
            }
        });

        return convertView;
    }

    public void reloadData() {
        notifyDataSetChanged();
    }

    private void deleteData(final String id_pencatatan, final int position) {
        String url = new Configurasi().baseUrl() + "pencatatan/hapus.php";

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
                form.put("id_pencatatan", id_pencatatan);
                return form;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private static class ViewHolder {
        TextView nama_sepeda, nama_petugas, nama_lokasi, nama_pemesan, nomor_hp_pemesan,jumlah_pemesanan, mulai_sewa, id_pencatatan;
        ImageButton editButton, deleteButton;
    }
}
