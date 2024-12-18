package btl.skincareapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import btl.skincareapp.R;
import btl.skincareapp.adapter.SanPhamAdapter;
import btl.skincareapp.adapter.routineAdapter;
import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;
import btl.skincareapp.model.Product;
import btl.skincareapp.model.Routine;
import btl.skincareapp.view.routine.AddRoutine;
import btl.skincareapp.view.product.AddProductFragment;
import btl.skincareapp.view.routine.EditRoutine;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    List<Product> listHetHan = new ArrayList<Product>();
    List<MyProduct> listProductRoutine = new ArrayList<MyProduct>();
    List<Routine> listRoutine = new ArrayList<Routine>();
    TextView tvGreeting;
    Button btnQuanLyRoutine;
    TextView tvThongBao;
    LinearLayout routineContainer;
    RecyclerView rvSanPhamHetHan;
    int user_id;

    ActivityResultLauncher<Intent> intentLancher;

    public HomeFragment() {
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void themSanPhamList(int user_id) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<Product> resultMap = db.getExpiredProducts(user_id);
        for (Product product:
             resultMap) {
            listHetHan.add(new Product(product.getTenSp().toString(), "Còn " + product.getMoTaSP().toString() + " ngày", product.getImageURL().toString()));
        }

    }
    private void recycleViewLoad(View view) {
        rvSanPhamHetHan = view.findViewById(R.id.rvExpiringProducts);
        TextView tvProductsTitle = view.findViewById(R.id.tvProductsTitle);
        ConstraintLayout constraintLayout = view.findViewById(R.id.layoutNoProduct);
        if(listHetHan.isEmpty()) {
            rvSanPhamHetHan.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            tvProductsTitle.setLayoutDirection(R.id.layoutNoProduct);

        } else {
            rvSanPhamHetHan.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
            rvSanPhamHetHan.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerHetHan = new LinearLayoutManager(view.getContext());
            rvSanPhamHetHan.setLayoutManager(layoutManagerHetHan);
            rvSanPhamHetHan.setAdapter(new SanPhamAdapter(listHetHan, view.getContext(), "co", null));
        }
    }

    private void loadRoutine(View view, int user_id) {
        routineContainer = view.findViewById(R.id.routineContainer);
        if(routineContainer != null) {
            routineContainer.removeAllViews();
        }
        DatabaseHelper db = new DatabaseHelper(view.getContext());
        listRoutine = db.getAllRoutine(user_id);
        if(listRoutine.isEmpty()) {
            routineContainer.setVisibility(View.GONE);
            tvThongBao.setVisibility(View.VISIBLE);
        } else {
            routineContainer.setVisibility(View.VISIBLE);
            tvThongBao.setVisibility(View.INVISIBLE);
            for (Routine routine : listRoutine) {

                View routineView = LayoutInflater.from(getContext()).inflate(R.layout.item_routine, routineContainer, false);
                TextView txtRoutineName = routineView.findViewById(R.id.routineName);
                txtRoutineName.setText(routine.getName().toString());
                TextView txtRoutineTime = routineView.findViewById(R.id.routineTime);
                txtRoutineTime.setText(routine.getTime().toString());

                RecyclerView rvSanPhamRoutine = routineView.findViewById(R.id.rvRoutine);
                rvSanPhamRoutine.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                rvSanPhamRoutine.setLayoutManager(layoutManager);
                rvSanPhamRoutine.setAdapter(new routineAdapter(routine.getProductList(), view.getContext()));

                Button btnXoa = routineView.findViewById(R.id.btnXoa);
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHelper db = new DatabaseHelper(view.getContext());
                        db.deleteOneRoutine(user_id, routine.getId());
                        routineContainer.removeView(routineView);
                        listRoutine.remove(routine);

                        if (listRoutine.isEmpty()) {
                            routineContainer.setVisibility(View.GONE);
                            tvThongBao.setVisibility(View.VISIBLE);
                        }
                    }
                });
                Button btnSua = routineView.findViewById(R.id.btnSua);
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), EditRoutine.class);
                        intent.putExtra("routine", routine);
                        intent.putExtra("user_id", user_id);
                        intentLancher.launch(intent);
                    }
                });

                routineContainer.addView(routineView);
            }
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
        tvThongBao = view.findViewById(R.id.tvThongBao2);

        // get user id
        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        user_id = share.getInt("currentUserID", -1);

        textWelcome(view);
        themSanPhamList(user_id);
        //// Them vao recycle het han, goi y mot dong san pham
        recycleViewLoad(view);
        loadRoutine(view, user_id);

        intentLancher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK) {
                loadRoutine(view,user_id);
            }
        });

        Button btnThemSanPham = view.findViewById(R.id.btnThemSanPham);
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new AddProductFragment()).addToBackStack(null).commit();
            }
        });
        btnQuanLyRoutine = view.findViewById(R.id.btnSuaRoutine);

        btnQuanLyRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                List<MyProduct> productList;
                try {
                    productList = db.getMyProduct(user_id);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if(productList.isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setTitle("Thông báo");
                    alert.setMessage("Thông sản phẩm trước khi tạo routine");
                    alert.show();
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                } else {
                    Intent intent = new Intent(view.getContext(), AddRoutine.class);
                    intent.putExtra("user_id", user_id);
                    intentLancher.launch(intent);
                }

            }
        });
        return view;
    }

}