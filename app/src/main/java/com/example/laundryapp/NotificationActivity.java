package com.example.laundryapp;
import android.os.Bundle; import android.view.*; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.*;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView rv; private TextView tvEmpty, tvBack;
    private DatabaseHelper dbHelper; private SessionManager session;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s); setContentView(R.layout.activity_notification);
        dbHelper=DatabaseHelper.getInstance(this); session=new SessionManager(this);
        rv=findViewById(R.id.rvNotifications); tvEmpty=findViewById(R.id.tvEmpty); tvBack=findViewById(R.id.tvBack);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvBack.setOnClickListener(v->finish());
    }
    @Override protected void onResume(){super.onResume();load();}
    private void load(){
        int uid=session.getUserId(); dbHelper.markAllRead(uid);
        List<NotificationModel> list=dbHelper.getNotificationsByUser(uid);
        rv.setAdapter(new NotifAdapter(list));
        tvEmpty.setVisibility(list.isEmpty()?View.VISIBLE:View.GONE);
    }

    static class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.VH> {
        List<NotificationModel> list;
        NotifAdapter(List<NotificationModel> l){list=l;}
        @Override public VH onCreateViewHolder(ViewGroup p,int t){return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_notification,p,false));}
        @Override public void onBindViewHolder(VH h,int pos){
            NotificationModel n=list.get(pos);
            h.tvTitle.setText(n.getTitle()); h.tvMessage.setText(n.getMessage()); h.tvDate.setText(n.getDate());
            h.itemView.setAlpha(n.isRead()?0.7f:1f);
        }
        @Override public int getItemCount(){return list.size();}
        static class VH extends RecyclerView.ViewHolder{TextView tvTitle,tvMessage,tvDate;VH(View v){super(v);tvTitle=v.findViewById(R.id.tvTitle);tvMessage=v.findViewById(R.id.tvMessage);tvDate=v.findViewById(R.id.tvDate);}}
    }
}
