package btl.skincareapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.result.ActivityResultLauncher;
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
    ActivityResultLauncher<Intent> editLauncher;

    public myProductAdapter(List<MyProduct> sanPhamList, Context context) {
        this.sanPhamList = sanPhamList;
        this.context = context;
        this.editLauncher = null;
    }

    public myProductAdapter(List<MyProduct> sanPhamList, Context context, ActivityResultLauncher<Intent> editLauncher) {
        this.sanPhamList = sanPhamList;
        this.context = context;
        this.editLauncher = editLauncher;
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

    public int getUserId() {
        SharedPreferences share = context.getSharedPreferences("UserSession", MODE_PRIVATE);
        return share.getInt("currentUserID", -1);
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

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = sanPhamList.get(holder.getAdapterPosition()).getId();
                Intent intent = new Intent(view.getContext(), EditSP.class);
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                MyProduct myProduct = db.getOneSP(adapterPosition, getUserId());
                intent.putExtra("product", myProduct);
                intent.putExtra("product_position", holder.getAdapterPosition());
                editLauncher.launch(intent);

                //
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

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

                            // Xoa file anh truoc khi xoa list
                            File imagePath = new File(sanPhamList.get(adapterPosition).getUrl_Anh());
                            if(imagePath.exists()) {
                                imagePath.delete();
                            }
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
