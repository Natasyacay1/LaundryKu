package com.example.laundryapp;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminServiceActivity extends AppCompatActivity {

    private RecyclerView rvServices;
    private TextView tvEmpty, tvBack;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd;
    private ServiceAdapter adapter;
    private List<ServiceModel> serviceList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service);

        dbHelper = DatabaseHelper.getInstance(this);
        rvServices = findViewById(R.id.rvServices);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvBack = findViewById(R.id.tvBack);
        fabAdd = findViewById(R.id.fabAdd);

        rvServices.setLayoutManager(new LinearLayoutManager(this));
        tvBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showServiceDialog(null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServices();
    }

    private void loadServices() {
        serviceList = dbHelper.getAllServices();
        adapter = new ServiceAdapter(serviceList,
                s -> showServiceDialog(s),
                s -> confirmDelete(s));
        rvServices.setAdapter(adapter);
        tvEmpty.setVisibility(serviceList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showServiceDialog(ServiceModel service) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_service);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        EditText etName = dialog.findViewById(R.id.etName);
        EditText etPrice = dialog.findViewById(R.id.etPrice);
        EditText etUnit = dialog.findViewById(R.id.etUnit);
        EditText etDesc = dialog.findViewById(R.id.etDesc);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        boolean isEdit = (service != null);
        tvTitle.setText(isEdit ? "Edit Layanan" : "Tambah Layanan");

        if (isEdit) {
            etName.setText(service.getName());
            etPrice.setText(String.valueOf((int) service.getPrice()));
            etUnit.setText(service.getUnit());
            etDesc.setText(service.getDescription());
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (TextUtils.isEmpty(name)) { etName.setError("Nama wajib diisi"); return; }
            if (TextUtils.isEmpty(priceStr)) { etPrice.setError("Harga wajib diisi"); return; }
            if (TextUtils.isEmpty(unit)) { etUnit.setError("Satuan wajib diisi"); return; }

            double price = Double.parseDouble(priceStr);

            if (isEdit) {
                service.setName(name);
                service.setPrice(price);
                service.setUnit(unit);
                service.setDescription(desc);
                dbHelper.updateService(service);
                Toast.makeText(this, "Layanan diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                ServiceModel newService = new ServiceModel(name, price, unit, desc);
                dbHelper.addService(newService);
                Toast.makeText(this, "Layanan ditambahkan", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            loadServices();
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void confirmDelete(ServiceModel service) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Layanan")
                .setMessage("Hapus layanan \"" + service.getName() + "\"?")
                .setPositiveButton("Hapus", (d, w) -> {
                    dbHelper.deleteService(service.getId());
                    Toast.makeText(this, "Layanan dihapus", Toast.LENGTH_SHORT).show();
                    loadServices();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Inner Adapter
    static class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.VH> {
        private List<ServiceModel> list;
        private java.util.function.Consumer<ServiceModel> onEdit, onDelete;

        ServiceAdapter(List<ServiceModel> list,
                       java.util.function.Consumer<ServiceModel> onEdit,
                       java.util.function.Consumer<ServiceModel> onDelete) {
            this.list = list;
            this.onEdit = onEdit;
            this.onDelete = onDelete;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_service, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int pos) {
            ServiceModel s = list.get(pos);
            holder.tvName.setText(s.getName());
            holder.tvPrice.setText(s.getFormattedPrice());
            holder.tvDesc.setText(s.getDescription() != null ? s.getDescription() : "");
            holder.btnEdit.setOnClickListener(v -> onEdit.accept(s));
            holder.btnDelete.setOnClickListener(v -> onDelete.accept(s));
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvPrice, tvDesc;
            ImageButton btnEdit, btnDelete;
            VH(View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
                tvPrice = v.findViewById(R.id.tvPrice);
                tvDesc = v.findViewById(R.id.tvDesc);
                btnEdit = v.findViewById(R.id.btnEdit);
                btnDelete = v.findViewById(R.id.btnDelete);
            }
        }
    }
}
