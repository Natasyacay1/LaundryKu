package com.example.laundryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress, etOldPassword, etNewPassword;
    private TextView tvEmail, tvBack;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = DatabaseHelper.getInstance(this);
        sessionManager = new SessionManager(this);

        tvEmail = findViewById(R.id.tvEmail);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSave = findViewById(R.id.btnSave);
        tvBack = findViewById(R.id.tvBack);

        tvBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveProfile());

        loadProfile();
    }

    private void loadProfile() {
        currentUser = dbHelper.getUserById(sessionManager.getUserId());
        if (currentUser != null) {
            tvEmail.setText(currentUser.getEmail());
            etName.setText(currentUser.getName());
            etPhone.setText(currentUser.getPhone());
            etAddress.setText(currentUser.getAddress());
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String oldPass = etOldPassword.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { etName.setError("Nama tidak boleh kosong"); return; }

        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setAddress(address);

        if (!TextUtils.isEmpty(oldPass) || !TextUtils.isEmpty(newPass)) {
            if (!oldPass.equals(currentUser.getPassword())) {
                etOldPassword.setError("Password lama salah");
                return;
            }
            if (newPass.length() < 6) {
                etNewPassword.setError("Password baru minimal 6 karakter");
                return;
            }
            currentUser.setPassword(newPass);
        } else {
            currentUser.setPassword(null); // no update
        }

        int result = dbHelper.updateUser(currentUser);
        if (result > 0) {
            sessionManager.updateName(name);
            Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            etOldPassword.setText("");
            etNewPassword.setText("");
            loadProfile();
        } else {
            Toast.makeText(this, "Gagal memperbarui profil.", Toast.LENGTH_SHORT).show();
        }
    }
}
