package btl.skincareapp.view.product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import btl.skincareapp.R;
import btl.skincareapp.helper.DatabaseHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment {

    Map<String, Object> resultMap = new HashMap<>();
    TextView tvTongSpent, tvSoSanPham, tvTrungBinh, tvSpDat, tvMontlySpent;
    BarChart barChart;
    Map<String, Integer> barChartValue = null;

    public AnalysisFragment() {
        // Required empty public constructor
    }
    public static AnalysisFragment newInstance(String param1, String param2) {
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void ThongKe(View view, int user_id) {
        DatabaseHelper db = new DatabaseHelper(view.getContext());
        resultMap = db.thongKe(user_id);  /// them user_id
        if(Objects.equals(resultMap.get("count_product"), 0) || resultMap.get("max_gia_name") == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setTitle("Thông báo");
            alert.setMessage("Hãy nhập sản phẩm skincare của bạn trước khi xem thống kê nhé!");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new AddProductFragment());
                    fragmentTransaction.commit();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        } else {
            resultMap.get("max_gia_name").toString();
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyFor = NumberFormat.getCurrencyInstance(localeVN);

            tvSoSanPham.setText(resultMap.get("count_product").toString());
            tvSpDat.setText(resultMap.get("max_gia_name").toString());
            tvTongSpent.setText(currencyFor.format(Integer.parseInt(resultMap.get("sum_price").toString())));
            tvTrungBinh.setText(currencyFor.format(Integer.parseInt(resultMap.get("avg_price").toString())));
            tvMontlySpent.setText(currencyFor.format(Integer.parseInt(resultMap.get("monthly_spent").toString())));
            barChartValue = (Map<String, Integer>) resultMap.get("barChartValue");

            ArrayList<BarEntry> barEntries = new ArrayList<>();
            List<String> months = new ArrayList<>();
            int index = 0;
            for (Map.Entry<String, Integer> entry: barChartValue.entrySet()) {
                barEntries.add(new BarEntry(index, entry.getValue()));
                months.add(entry.getKey().toString());
                index++;
            }
            BarDataSet dataSet = new BarDataSet(barEntries, "thống kê");
            dataSet.setColor(Color.parseColor("#fac738"));
            dataSet.setValueTextColor(Color.parseColor("#A07D1C"));
            dataSet.setValueTextSize(12f);

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.9f);

            barChart.setData(barData);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getDescription().setEnabled(false);
            barChart.getAxisLeft().setEnabled(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.setFitBars(true);
            barChart.invalidate();
        }

    }
    private void initialization(View view) {
        tvTrungBinh = view.findViewById(R.id.tvTrungBinh);
        tvSoSanPham = view.findViewById(R.id.tvSoSanPham);
        tvSpDat = view.findViewById(R.id.tvSpDat);
        tvTongSpent = view.findViewById(R.id.tvTongSpent);
        tvMontlySpent = view.findViewById(R.id.tvAnalysis3);
        barChart = view.findViewById(R.id.barchart);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        initialization(view);
        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int user_id = share.getInt("currentUserID", -1);
        ThongKe(view, user_id);


        return view;
    }
}