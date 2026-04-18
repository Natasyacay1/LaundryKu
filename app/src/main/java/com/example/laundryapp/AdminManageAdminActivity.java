package com.example.laundryapp;
import android.app.Dialog; import android.os.Bundle; import android.text.TextUtils; import android.view.*;
import android.widget.*; import androidx.appcompat.app.AlertDialog; import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*; import java.util.List;

public class AdminManageAdminActivity extends AppCompatActivity {
    private RecyclerView rv; private TextView tvEmpty,tvBack;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fab;
    private DatabaseHelper dbHelper; private SessionManager session;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s); setContentView(R.layout.activity_admin_manage_admin);
        dbHelper=DatabaseHelper.getInstance(this); session=new SessionManager(this);
        rv=findViewById(R.id.rvAdmins); tvEmpty=findViewById(R.id.tvEmpty); tvBack=findViewById(R.id.tvBack); fab=findViewById(R.id.fabAdd);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvBack.setOnClickListener(v->finish()); fab.setOnClickListener(v->showAddDialog());
    }
    @Override protected void onResume(){super.onResume();load();}
    private void load(){
        List<UserModel> list=dbHelper.getAllAdmins();
        rv.setAdapter(new AdminCustomerActivity.CustomerAdapter(list,u->showOptions(u)));
        tvEmpty.setVisibility(list.isEmpty()?View.VISIBLE:View.GONE);
    }
    private void showOptions(UserModel u){
        if(u.getId()==session.getUserId()){Toast.makeText(this,"Tidak bisa hapus akun sendiri",Toast.LENGTH_SHORT).show();return;}
        new AlertDialog.Builder(this).setTitle(u.getName()).setItems(new String[]{"Hapus Admin"},(d,w)->confirmDelete(u)).show();
    }
    private void confirmDelete(UserModel u){
        new AlertDialog.Builder(this).setTitle("Hapus Admin").setMessage("Hapus admin "+u.getName()+"?")
            .setPositiveButton("Hapus",(d,w)->{dbHelper.deleteUser(u.getId());Toast.makeText(this,"Admin dihapus",Toast.LENGTH_SHORT).show();load();})
            .setNegativeButton("Batal",null).show();
    }
    private void showAddDialog(){
        Dialog d=new Dialog(this); d.requestWindowFeature(Window.FEATURE_NO_TITLE); d.setContentView(R.layout.dialog_add_admin);
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText etName=d.findViewById(R.id.etName),etEmail=d.findViewById(R.id.etEmail),etPhone=d.findViewById(R.id.etPhone),etPassword=d.findViewById(R.id.etPassword);
        Button btnSave=d.findViewById(R.id.btnSave); TextView tvCancel=d.findViewById(R.id.tvCancel);
        btnSave.setOnClickListener(v->{
            String name=etName.getText().toString().trim(),email=etEmail.getText().toString().trim(),phone=etPhone.getText().toString().trim(),pass=etPassword.getText().toString().trim();
            if(TextUtils.isEmpty(name)){etName.setError("Wajib");return;} if(TextUtils.isEmpty(email)){etEmail.setError("Wajib");return;} if(TextUtils.isEmpty(pass)||pass.length()<6){etPassword.setError("Min 6 karakter");return;}
            if(dbHelper.isEmailExists(email)){etEmail.setError("Email sudah ada");return;}
            UserModel admin=new UserModel(); admin.setName(name); admin.setEmail(email); admin.setPassword(pass); admin.setPhone(phone);
            long res=dbHelper.addAdmin(admin);
            if(res>0){Toast.makeText(this,"Admin ditambahkan",Toast.LENGTH_SHORT).show();d.dismiss();load();}
            else Toast.makeText(this,"Gagal menambah admin",Toast.LENGTH_SHORT).show();
        });
        tvCancel.setOnClickListener(v->d.dismiss()); d.show();
    }
}
