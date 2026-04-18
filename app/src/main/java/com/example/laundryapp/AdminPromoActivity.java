package com.example.laundryapp;
import android.app.Dialog; import android.os.Bundle; import android.text.TextUtils; import android.view.*;
import android.widget.*; import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*; import java.util.List;

public class AdminPromoActivity extends AppCompatActivity {
    private RecyclerView rv; private TextView tvEmpty,tvBack;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    private DatabaseHelper dbHelper;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s); setContentView(R.layout.activity_admin_promo);
        dbHelper=DatabaseHelper.getInstance(this);
        rv=findViewById(R.id.rvPromos); tvEmpty=findViewById(R.id.tvEmpty); tvBack=findViewById(R.id.tvBack); fab=findViewById(R.id.fabAdd);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvBack.setOnClickListener(v->finish()); fab.setOnClickListener(v->showDialog(null));
    }
    @Override protected void onResume(){super.onResume();load();}
    private void load(){
        List<PromoModel> list=dbHelper.getAllPromos();
        rv.setAdapter(new PromoAdapter(list,p->showDialog(p),p->confirmDelete(p),p->toggleActive(p)));
        tvEmpty.setVisibility(list.isEmpty()?View.VISIBLE:View.GONE);
    }
    private void toggleActive(PromoModel p){p.setActive(!p.isActive());dbHelper.updatePromo(p);Toast.makeText(this,p.isActive()?"Promo diaktifkan":"Promo dinonaktifkan",Toast.LENGTH_SHORT).show();load();}
    private void confirmDelete(PromoModel p){new AlertDialog.Builder(this).setTitle("Hapus Promo").setMessage("Hapus promo "+p.getCode()+"?").setPositiveButton("Hapus",(d,w)->{dbHelper.deletePromo(p.getId());Toast.makeText(this,"Promo dihapus",Toast.LENGTH_SHORT).show();load();}).setNegativeButton("Batal",null).show();}
    private void showDialog(PromoModel promo){
        Dialog d=new Dialog(this); d.requestWindowFeature(Window.FEATURE_NO_TITLE); d.setContentView(R.layout.dialog_promo);
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvTitle=d.findViewById(R.id.tvTitle); EditText etCode=d.findViewById(R.id.etCode),etDesc=d.findViewById(R.id.etDesc),etValue=d.findViewById(R.id.etValue),etMin=d.findViewById(R.id.etMin),etExpiry=d.findViewById(R.id.etExpiry);
        Spinner spType=d.findViewById(R.id.spType); Button btnSave=d.findViewById(R.id.btnSave); TextView tvCancel=d.findViewById(R.id.tvCancel);
        boolean isEdit=promo!=null; tvTitle.setText(isEdit?"Edit Promo":"Tambah Promo");
        String[] types={"percent","fixed"}; ArrayAdapter<String> ta=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,types); ta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spType.setAdapter(ta);
        if(isEdit){etCode.setText(promo.getCode());etDesc.setText(promo.getDescription());etValue.setText(String.valueOf((int)promo.getValue()));etMin.setText(String.valueOf((int)promo.getMinOrder()));etExpiry.setText(promo.getExpiryDate());for(int i=0;i<types.length;i++)if(types[i].equals(promo.getType())){spType.setSelection(i);break;}}
        btnSave.setOnClickListener(v->{
            String code=etCode.getText().toString().trim(),desc=etDesc.getText().toString().trim(),valStr=etValue.getText().toString().trim(),minStr=etMin.getText().toString().trim(),expiry=etExpiry.getText().toString().trim();
            if(TextUtils.isEmpty(code)){etCode.setError("Wajib");return;} if(TextUtils.isEmpty(valStr)){etValue.setError("Wajib");return;}
            PromoModel p=isEdit?promo:new PromoModel(); p.setCode(code); p.setDescription(desc); p.setType(types[spType.getSelectedItemPosition()]); p.setValue(Double.parseDouble(valStr)); p.setMinOrder(TextUtils.isEmpty(minStr)?0:Double.parseDouble(minStr)); p.setExpiryDate(expiry); p.setActive(true);
            if(isEdit)dbHelper.updatePromo(p); else dbHelper.addPromo(p);
            Toast.makeText(this,"Promo disimpan",Toast.LENGTH_SHORT).show(); d.dismiss(); load();
        });
        tvCancel.setOnClickListener(v->d.dismiss()); d.show();
    }

    static class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.VH>{
        List<PromoModel> list; java.util.function.Consumer<PromoModel> onEdit,onDelete,onToggle;
        PromoAdapter(List<PromoModel> l,java.util.function.Consumer<PromoModel> e,java.util.function.Consumer<PromoModel> d,java.util.function.Consumer<PromoModel> t){list=l;onEdit=e;onDelete=d;onToggle=t;}
        @Override public VH onCreateViewHolder(ViewGroup p,int t){return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_promo,p,false));}
        @Override public void onBindViewHolder(VH h,int pos){
            PromoModel p=list.get(pos); h.tvCode.setText(p.getCode()); h.tvDesc.setText(p.getDescription()!=null?p.getDescription():"");
            h.tvValue.setText(p.getDisplayValue()); h.tvMin.setText("Min: Rp "+String.format("%,.0f",p.getMinOrder()));
            h.tvStatus.setText(p.isActive()?"Aktif":"Nonaktif"); h.tvStatus.setTextColor(p.isActive()?0xFF618C82:0xFF888888);
            h.btnEdit.setOnClickListener(v->onEdit.accept(p)); h.btnDelete.setOnClickListener(v->onDelete.accept(p)); h.tvStatus.setOnClickListener(v->onToggle.accept(p));
        }
        @Override public int getItemCount(){return list.size();}
        static class VH extends RecyclerView.ViewHolder{TextView tvCode,tvDesc,tvValue,tvMin,tvStatus;ImageButton btnEdit,btnDelete;
            VH(View v){super(v);tvCode=v.findViewById(R.id.tvCode);tvDesc=v.findViewById(R.id.tvDesc);tvValue=v.findViewById(R.id.tvValue);tvMin=v.findViewById(R.id.tvMin);tvStatus=v.findViewById(R.id.tvStatus);btnEdit=v.findViewById(R.id.btnEdit);btnDelete=v.findViewById(R.id.btnDelete);}}
    }
}
