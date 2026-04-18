package com.example.laundryapp;
import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.*;
import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*; import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {
    private TextView tvGreeting,tvActiveCount,tvNotifBadge;
    private LinearLayout btnNewOrder,btnHistory,btnProfile,btnLogout,btnNotif;
    private RecyclerView rvActiveOrders; private TextView tvNoActive;
    private DatabaseHelper dbHelper; private SessionManager sessionManager;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s); setContentView(R.layout.activity_customer_dashboard);
        dbHelper=DatabaseHelper.getInstance(this); sessionManager=new SessionManager(this);
        tvGreeting=findViewById(R.id.tvGreeting); tvActiveCount=findViewById(R.id.tvActiveCount);
        tvNotifBadge=findViewById(R.id.tvNotifBadge); rvActiveOrders=findViewById(R.id.rvActiveOrders);
        tvNoActive=findViewById(R.id.tvNoActive); btnNewOrder=findViewById(R.id.btnNewOrder);
        btnHistory=findViewById(R.id.btnHistory); btnProfile=findViewById(R.id.btnProfile);
        btnLogout=findViewById(R.id.btnLogout); btnNotif=findViewById(R.id.btnNotif);
        tvGreeting.setText("Halo, "+sessionManager.getUserName()+" 👗");
        rvActiveOrders.setLayoutManager(new LinearLayoutManager(this));
        btnNewOrder.setOnClickListener(v->startActivity(new Intent(this,CustomerOrderActivity.class)));
        btnHistory.setOnClickListener(v->startActivity(new Intent(this,CustomerHistoryActivity.class)));
        btnProfile.setOnClickListener(v->startActivity(new Intent(this,ProfileActivity.class)));
        btnNotif.setOnClickListener(v->startActivity(new Intent(this,NotificationActivity.class)));
        btnLogout.setOnClickListener(v->showLogoutDialog());
    }
    @Override protected void onResume(){super.onResume();loadDashboard();}
    private void loadDashboard(){
        int uid=sessionManager.getUserId();
        List<OrderModel> active=dbHelper.getActiveOrdersByUser(uid);
        tvActiveCount.setText(String.valueOf(active.size()));
        int unread=dbHelper.getUnreadCount(uid);
        tvNotifBadge.setVisibility(unread>0?View.VISIBLE:View.GONE);
        tvNotifBadge.setText(String.valueOf(unread));
        OrderAdapter adapter=new OrderAdapter(active,null,false);
        rvActiveOrders.setAdapter(adapter);
        tvNoActive.setVisibility(active.isEmpty()?View.VISIBLE:View.GONE);
    }
    private void showLogoutDialog(){
        new AlertDialog.Builder(this).setTitle("Logout").setMessage("Yakin ingin keluar?")
            .setPositiveButton("Ya",(d,w)->{sessionManager.logout();startActivity(new Intent(this,LoginActivity.class));finish();})
            .setNegativeButton("Batal",null).show();
    }
}
