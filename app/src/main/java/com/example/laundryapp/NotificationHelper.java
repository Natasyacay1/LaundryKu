package com.example.laundryapp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper {
    private DatabaseHelper dbHelper;
    public NotificationHelper(android.content.Context ctx) { dbHelper = DatabaseHelper.getInstance(ctx); }

    public void sendOrderStatusNotification(OrderModel order) {
        String title = "Status Order #" + order.getId() + " Diperbarui";
        String message = getStatusMessage(order.getStatus(), order.getServiceName());
        if (order.getEstimateDate() != null && !order.getEstimateDate().isEmpty() && "Diproses".equals(order.getStatus())) {
            message += "\nEstimasi selesai: " + order.getEstimateDate();
        }
        NotificationModel n = new NotificationModel();
        n.setUserId(order.getUserId());
        n.setTitle(title);
        n.setMessage(message);
        n.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        n.setOrderId(order.getId());
        dbHelper.addNotification(n);
    }

    public void sendOrderCreatedNotification(OrderModel order) {
        NotificationModel n = new NotificationModel();
        n.setUserId(order.getUserId());
        n.setTitle("Order Berhasil Dibuat! 🧺");
        n.setMessage("Order #" + order.getId() + " untuk " + order.getServiceName() + " sedang menunggu konfirmasi admin.\nEstimasi selesai: " + (order.getEstimateDate() != null ? order.getEstimateDate() : "-"));
        n.setDate(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        n.setOrderId(order.getId());
        dbHelper.addNotification(n);
    }

    private String getStatusMessage(String status, String service) {
        switch (status != null ? status : "") {
            case "Diproses": return "Laundry " + service + " kamu sedang diproses. Mohon tunggu ya! 🌀";
            case "Selesai":  return "Laundry " + service + " kamu sudah selesai! Siap untuk diambil. ✅";
            case "Diambil":  return "Order " + service + " telah diambil. Terima kasih! ⭐";
            default:         return "Status order " + service + " diubah ke: " + status;
        }
    }
}
