package com.example.laundryapp;
import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.*;
import java.util.List;

public class CustomerHistoryActivity extends AppCompatActivity {
    private RecyclerView rv; private TextView tvEmpty,tvBack; private EditText etSearch;
    private DatabaseHelper dbHelper; private SessionManager session;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s); setContentView(R.layout.activity_customer_history);
        dbHelper=DatabaseHelper.getInstance(this); session=new SessionManager(this);
        rv=findViewById(R.id.rvHistory); tvEmpty=findViewById(R.id.tvEmpty); tvBack=findViewById(R.id.tvBack); etSearch=findViewById(R.id.etSearch);
        rv.setLayoutManager(new LinearLayoutManager(this)); tvBack.setOnClickListener(v->finish());
        etSearch.addTextChangedListener(new android.text.TextWatcher(){
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){load(s.toString());}
            public void afterTextChanged(android.text.Editable s){}
        });
    }
    @Override protected void onResume(){super.onResume();load("");}
    private void load(String q){
        int uid=session.getUserId();
        List<OrderModel> list=q.isEmpty()?dbHelper.getOrdersByUser(uid):dbHelper.searchOrdersByUser(uid,q);
        OrderAdapter adapter=new OrderAdapter(list,new OrderAdapter.OnOrderActionListener(){
            public void onEditClick(OrderModel o){}
            public void onDeleteClick(OrderModel o){}
            public void onStatusClick(OrderModel o){}
            public void onShareClick(OrderModel o){StrukHelper.shareStruk(CustomerHistoryActivity.this,o,session.getUserName());}
            public void onRateClick(OrderModel o){
                Intent i=new Intent(CustomerHistoryActivity.this,RatingActivity.class);
                i.putExtra("order_id",o.getId()); i.putExtra("user_id",o.getUserId()); i.putExtra("service_name",o.getServiceName());
                startActivity(i);
            }
        },false);
        rv.setAdapter(adapter); tvEmpty.setVisibility(list.isEmpty()?View.VISIBLE:View.GONE);
    }
}
