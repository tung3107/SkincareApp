package btl.skincareapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListFragment extends Fragment implements MenuProvider {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button txtXoa,txtSua;

    List<MyProduct> productList = new ArrayList<MyProduct>();

    public MyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyListFragment newInstance(String param1, String param2) {
        MyListFragment fragment = new MyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void rvAd(View view, ActivityResultLauncher<Intent> editLaucher) {
        RecyclerView rvProducts = view.findViewById(R.id.rvProducts);
        rvProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutMyList = new LinearLayoutManager(view.getContext());
        rvProducts.setLayoutManager(layoutMyList);
        rvProducts.setAdapter(new myProductAdapter(productList, view.getContext(), editLaucher));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_list, container, false);

        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int user_id = share.getInt("currentUserID", -1);

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


                //// UPDATE THE LIST NHE
                RecyclerView rvProducts = view.findViewById(R.id.rvProducts);
                myProductAdapter adapter = (myProductAdapter) rvProducts.getAdapter();
                if (adapter != null) {
                    adapter.notifyItemChanged(product_position); //
                }

            }
        });
        rvAd(view, editLaucher);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner());




        return view;
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu) {
        MenuProvider.super.onPrepareMenu(menu);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onMenuClosed(@NonNull Menu menu) {
        MenuProvider.super.onMenuClosed(menu);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}