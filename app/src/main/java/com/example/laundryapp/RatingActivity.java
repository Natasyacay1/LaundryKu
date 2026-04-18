package com.example.laundryapp;
import android.os.Bundle; import android.text.TextUtils; import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat; import java.util.*; import android.view.View;

public class RatingActivity extends AppCompatActivity {
    private TextView tvBack, tvOrderInfo, tvStar1, tvStar2, tvStar3, tvStar4, tvStar5;
    private EditText etReview; private Button btnSubmit;
    private int selectedScore = 0; private int orderId, userId;
    private DatabaseHelper dbHelper;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s); setContentView(R.layout.activity_rating);
        dbHelper=DatabaseHelper.getInstance(this);
        orderId=getIntent().getIntExtra("order_id",-1);
        userId=getIntent().getIntExtra("user_id",-1);
        String serviceName=getIntent().getStringExtra("service_name");
        tvBack=findViewById(R.id.tvBack); tvOrderInfo=findViewById(R.id.tvOrderInfo);
        tvStar1=findViewById(R.id.tvStar1); tvStar2=findViewById(R.id.tvStar2); tvStar3=findViewById(R.id.tvStar3);
        tvStar4=findViewById(R.id.tvStar4); tvStar5=findViewById(R.id.tvStar5);
        etReview=findViewById(R.id.etReview); btnSubmit=findViewById(R.id.btnSubmit);
        tvOrderInfo.setText("Beri rating untuk: " + (serviceName!=null?serviceName:"Order #"+orderId));
        tvBack.setOnClickListener(v->finish());
        TextView[] stars={tvStar1,tvStar2,tvStar3,tvStar4,tvStar5};
        for(int i=0;i<5;i++){final int score=i+1;stars[i].setOnClickListener(v->{selectedScore=score;updateStars(stars,score);});}
        btnSubmit.setOnClickListener(v->submitRating());
    }
    private void updateStars(TextView[] stars,int score){for(int i=0;i<5;i++)stars[i].setTextColor(i<score?0xFFDD5547:0xFFCCCCCC);}
    private void submitRating(){
        if(selectedScore==0){Toast.makeText(this,"Pilih bintang dulu",Toast.LENGTH_SHORT).show();return;}
        RatingModel r=new RatingModel(); r.setOrderId(orderId); r.setUserId(userId); r.setScore(selectedScore);
        r.setReview(etReview.getText().toString().trim());
        r.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm",Locale.getDefault()).format(new Date()));
        long res=dbHelper.addRating(r);
        if(res>0){Toast.makeText(this,"Terima kasih atas ulasan kamu! ⭐",Toast.LENGTH_SHORT).show();finish();}
        else Toast.makeText(this,"Gagal menyimpan rating.",Toast.LENGTH_SHORT).show();
    }
}
