package com.example.laundryapp;
import android.content.Intent; import android.os.Bundle; import android.widget.*;
import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    private TextView tvAdminName,tvTotalOrders,tvPendingOrders,tvRevenue,tvTotalCustomers;
    private LinearLayout cardOrders,cardCustomers,cardServices,cardReport,cardPromo,cardAdmins,btnLogout;
    private DatabaseHelper dbHelper; private SessionManager sessionManager;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s); setContentView(R.layout.activity_admin_dashboard);
        dbHelper=DatabaseHelper.getInstance(this); sessionManager=new SessionManager(this);
        tvAdminName=findViewById(R.id.tvAdminName); tvTotalOrders=findViewById(R.id.tvTotalOrders);
        tvPendingOrders=findViewById(R.id.tvPendingOrders); tvRevenue=findViewById(R.id.tvRevenue);
        tvTotalCustomers=findViewById(R.id.tvTotalCustomers); cardOrders=findViewById(R.id.cardOrders);
        cardCustomers=findViewById(R.id.cardCustomers); cardServices=findViewById(R.id.cardServices);
        cardReport=findViewById(R.id.cardReport); cardPromo=findViewById(R.id.cardPromo);
        cardAdmins=findViewById(R.id.cardAdmins); btnLogout=findViewById(R.id.btnLogout);
        tvAdminName.setText("Halo, "+sessionManager.getUserName()+" 👋");
        cardOrders.setOnClickListener(v->startActivity(new Intent(this,AdminOrderActivity.class)));
        cardCustomers.setOnClickListener(v->startActivity(new Intent(this,AdminCustomerActivity.class)));
        cardServices.setOnClickListener(v->startActivity(new Intent(this,AdminServiceActivity.class)));
        cardReport.setOnClickListener(v->startActivity(new Intent(this,AdminReportActivity.class)));
        cardPromo.setOnClickListener(v->startActivity(new Intent(this,AdminPromoActivity.class)));
        cardAdmins.setOnClickListener(v->startActivity(new Intent(this,AdminManageAdminActivity.class)));
        btnLogout.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Logout").setMessage("Yakin keluar?").setPositiveButton("Ya",(d,w)->{sessionManager.logout();startActivity(new Intent(this,LoginActivity.class));finish();}).setNegativeButton("Batal",null).show());
    }
    @Override protected void onResume(){super.onResume();loadStats();}
    private void loadStats(){
        tvTotalOrders.setText(String.valueOf(dbHelper.getTotalOrders()));
        tvPendingOrders.setText(String.valueOf(dbHelper.getPendingOrders()));
        tvRevenue.setText("Rp "+String.format("%,.0f",dbHelper.getTotalRevenue()));
        tvTotalCustomers.setText(String.valueOf(dbHelper.getTotalCustomers()));
    }
}
