package btl.skincareapp.view.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import btl.skincareapp.R;
import btl.skincareapp.adapter.myProductAdapter;
import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListFragment extends Fragment {


    Button txtXoa,txtSua;
    SearchView searchView;

    List<MyProduct> productList = new ArrayList<MyProduct>();
    RecyclerView rvProducts;
    public MyListFragment() {
        // Required empty public constructor
    }

    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();
        return fragment;
    }

    private void rvAd(View view, ActivityResultLauncher<Intent> editLaucher) {
        rvProducts = view.findViewById(R.id.rvProductsSang);
        rvProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutMyList = new LinearLayoutManager(view.getContext());
        rvProducts.setLayoutManager(layoutMyList);
        rvProducts.setAdapter(new myProductAdapter(productList, view.getContext(), editLaucher));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        searchView = view.findViewById(R.id.inputSearch);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSearch(newText);
                return false;
            }
        });

        int user_id = getUserId();

        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        try {
            productList = databaseHelper.getMyProduct(user_id); /// Them user_id sau
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ActivityResultLauncher<Intent> editLaucher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                MyProduct updatedProduct = (MyProduct) result.getData().getSerializableExtra("updatedProduct");
                int product_position = result.getData().getIntExtra("product_position", -1);
                productList.set(product_position, updatedProduct);

                 rvProducts = view.findViewById(R.id.rvProductsSang);
                //// UPDATE THE LIST NHE
                myProductAdapter adapter = (myProductAdapter) rvProducts.getAdapter();
                if (adapter != null) {
                    adapter.notifyItemChanged(product_position); //
                }

            }
        });

        rvAd(view, editLaucher);

        return view;
    }

    private int getUserId() {
        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int user_id = share.getInt("currentUserID", -1);
        return user_id;
    }

    private void filterSearch(String newText) {
        List<MyProduct> filterList = new ArrayList<>();
        for(MyProduct myProduct : productList) {
            if(myProduct.getTenSp().toLowerCase().contains(newText)) {
                filterList.add(myProduct);
            }
        }

        if(!filterList.isEmpty())  {
            myProductAdapter adapter = (myProductAdapter) rvProducts.getAdapter();
            adapter.setFilteredList(filterList);
        }
    }

}