package com.example.laundryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminReportActivity extends AppCompatActivity {

    private TextView tvBack, tvTotalOrders, tvTotalRevenue, tvPendingCount,
            tvDiprosesCount, tvSelesaiCount, tvDiambilCount, tvEmpty;
    private RecyclerView rvReport;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        dbHelper = DatabaseHelper.getInstance(this);

        tvBack = findViewById(R.id.tvBack);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvDiprosesCount = findViewById(R.id.tvDiprosesCount);
        tvSelesaiCount = findViewById(R.id.tvSelesaiCount);
        tvDiambilCount = findViewById(R.id.tvDiambilCount);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvReport = findViewById(R.id.rvReport);

        tvBack.setOnClickListener(v -> finish());

        rvReport.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReport();
    }

    private void loadReport() {
        List<OrderModel> orders = dbHelper.getAllOrders();

        int pending = 0, diproses = 0, selesai = 0, diambil = 0;
        for (OrderModel o : orders) {
            switch (o.getStatus()) {
                case "Pending": pending++; break;
                case "Diproses": diproses++; break;
                case "Selesai": selesai++; break;
                case "Diambil": diambil++; break;
            }
        }

        tvTotalOrders.setText(String.valueOf(orders.size()));
        tvTotalRevenue.setText("Rp " + String.format("%,.0f", dbHelper.getTotalRevenue()));
        tvPendingCount.setText(String.valueOf(pending));
        tvDiprosesCount.setText(String.valueOf(diproses));
        tvSelesaiCount.setText(String.valueOf(selesai));
        tvDiambilCount.setText(String.valueOf(diambil));

        OrderAdapter adapter = new OrderAdapter(orders, null, false);
        rvReport.setAdapter(adapter);
        tvEmpty.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
