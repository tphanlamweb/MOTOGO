package com.example.motogo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productImage.setImageResource(product.getImageResId());
        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.ShowPrice(product.getPrice(),holder.txtPrice);


        holder.btnThue.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("Name", product.getName());
            intent.putExtra("Image", product.getImageResId());
            intent.putExtra("Price", product.getPrice());  // Truyền giá thuê qua Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDescription, txtPrice;
        ImageView productImage;
        Button btnThue;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnThue = itemView.findViewById(R.id.btnThue);
        }

        private void ShowPrice(int Gia,TextView textView){
            int GiaSP = Gia; // Giá sản phẩm

            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(GiaSP) + " vnđ";

            textView.setText(formattedPrice);
        }
    }
}