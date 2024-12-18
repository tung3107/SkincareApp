package btl.skincareapp.view.routine;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import btl.skincareapp.R;
import btl.skincareapp.adapter.routineProductAdapter;
import btl.skincareapp.adapter.spinnerProductAdapter;
import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;
import btl.skincareapp.model.Routine;

public class EditRoutine extends AppCompatActivity {

    EditText edtRoutineName;
    Button btnTime, btnEditRoutine;
    ImageView btnBack;
    Spinner spnRoutineSP;
    RecyclerView rvRoutineSP;

    List<MyProduct> productList = new ArrayList<MyProduct>();
    List<MyProduct> selectedList = new ArrayList<MyProduct>();

    boolean isSpinnerInitialized = false;

    private void initialize() {
        edtRoutineName = findViewById(R.id.editTextTenRoutine2);
        btnTime = findViewById(R.id.buttonThoiGian2);
        btnEditRoutine = findViewById(R.id.buttonEdiRoutine);
        spnRoutineSP = findViewById(R.id.spinnerSanPham2);
        rvRoutineSP = findViewById(R.id.rvRoutineSP2);
        btnBack = findViewById(R.id.btnBack2);
    }
    private void addProductToSpinner(int user_id) throws ParseException {
        DatabaseHelper db = new DatabaseHelper(this);
        productList = db.getMyProduct(user_id);
        productList.add(0 , new MyProduct(-1, "Chọn sản phẩm...", "d"));
        spinnerProductAdapter adapter = new spinnerProductAdapter(this, productList);
        adapter.setDropDownViewResource(R.layout.item_spinner_product);

        spnRoutineSP.setAdapter(adapter);
    }

    private void addRoutineToView(Routine routine) {
        edtRoutineName.setText(routine.getName().toString());
        btnTime.setText(routine.getTime());

        rvRoutineSP.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvRoutineSP.setLayoutManager(layoutManager);
        selectedList = routine.getProductList();
        rvRoutineSP.setAdapter(new routineProductAdapter(selectedList, this));
    }
    private void onListenEvents(int user_id, Routine routine) {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spnRoutineSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("THEEMMMMMMMMMMMMMMMM");

                if(!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }
                MyProduct selectedProduct = productList.get(i);
                if (i > 0) {
                    boolean isAlreadyAdded = false;

                    for (MyProduct product : selectedList) {
                        if (product.getId() == selectedProduct.getId()) {
                            isAlreadyAdded = true;
                            break;
                        }
                    }
                    if (!isAlreadyAdded) {
                        selectedList.add(selectedProduct);

                        routineProductAdapter adapter = (routineProductAdapter) rvRoutineSP.getAdapter();
                        if (adapter != null) {
                            adapter.notifyItemInserted(selectedList.size() - 1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d", i, i1));
                    }
                };
                TimePickerDialog pic = new TimePickerDialog(EditRoutine.this, callback, hour, minute, true);
                pic.setTitle("Chọn giờ skincare");
                pic.show();
            }
        });

        btnEditRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Routine newRoutine = getRoutine(routine);
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                boolean result = db.updateOneRoutine(user_id, newRoutine);
                if(result) {
                    Toast.makeText(EditRoutine.this, "Thêm routine thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(EditRoutine.this, "Không thêm được routine", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Routine getRoutine(Routine routine) {
        Routine newRoutine = new Routine();
        newRoutine.setId(routine.getId());
        newRoutine.setName(edtRoutineName.getText().toString());
        newRoutine.setTime(btnTime.getText().toString());
        newRoutine.setProductList(selectedList);
        return newRoutine;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_routine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
        Intent intent = this.getIntent();
        int user_id = intent.getIntExtra("user_id", -1);
        try {
            addProductToSpinner(user_id);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Routine routine = (Routine) intent.getSerializableExtra("routine");
        addRoutineToView(routine);
        onListenEvents(user_id, routine);
    }
}