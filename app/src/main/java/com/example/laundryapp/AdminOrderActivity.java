package com.example.laundryapp;
import android.app.Dialog; import android.os.Bundle; import android.text.TextUtils; import android.view.Window;
import android.widget.*; import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList; import java.util.List;

public class AdminOrderActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {
    private RecyclerView rvOrders; private TextView tvEmpty, tvBack; private EditText etSearch;
    private OrderAdapter adapter; private List<OrderModel> filteredList = new ArrayList<>();
    private DatabaseHelper dbHelper; private NotificationHelper notifHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_admin_order);
        dbHelper = DatabaseHelper.getInstance(this); notifHelper = new NotificationHelper(this);
        rvOrders = findViewById(R.id.rvOrders); tvEmpty = findViewById(R.id.tvEmpty);
        tvBack = findViewById(R.id.tvBack); etSearch = findViewById(R.id.etSearch);
        adapter = new OrderAdapter(filteredList, this, true);
        rvOrders.setLayoutManager(new LinearLayoutManager(this)); rvOrders.setAdapter(adapter);
        tvBack.setOnClickListener(v -> finish());
        etSearch.addTextChangedListener(new android.text.TextWatcher(){
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){loadOrders(s.toString());}
            public void afterTextChanged(android.text.Editable s){}
        });
    }

    @Override protected void onResume(){super.onResume();loadOrders("");}

    private void loadOrders(String query) {
        List<OrderModel> list = query.isEmpty() ? dbHelper.getAllOrders() : dbHelper.searchOrders(query);
        filteredList.clear(); filteredList.addAll(list); adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(filteredList.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override public void onEditClick(OrderModel order) { showEditOrderDialog(order); }
    @Override public void onDeleteClick(OrderModel order) {
        new AlertDialog.Builder(this).setTitle("Hapus Order").setMessage("Hapus order #"+order.getId()+"?")
            .setPositiveButton("Hapus",(d,w)->{dbHelper.deleteOrder(order.getId());Toast.makeText(this,"Order dihapus",Toast.LENGTH_SHORT).show();loadOrders("");})
            .setNegativeButton("Batal",null).show();
    }
    @Override public void onStatusClick(OrderModel order) {
        String[] statuses={"Pending","Diproses","Selesai","Diambil"};
        new AlertDialog.Builder(this).setTitle("Ubah Status Order #"+order.getId())
            .setItems(statuses,(d,w)->{
                order.setStatus(statuses[w]); dbHelper.updateOrderStatus(order.getId(),statuses[w]);
                notifHelper.sendOrderStatusNotification(order);
                Toast.makeText(this,"Status → "+statuses[w],Toast.LENGTH_SHORT).show(); loadOrders(etSearch.getText().toString());
            }).show();
    }
    @Override public void onShareClick(OrderModel order) {
        UserModel user = dbHelper.getUserById(order.getUserId());
        StrukHelper.shareStruk(this, order, user != null ? user.getName() : "");
    }

    private void showEditOrderDialog(OrderModel order) {
        Dialog dialog = new Dialog(this); dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_order);
        dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        Spinner spStatus = dialog.findViewById(R.id.spStatus); EditText etNotes = dialog.findViewById(R.id.etNotes);
        Button btnSave = dialog.findViewById(R.id.btnSave); TextView tvCancel = dialog.findViewById(R.id.tvCancel);
        TextView tvOrderInfo = dialog.findViewById(R.id.tvOrderInfo);
        tvOrderInfo.setText("Order #"+order.getId()+" - "+order.getUserName()); etNotes.setText(order.getNotes());
        String[] statuses={"Pending","Diproses","Selesai","Diambil"};
        ArrayAdapter<String> sa=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,statuses);
        sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spStatus.setAdapter(sa);
        for(int i=0;i<statuses.length;i++) if(statuses[i].equals(order.getStatus())){spStatus.setSelection(i);break;}
        btnSave.setOnClickListener(v->{
            String newStatus=statuses[spStatus.getSelectedItemPosition()];
            boolean statusChanged = !newStatus.equals(order.getStatus());
            order.setStatus(newStatus); order.setNotes(etNotes.getText().toString().trim());
            dbHelper.updateOrder(order);
            if (statusChanged) notifHelper.sendOrderStatusNotification(order);
            Toast.makeText(this,"Order diperbarui",Toast.LENGTH_SHORT).show(); dialog.dismiss(); loadOrders("");
        });
        tvCancel.setOnClickListener(v->dialog.dismiss()); dialog.show();
    }
}
