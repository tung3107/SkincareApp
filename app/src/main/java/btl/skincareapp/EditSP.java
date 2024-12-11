package btl.skincareapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;

public class EditSP extends AppCompatActivity {


    EditText editTenSp, editBrandName, editGia;
    Button btnNgayHetHan, btnNgayMua, btnSua, btnAnh;
    ImageView imgAnh, imgBack;
    String anhUrl = null;

    public void InitialView() {
        editTenSp = findViewById(R.id.editTextTenSP);
        editBrandName = findViewById(R.id.editTextNhanHieu);
        editGia = findViewById(R.id.editTextGiaTien);
        btnNgayHetHan = findViewById(R.id.buttonNgayHetHan);
        btnNgayMua = findViewById(R.id.buttonNgayMua);
        btnSua = findViewById(R.id.buttonSua);
        btnAnh = findViewById(R.id.btnAnh);
        imgAnh = findViewById(R.id.anhSp);
        imgBack = findViewById(R.id.btnBack);
    }
    public void loadView(MyProduct myProduct) {
        editTenSp.setText(myProduct.getTenSp());
        editBrandName.setText(myProduct.getNhanHieu());
        editGia.setText(myProduct.getGia());
        btnNgayHetHan.setText(myProduct.getNgayHetHan());
        btnNgayMua.setText(myProduct.getNgayMua());

        Bitmap bitmap = BitmapFactory.decodeFile(myProduct.getUrl_Anh());
        imgAnh.setImageBitmap(bitmap);
    }
//    public int getUserId() {
//        SharedPreferences share = getSharedPreferences("UserSession", MODE_PRIVATE);
//        return share.getInt("currentUserID", -1);
//    }
    public void onClickListener(MyProduct myProduct, int user_id, int product_position) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db =  new DatabaseHelper(view.getContext());
                MyProduct updatedProduct = new MyProduct(myProduct.getId(), editTenSp.getText().toString(), editGia.getText().toString(), editBrandName.getText().toString(),
                        anhUrl == null ? myProduct.getUrl_Anh() : anhUrl, btnNgayMua.getText().toString(), btnNgayHetHan.getText().toString(), user_id);
                boolean update = db.updateSP(updatedProduct);
                if(update) {
                    Toast.makeText(EditSP.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent resultData = new Intent();
                    resultData.putExtra("updatedProduct", updatedProduct);
                    resultData.putExtra("product_position", product_position);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                } else {
                    Toast.makeText(EditSP.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }

            int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
            ActivityResultLauncher<PickVisualMediaRequest> pickLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if(uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri, flag);
                        File directory = new File(getFilesDir(), "images");
                        if(!directory.exists()) {
                            directory.mkdir();
                        }
                        String fileName = "saved_image_" + System.currentTimeMillis() + ".png";
                        File imagePath = new File(directory, fileName);
                        FileOutputStream fo = new FileOutputStream(imagePath);
                        imgAnh.setImageURI(uri);
                        anhUrl = imagePath.getAbsolutePath();
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo);
                        fo.close();



                    } catch (Exception e) {
                        Toast.makeText(EditSP.this, "Không thể tạo ảnh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
        });
        btnNgayMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        btnNgayMua.setText(sdf.format(selectedCalendar.getTime()));
                    }
                };
                DatePickerDialog pic = new DatePickerDialog(view.getContext(),  callback, year, month, day);
                pic.setTitle("Chọn ngày mua");
                pic.show();
            }
        });
        btnNgayHetHan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        btnNgayHetHan.setText(sdf.format(selectedCalendar.getTime()));
                    }
                };
                DatePickerDialog pic = new DatePickerDialog(view.getContext(), callback, year, month, day);
                pic.setTitle("Chọn ngày hết hạn");
                pic.show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_sp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitialView();
        Intent intent = this.getIntent();
        MyProduct myProduct = (MyProduct) intent.getSerializableExtra("product");
        int product_position = intent.getIntExtra("product_position", -1);
        int user_id = myProduct.getUser_id();
        loadView(myProduct);

        onClickListener(myProduct, user_id, product_position);
    }
}