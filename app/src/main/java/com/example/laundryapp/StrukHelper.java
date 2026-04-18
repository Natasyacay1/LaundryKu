package com.example.laundryapp;
import android.content.Context;
import android.content.Intent;

public class StrukHelper {
    public static void shareStruk(Context ctx, OrderModel order, String customerName) {
        String struk =
            "=============================\n" +
            "       LAUNDRYKU STRUK       \n" +
            "=============================\n" +
            "No. Order  : #" + order.getId() + "\n" +
            "Pelanggan  : " + (customerName != null ? customerName : "-") + "\n" +
            "Layanan    : " + order.getServiceName() + "\n" +
            "Berat      : " + order.getWeightFormatted() + "\n" +
            "Tgl Order  : " + order.getOrderDate() + "\n" +
            "Est. Selesai: " + (order.getEstimateDate() != null ? order.getEstimateDate() : "-") + "\n" +
            "-----------------------------\n" +
            "Subtotal   : Rp " + String.format("%,.0f", order.getTotalPrice()) + "\n" +
            (order.getDiscount() > 0 ? "Diskon     : - Rp " + String.format("%,.0f", order.getDiscount()) + "\n" : "") +
            (order.getPromoCode() != null && !order.getPromoCode().isEmpty() ? "Kode Promo : " + order.getPromoCode() + "\n" : "") +
            "TOTAL      : Rp " + String.format("%,.0f", order.getFinalTotal()) + "\n" +
            "-----------------------------\n" +
            "Status     : " + order.getStatus() + "\n" +
            "=============================\n" +
            "Terima kasih telah mempercayai\n" +
            "LaundryKu untuk pakaian Anda!\n" +
            "=============================";

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Struk Order #" + order.getId() + " - LaundryKu");
        intent.putExtra(Intent.EXTRA_TEXT, struk);
        ctx.startActivity(Intent.createChooser(intent, "Bagikan Struk via..."));
    }
}
