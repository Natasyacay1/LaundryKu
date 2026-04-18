package com.example.laundryapp;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomerOrderActivity extends AppCompatActivity {
    private Spinner spService;
    private EditText etWeight, etNotes, etPromoCode;
    private TextView tvPrice, tvTotal, tvBack, tvEstimate, tvDiscount, tvFinalTotal, tvPromoResult;
    private Button btnOrder, btnApplyPromo;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private List<ServiceModel> serviceList;
    private double currentPrice = 0;
    private double discountAmount = 0;
    private PromoModel appliedPromo = null;
    private int estimateDays = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        dbHelper = DatabaseHelper.getInstance(this);
        sessionManager = new SessionManager(this);
        spService = findViewById(R.id.spService);
        etWeight = findViewById(R.id.etWeight);
        etNotes = findViewById(R.id.etNotes);
        etPromoCode = findViewById(R.id.etPromoCode);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotal = findViewById(R.id.tvTotal);
        tvEstimate = findViewById(R.id.tvEstimate);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        tvPromoResult = findViewById(R.id.tvPromoResult);
        tvBack = findViewById(R.id.tvBack);
        btnOrder = findViewById(R.id.btnOrder);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        tvBack.setOnClickListener(v -> finish());
        serviceList = dbHelper.getAllServices();
        String[] names = new String[serviceList.size()];
        for (int i = 0; i < serviceList.size(); i++)
            names[i] = serviceList.get(i).getName() + " - " + serviceList.get(i).getFormattedPrice();
        ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spService.setAdapter(a);
        spService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                currentPrice = serviceList.get(pos).getPrice();
                estimateDays = serviceList.get(pos).getEstimateDays();
                tvPrice.setText(serviceList.get(pos).getFormattedPrice());
                tvEstimate.setText("⏱ Estimasi: " + serviceList.get(pos).getEstimateText());
                calculateTotal();
            }
            public void onNothingSelected(AdapterView<?> p) {}
        });
        etWeight.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int a,int b,int c){}
            public void onTextChanged(CharSequence s,int a,int b,int c){calculateTotal();}
            public void afterTextChanged(android.text.Editable s){}
        });
        btnApplyPromo.setOnClickListener(v -> applyPromo());
        btnOrder.setOnClickListener(v -> submitOrder());
    }

    private void calculateTotal() {
        String ws = etWeight.getText().toString().trim();
        if (!TextUtils.isEmpty(ws)) {
            try {
                double w = Double.parseDouble(ws);
                double total = w * currentPrice;
                tvTotal.setText("Subtotal: Rp " + String.format("%,.0f", total));
                if (appliedPromo != null) {
                    discountAmount = appliedPromo.calculateDiscount(total);
                    tvDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText("Diskon (" + appliedPromo.getCode() + "): - Rp " + String.format("%,.0f", discountAmount));
                    tvFinalTotal.setText("TOTAL: Rp " + String.format("%,.0f", total - discountAmount));
                } else {
                    tvDiscount.setVisibility(View.GONE);
                    tvFinalTotal.setText("TOTAL: Rp " + String.format("%,.0f", total));
                }
            } catch (NumberFormatException e) { tvFinalTotal.setText("TOTAL: Rp 0"); }
        } else { tvFinalTotal.setText("TOTAL: Rp 0"); }
    }

    private void applyPromo() {
        String code = etPromoCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) { Toast.makeText(this, "Masukkan kode promo", Toast.LENGTH_SHORT).show(); return; }
        PromoModel promo = dbHelper.getPromoByCode(code);
        if (promo == null) { tvPromoResult.setVisibility(View.VISIBLE); tvPromoResult.setText("❌ Kode promo tidak valid atau sudah expired"); tvPromoResult.setTextColor(0xFFDD5547); appliedPromo = null; calculateTotal(); return; }
        appliedPromo = promo;
        tvPromoResult.setVisibility(View.VISIBLE);
        tvPromoResult.setText("✅ Promo " + promo.getCode() + " berhasil! Hemat " + promo.getDisplayValue());
        tvPromoResult.setTextColor(0xFF618C82);
        calculateTotal();
    }

    private void submitOrder() {
        String ws = etWeight.getText().toString().trim();
        if (TextUtils.isEmpty(ws)) { etWeight.setError("Berat tidak boleh kosong"); return; }
        double weight;
        try { weight = Double.parseDouble(ws); if (weight <= 0) { etWeight.setError("Berat harus > 0"); return; } }
        catch (NumberFormatException e) { etWeight.setError("Format tidak valid"); return; }
        ServiceModel svc = serviceList.get(spService.getSelectedItemPosition());
        double total = weight * svc.getPrice();
        discountAmount = appliedPromo != null ? appliedPromo.calculateDiscount(total) : 0;
        double finalTotal = total - discountAmount;
        String orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, estimateDays);
        String estimateDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());
        OrderModel order = new OrderModel();
        order.setUserId(sessionManager.getUserId());
        order.setServiceId(svc.getId());
        order.setWeight(weight);
        order.setTotalPrice(total);
        order.setDiscount(discountAmount);
        order.setFinalTotal(finalTotal);
        order.setPromoCode(appliedPromo != null ? appliedPromo.getCode() : "");
        order.setStatus("Pending");
        order.setOrderDate(orderDate);
        order.setEstimateDate(estimateDate);
        order.setNotes(etNotes.getText().toString().trim());
        order.setServiceName(svc.getName());
        long result = dbHelper.addOrder(order);
        if (result > 0) {
            order.setId((int) result);
            new NotificationHelper(this).sendOrderCreatedNotification(order);
            Toast.makeText(this, "Order berhasil! Estimasi selesai: " + estimateDate, Toast.LENGTH_LONG).show();
            finish();
        } else { Toast.makeText(this, "Gagal membuat order.", Toast.LENGTH_SHORT).show(); }
    }
}
