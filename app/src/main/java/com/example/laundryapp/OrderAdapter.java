package com.example.laundryapp;
import android.view.*; import android.widget.*; import androidx.recyclerview.widget.*;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    public interface OnOrderActionListener {
        void onEditClick(OrderModel order); void onDeleteClick(OrderModel order);
        void onStatusClick(OrderModel order); void onShareClick(OrderModel order);
        default void onRateClick(OrderModel order){}
    }
    private List<OrderModel> orderList; private OnOrderActionListener listener; private boolean isAdmin;
    public OrderAdapter(List<OrderModel> l, OnOrderActionListener li, boolean admin){orderList=l;listener=li;isAdmin=admin;}

    @Override public OrderViewHolder onCreateViewHolder(ViewGroup p,int t){
        return new OrderViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_order,p,false));
    }
    @Override public void onBindViewHolder(OrderViewHolder h,int pos){
        OrderModel o=orderList.get(pos);
        h.tvOrderId.setText("#"+o.getId()); h.tvServiceName.setText(o.getServiceName()!=null?o.getServiceName():"Layanan");
        h.tvWeight.setText("⚖️ "+o.getWeightFormatted()); h.tvTotal.setText(o.getFormattedTotal()); h.tvDate.setText("📅 "+o.getOrderDate());
        h.tvStatus.setText(o.getStatus());
        if(o.getEstimateDate()!=null&&!o.getEstimateDate().isEmpty()){h.tvEstimate.setVisibility(View.VISIBLE);h.tvEstimate.setText("⏱ Est: "+o.getEstimateDate());}else{h.tvEstimate.setVisibility(View.GONE);}
        if(o.getDiscount()>0){h.tvDiscount.setVisibility(View.VISIBLE);h.tvDiscount.setText("🏷 Diskon: "+o.getFormattedDiscount());}else{h.tvDiscount.setVisibility(View.GONE);}
        if(isAdmin&&o.getUserName()!=null){h.tvCustomerName.setVisibility(View.VISIBLE);h.tvCustomerName.setText("👤 "+o.getUserName());}else{h.tvCustomerName.setVisibility(View.GONE);}
        switch(o.getStatus()!=null?o.getStatus():""){
            case "Pending": h.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);break;
            case "Diproses": h.tvStatus.setBackgroundResource(R.drawable.bg_status_process);break;
            case "Selesai": h.tvStatus.setBackgroundResource(R.drawable.bg_status_done);break;
            case "Diambil": h.tvStatus.setBackgroundResource(R.drawable.bg_status_taken);break;
            default: h.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
        }
        if(isAdmin&&listener!=null){
            h.btnEdit.setVisibility(View.VISIBLE); h.btnDelete.setVisibility(View.VISIBLE); h.btnShare.setVisibility(View.VISIBLE);
            h.btnRate.setVisibility(View.GONE);
            h.btnEdit.setOnClickListener(v->listener.onEditClick(o));
            h.btnDelete.setOnClickListener(v->listener.onDeleteClick(o));
            h.tvStatus.setOnClickListener(v->listener.onStatusClick(o));
            h.btnShare.setOnClickListener(v->listener.onShareClick(o));
        } else if(!isAdmin&&listener!=null){
            h.btnEdit.setVisibility(View.GONE); h.btnDelete.setVisibility(View.GONE);
            h.btnShare.setVisibility(View.VISIBLE);
            h.btnShare.setOnClickListener(v->listener.onShareClick(o));
            boolean canRate="Diambil".equals(o.getStatus());
            h.btnRate.setVisibility(canRate?View.VISIBLE:View.GONE);
            if(canRate) h.btnRate.setOnClickListener(v->listener.onRateClick(o));
        } else {
            h.btnEdit.setVisibility(View.GONE); h.btnDelete.setVisibility(View.GONE);
            h.btnShare.setVisibility(View.GONE); h.btnRate.setVisibility(View.GONE);
        }
    }
    @Override public int getItemCount(){return orderList!=null?orderList.size():0;}

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId,tvServiceName,tvWeight,tvTotal,tvDate,tvStatus,tvCustomerName,tvEstimate,tvDiscount;
        ImageButton btnEdit,btnDelete,btnShare; Button btnRate;
        OrderViewHolder(View v){super(v);
            tvOrderId=v.findViewById(R.id.tvOrderId); tvServiceName=v.findViewById(R.id.tvServiceName);
            tvWeight=v.findViewById(R.id.tvWeight); tvTotal=v.findViewById(R.id.tvTotal); tvDate=v.findViewById(R.id.tvDate);
            tvStatus=v.findViewById(R.id.tvStatus); tvCustomerName=v.findViewById(R.id.tvCustomerName);
            tvEstimate=v.findViewById(R.id.tvEstimate); tvDiscount=v.findViewById(R.id.tvDiscount);
            btnEdit=v.findViewById(R.id.btnEdit); btnDelete=v.findViewById(R.id.btnDelete);
            btnShare=v.findViewById(R.id.btnShare); btnRate=v.findViewById(R.id.btnRate);
        }
    }
}
