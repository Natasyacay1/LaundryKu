package com.example.laundryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword, etPhone, etAddress;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = DatabaseHelper.getInstance(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { etName.setError("Nama tidak boleh kosong"); return; }
        if (TextUtils.isEmpty(email)) { etEmail.setError("Email tidak boleh kosong"); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid"); return;
        }
        if (TextUtils.isEmpty(password)) { etPassword.setError("Password tidak boleh kosong"); return; }
        if (password.length() < 6) { etPassword.setError("Password minimal 6 karakter"); return; }
        if (!password.equals(confirmPassword)) { etConfirmPassword.setError("Password tidak cocok"); return; }
        if (TextUtils.isEmpty(phone)) { etPhone.setError("Nomor HP tidak boleh kosong"); return; }

        if (dbHelper.isEmailExists(email)) {
            etEmail.setError("Email sudah terdaftar");
            return;
        }

        UserModel user = new UserModel(name, email, password, phone, address);
        long result = dbHelper.registerUser(user);

        if (result > 0) {
            Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registrasi gagal. Coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }
}
