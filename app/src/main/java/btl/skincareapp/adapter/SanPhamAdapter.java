package btl.skincareapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import btl.skincareapp.model.Product;
import btl.skincareapp.R;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {

    String isSmall, isHome;
    List<Product> sanPhamList;
    Context context;

    public SanPhamAdapter(List<Product> sanPhamList, Context context, String isSmall, String isHome) {
        this.sanPhamList = sanPhamList;
        this.context = context;
        this.isSmall = isSmall;
        this.isHome = isHome;
    }


    /// Return xem no display cai gi
    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(this.isSmall == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_small, parent, false);
        }
        SanPhamViewHolder holder = new SanPhamViewHolder(view);

        return holder;
    }


    ///
    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        holder.txSp.setText(sanPhamList.get(position).getTenSp());
        holder.txDescrip.setText(sanPhamList.get(position).getMoTaSP());
        if (sanPhamList.get(position).getImageImport() != 0) {
            holder.AnhSp.setImageResource(sanPhamList.get(position).getImageImport());
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(sanPhamList.get(position).getImageURL());
            holder.AnhSp.setImageBitmap(bitmap);
        }
    }


    // Dem co may san pham
    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }


    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        ImageView AnhSp;
        TextView txSp, txDescrip;
        Button btnAddRoutine;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            AnhSp = itemView.findViewById(R.id.ivProductImage);
            txSp = itemView.findViewById(R.id.tvProductName);
            txDescrip = itemView.findViewById(R.id.tvProductDescription);

        }
    }
}
