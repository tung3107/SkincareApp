package btl.skincareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import btl.skincareapp.helper.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MenuProvider {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    List<Product> listHetHan = new ArrayList<Product>();
    List<Product> listGoiY = new ArrayList<Product>();
    TextView tvGreeting;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void themSanPhamList(int user_id) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<Product> resultMap = db.getExpiredProducts(user_id);
        for (Product product:
             resultMap) {
            listHetHan.add(new Product(product.getTenSp().toString(), "Còn " + product.getMoTaSP().toString() + " ngày", product.getImageURL().toString()));
        }

        //
        listGoiY.add(new Product("La Roche-Posay Foaming Cleanser", "For dry skin", R.drawable.product1));
        listGoiY.add(new Product("CeraVe Hydrating Cleanser", "For sensitive skin", R.drawable.product1));
        listGoiY.add(new Product("The Ordinary Niacinamide 10% + Zinc 1%", "For oily skin", R.drawable.product1));

    }
    private void rvGoiY(View view) {
        RecyclerView rvSanPhamGoiY = view.findViewById(R.id.rvProducts);
        rvSanPhamGoiY.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerGoiY = new LinearLayoutManager(view.getContext());
        rvSanPhamGoiY.setLayoutManager(layoutManagerGoiY);
        rvSanPhamGoiY.setAdapter(new SanPhamAdapter(listGoiY, view.getContext(),""));

        ///
        RecyclerView rvSanPhamHetHan = view.findViewById(R.id.rvExpiringProducts);
        ConstraintLayout constraintLayout = view.findViewById(R.id.layoutNoProduct);
        if(listHetHan.isEmpty()) {
            rvSanPhamHetHan.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            rvSanPhamGoiY.setLayoutDirection(R.id.layoutNoProduct);

        } else {
            rvSanPhamHetHan.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
            rvSanPhamHetHan.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerHetHan = new LinearLayoutManager(view.getContext());
            rvSanPhamHetHan.setLayoutManager(layoutManagerHetHan);
            rvSanPhamHetHan.setAdapter(new SanPhamAdapter(listHetHan, view.getContext(), "co"));
        }
    }

    private void textWelcome(View view) {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvGreeting.setText("Chào buổi " + ((6 <= timeOfDay && timeOfDay <= 12) ? "sáng!" : (13 <= timeOfDay && timeOfDay <= 18) ? "chiều!" : "tối!"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int user_id = share.getInt("currentUserID", -1);
        textWelcome(view);
        themSanPhamList(user_id);
        //// Them vao recycle het han, goi y mot dong san pham
        rvGoiY(view);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner());

        Button btnThemSanPham = view.findViewById(R.id.btnThemSanPham);
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AddProductFragment()).addToBackStack(null).commit();
            }
        });

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