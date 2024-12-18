package btl.skincareapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import btl.skincareapp.R;
import btl.skincareapp.model.MyProduct;

public class routineAdapter extends RecyclerView.Adapter<routineAdapter.routineViewHolder> {

    List<MyProduct> sanPhamList;
    Context context;

    public routineAdapter(List<MyProduct> sanPhamList, Context context) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public routineAdapter.routineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine_home, parent, false);
        routineViewHolder holder = new routineViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull routineAdapter.routineViewHolder holder, int position) {
        holder.txSp.setText(sanPhamList.get(position).getTenSp());
        Bitmap bitmap = BitmapFactory.decodeFile(sanPhamList.get(position).getUrl_Anh());
        holder.AnhSp.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public class routineViewHolder extends RecyclerView.ViewHolder {
        ImageView AnhSp;
        TextView txSp;
        public routineViewHolder(@NonNull View itemView) {
            super(itemView);
            AnhSp = itemView.findViewById(R.id.ivProductImage);
            txSp = itemView.findViewById(R.id.tvProductName);
        }
    }
}
