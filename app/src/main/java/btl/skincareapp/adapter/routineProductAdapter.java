package btl.skincareapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import btl.skincareapp.R;
import btl.skincareapp.model.MyProduct;

public class routineProductAdapter extends RecyclerView.Adapter<routineProductAdapter.routineProductViewHolder> {

    List<MyProduct> selectedList;
    Context context;

    public routineProductAdapter(List<MyProduct> selectedList, Context context) {
        this.selectedList = selectedList;
        this.context = context;
    }

    @NonNull
    @Override
    public routineProductAdapter.routineProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        routineProductViewHolder holder = new routineProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull routineProductAdapter.routineProductViewHolder holder, int position) {
        holder.txSp.setText(selectedList.get(position).getTenSp());
        holder.AnhSp.setImageURI(Uri.parse(selectedList.get(position).getUrl_Anh()));
        
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != -1) {
                    AlertDialog.Builder alertdialog = getBuilder(view, adapterPosition);
                    alertdialog.show();
                }
            }
        });
    }

    private AlertDialog.Builder getBuilder(View view, int adapterPosition) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(view.getContext());
        alertdialog.setTitle("Thông báo");
        alertdialog.setMessage("Bạn có chắc chắn muốn xóa không?");
        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, selectedList.size());
            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return alertdialog;
    }

    @Override
    public int getItemCount() {
        return selectedList.size();
    }

    public class routineProductViewHolder extends RecyclerView.ViewHolder {
        ImageView AnhSp;
        TextView txSp;
        ImageButton btnXoa;

        public routineProductViewHolder(@NonNull View itemView) {
            super(itemView);
            AnhSp = itemView.findViewById(R.id.productImage);
            txSp = itemView.findViewById(R.id.productName);
            btnXoa = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}
