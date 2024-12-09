package btl.skincareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import android.content.ContentResolver;

import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;

public class myProductAdapter extends RecyclerView.Adapter<myProductAdapter.myProductViewHolder> {

    List<MyProduct> sanPhamList;
    Context context;

    public myProductAdapter(List<MyProduct> sanPhamList, Context context) {
        this.sanPhamList = sanPhamList;
        this.context = context;
    }


    /// Return xem no display cai gi
    @NonNull
    @Override
    public myProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list, parent, false);
        myProductViewHolder holder = new myProductViewHolder(view);

        return holder;
    }


    ///
    @Override
    public void onBindViewHolder(@NonNull myProductViewHolder holder, int position) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFor = NumberFormat.getCurrencyInstance(localeVN);

        holder.txSp.setText(sanPhamList.get(position).getTenSp());

        /// Hien thi anh
        Bitmap bitmap = BitmapFactory.decodeFile(sanPhamList.get(position).getUrl_Anh());
        holder.AnhSp.setImageBitmap(bitmap);

        holder.txtGia.setText("Giá: " + currencyFor.format(Integer.parseInt(sanPhamList.get(position).getGia())));
        holder.txtNgayMua.setText("Ngày mua: "+sanPhamList.get(position).getNgayMua().toString());
        holder.txtNgayhetHan.setText("Ngày hết hạn: "+sanPhamList.get(position).getNgayHetHan().toString());

        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if(adapterPosition != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
                    alertDialog.setTitle("Thông báo");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseHelper.deleteOneSP(sanPhamList.get(adapterPosition).getId());
                            sanPhamList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, sanPhamList.size());
                        }
                    });
                    alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }

            }
        });
    }


    // Dem co may san pham
    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }


    public class myProductViewHolder extends RecyclerView.ViewHolder {
        ImageView AnhSp;
        TextView txSp, txtGia, txtNgayhetHan, txtNgayMua;
        Button btnSua, btnXoa;

        public myProductViewHolder(@NonNull View itemView) {
            super(itemView);
            AnhSp = itemView.findViewById(R.id.ivProductImage);
            txSp = itemView.findViewById(R.id.tvProductName);
            txtGia = itemView.findViewById(R.id.tvGia);
            txtNgayhetHan = itemView.findViewById(R.id.tvNgayHetHan);
            txtNgayMua = itemView.findViewById(R.id.tvNgayMua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            btnSua = itemView.findViewById(R.id.btnSua);
        }
    }
}
