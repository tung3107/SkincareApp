package btl.skincareapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import btl.skincareapp.R;
import btl.skincareapp.model.MyProduct;

public class spinnerProductAdapter extends ArrayAdapter<MyProduct> {

    Context context;
    List<MyProduct> productList;
    public spinnerProductAdapter(@NonNull Context context, @NonNull List<MyProduct> objects) {
        super(context, R.layout.item_spinner_product, objects);
        this.context = context;
        this.productList = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View productSpinner = LayoutInflater.from(context).inflate(R.layout.item_spinner_product, parent, false);
        TextView txtProductName = productSpinner.findViewById(R.id.productName);
        txtProductName.setText(productList.get(position).getTenSp().toString());

        ImageView imageProduct = productSpinner.findViewById(R.id.productImage);
        imageProduct.setImageURI(Uri.parse(productList.get(position).getUrl_Anh().toString()));

        return productSpinner;
    }
}
