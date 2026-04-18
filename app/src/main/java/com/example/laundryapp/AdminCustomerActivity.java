package com.example.laundryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminCustomerActivity extends AppCompatActivity {

    private RecyclerView rvCustomers;
    private TextView tvEmpty, tvBack;
    private CustomerAdapter adapter;
    private List<UserModel> customerList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer);

        dbHelper = DatabaseHelper.getInstance(this);
        rvCustomers = findViewById(R.id.rvCustomers);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvBack = findViewById(R.id.tvBack);

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        tvBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
    }

    private void loadCustomers() {
        customerList = dbHelper.getAllCustomers();
        adapter = new CustomerAdapter(customerList, this::showCustomerOptions);
        rvCustomers.setAdapter(adapter);
        tvEmpty.setVisibility(customerList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showCustomerOptions(UserModel user) {
        String[] options = {"Lihat Detail", "Lihat Riwayat Order", "Hapus Pelanggan"};
        new AlertDialog.Builder(this)
                .setTitle(user.getName())
                .setItems(options, (d, which) -> {
                    if (which == 0) showCustomerDetail(user);
                    else if (which == 1) showCustomerOrders(user);
                    else confirmDeleteCustomer(user);
                })
                .show();
    }

    private void showCustomerDetail(UserModel user) {
        String detail = "Nama: " + user.getName() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "HP: " + (user.getPhone() != null ? user.getPhone() : "-") + "\n" +
                "Alamat: " + (user.getAddress() != null ? user.getAddress() : "-");
        new AlertDialog.Builder(this)
                .setTitle("Detail Pelanggan")
                .setMessage(detail)
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showCustomerOrders(UserModel user) {
        List<OrderModel> orders = dbHelper.getOrdersByUser(user.getId());
        if (orders.isEmpty()) {
            Toast.makeText(this, "Pelanggan belum punya order", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (OrderModel o : orders) {
            sb.append("Order #").append(o.getId()).append("\n");
            sb.append("Layanan: ").append(o.getServiceName()).append("\n");
            sb.append("Total: ").append(o.getFormattedTotal()).append("\n");
            sb.append("Status: ").append(o.getStatus()).append("\n\n");
        }
        new AlertDialog.Builder(this)
                .setTitle("Riwayat Order - " + user.getName())
                .setMessage(sb.toString().trim())
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void confirmDeleteCustomer(UserModel user) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Pelanggan")
                .setMessage("Hapus " + user.getName() + "? Semua order terkait juga akan terhapus.")
                .setPositiveButton("Hapus", (d, w) -> {
                    dbHelper.deleteUser(user.getId());
                    Toast.makeText(this, "Pelanggan dihapus", Toast.LENGTH_SHORT).show();
                    loadCustomers();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Inner Adapter
    static class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.VH> {
        private List<UserModel> list;
        private java.util.function.Consumer<UserModel> onClick;

        CustomerAdapter(List<UserModel> list, java.util.function.Consumer<UserModel> onClick) {
            this.list = list;
            this.onClick = onClick;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_customer, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int pos) {
            UserModel u = list.get(pos);
            holder.tvName.setText(u.getName());
            holder.tvEmail.setText(u.getEmail());
            holder.tvPhone.setText(u.getPhone() != null ? u.getPhone() : "-");
            holder.itemView.setOnClickListener(v -> onClick.accept(u));
        }

        @Override
        public int getItemCount() { return list.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvEmail, tvPhone;
            VH(View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
                tvEmail = v.findViewById(R.id.tvEmail);
                tvPhone = v.findViewById(R.id.tvPhone);
            }
        }
    }
}
