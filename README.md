# 🧺 LaundryApp — Android Laundry Management System

LaundryApp adalah aplikasi Android untuk mengelola layanan laundry secara digital.  
Aplikasi ini menyediakan fitur untuk **customer** dan **admin**, mulai dari pemesanan laundry, pengelolaan layanan, hingga laporan transaksi.

Dirancang menggunakan **Java**, **SQLite**, dan **Material UI**, aplikasi ini bertujuan untuk memberikan pengalaman penggunaan yang sederhana namun lengkap.

---

# 📱 Fitur Utama

## 👤 Customer Features

- 🔐 Login & Register akun
- 🧺 Membuat pesanan laundry
- 📋 Melihat riwayat pesanan
- ⭐ Memberikan rating layanan
- 🔔 Melihat notifikasi
- 👤 Mengelola profil pengguna
- 🏠 Dashboard customer
- 📦 Melihat detail pesanan

---

## 🛠️ Admin Features

- 📊 Dashboard admin
- 👥 Mengelola data customer
- 🧺 Mengelola pesanan laundry
- 🧼 Mengelola layanan laundry
- 🎁 Mengelola promo
- 👨‍💼 Mengelola admin
- 📈 Melihat laporan transaksi
- ✏️ Edit dan update data pesanan

---

# 🧱 Struktur Halaman (Based on Layout Files)

## Authentication

- `activity_splash.xml` → Splash screen
- `activity_login.xml` → Login page
- `activity_register.xml` → Register page

---

## Customer Pages

- `activity_customer_dashboard.xml`
- `activity_customer_order.xml`
- `activity_customer_history.xml`
- `activity_profile.xml`
- `activity_notification.xml`
- `activity_rating.xml`

---

## Admin Pages

- `activity_admin_dashboard.xml`
- `activity_admin_customer.xml`
- `activity_admin_order.xml`
- `activity_admin_service.xml`
- `activity_admin_promo.xml`
- `activity_admin_manage_admin.xml`
- `activity_admin_report.xml`

---

## Dialog Components

Digunakan untuk input dan edit data:

- `dialog_add_admin.xml`
- `dialog_edit_order.xml`
- `dialog_service.xml`
- `dialog_promo.xml`

---

## Item Layouts (RecyclerView)

Digunakan untuk menampilkan list data:

- `item_customer.xml`
- `item_notification.xml`

---

# 🛠️ Teknologi yang Digunakan

- Java
- Android Studio
- SQLite Database
- XML Layout
- RecyclerView
- Material Design Components

---

# 🗄️ Database

Database menggunakan **SQLite** untuk menyimpan:

- Data User
- Data Admin
- Data Order
- Data Service
- Data Promo
- Data Rating
- Data Notification

---

# 🚀 Cara Menjalankan Project

1. Clone repository:

```bash
git clone https://github.com/USERNAME/LaundryApp.git

---
