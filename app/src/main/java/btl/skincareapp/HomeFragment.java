package btl.skincareapp;

import android.content.Context;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

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

    private void themSanPhamList() {
        listHetHan.add(new Product("Glossier Soothing Face Mist", "5 days left", R.drawable.product1));
        listHetHan.add(new Product("Kiehl's Ultra Facial Cream", "3 days left", R.drawable.product1));
        listHetHan.add(new Product("Sunday Riley Good Genes", "1 week left", R.drawable.product1));
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
        rvSanPhamHetHan.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerHetHan = new LinearLayoutManager(view.getContext());
        rvSanPhamHetHan.setLayoutManager(layoutManagerHetHan);
        rvSanPhamHetHan.setAdapter(new SanPhamAdapter(listHetHan, view.getContext(), "co"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        themSanPhamList();
        //// Them vao recycle het han, goi y mot dong san pham
        rvGoiY(view);
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