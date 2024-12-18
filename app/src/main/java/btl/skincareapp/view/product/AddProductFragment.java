package btl.skincareapp.view.product;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import btl.skincareapp.R;
import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.MyProduct;
import btl.skincareapp.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    public static boolean isInteger(String str) {
        if(str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    EditText txtTenSp, txtGia, txtNhanHang;
    Button btnNgayMua, btnNgayHetHan, btnAnh, btnThem;
    private String selectedNgayMua = null, selectedNgayHetHan = null;
    String anhURL = null;
    ImageView anhSp;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void onListen(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int user_id = share.getInt("currentUserID", -1);

        btnNgayMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        btnNgayMua.setText(i2+"/"+(i1 + 1)+"/"+i);
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        selectedNgayMua = sdf.format(selectedCalendar.getTime());
                    }
                };
                DatePickerDialog pic = new DatePickerDialog(view.getContext(), callback, year,month,day);
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
                        btnNgayHetHan.setText(i2+"/"+(i1 + 1)+"/"+i);
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        selectedNgayHetHan = sdf.format(selectedCalendar.getTime());
                    }
                };
                DatePickerDialog pic = new DatePickerDialog(view.getContext(), callback, year,month,day);
                pic.setTitle("Chọn ngày hết hạn");
                pic.show();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProduct myProduct;
                User user;
                if(!txtTenSp.getText().toString().isEmpty() && !txtGia.getText().toString().isEmpty() && !txtNhanHang.getText().toString().isEmpty() && anhURL != null && selectedNgayHetHan != null && selectedNgayMua != null && isInteger(txtGia.getText().toString())) {
                    try {
                        myProduct = new MyProduct(-1, txtTenSp.getText().toString(), txtGia.getText().toString(), txtNhanHang.getText().toString(), anhURL, selectedNgayMua, selectedNgayHetHan, user_id); /// Test thu voi user 1
                        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
                        databaseHelper.addOneSP(myProduct);
                        Toast.makeText(view.getContext(), "Đã thêm", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), "Nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
//                    user = new User(-1, "error", null, null);
                        myProduct = null;

                    }

                } else {
                    Toast.makeText(view.getContext(), "Nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                    myProduct = null;
                }
            }
        });
        // btnAnh
        btnAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                imagePickerLauncher.launch(intent);
            }
            int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;

            ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {


                        if (uri != null) {
                            try {
                                view.getContext().getContentResolver().takePersistableUriPermission(uri, flag);
                                File directory = new File(getContext().getFilesDir(), "images");
                                if(!directory.exists()) {
                                    directory.mkdir();
                                }
                                String fileName = "saved_image_" + System.currentTimeMillis() + ".png";
                                File file = new File(directory, fileName);
                                FileOutputStream fos = new FileOutputStream(file);
                                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                anhSp.setImageBitmap(bitmap);
                                anhURL = file.getAbsolutePath();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                fos.close();

                            } catch (Exception e) {
                                Toast.makeText(view.getContext(), "Không thể tạo ảnh", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("PhotoPicker", "No media selected");
                        }
                    });

//            private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), uri -> {
//                if(uri.getResultCode() == RESULT_OK && uri.getData() != null) {
//                    Uri anhChon = uri.getData().getData();
//                    if(anhChon != null) {
//                        anhURL = anhChon.toString();
//                        try {
//                            InputStream inputStream = getContext().getContentResolver().openInputStream(anhChon);
//                            Bitmap bitmap = Bit
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            });
        });
    }
    private void initialGetThing(View view) {
        btnNgayMua = view.findViewById(R.id.buttonNgayMua);
        btnNgayHetHan = view.findViewById(R.id.buttonNgayHetHan);
        btnAnh = view.findViewById(R.id.buttonAnh);
        txtGia = view.findViewById(R.id.editTextGiaTien);
        txtNhanHang = view.findViewById(R.id.editTextNhanHieu);
        txtTenSp = view.findViewById(R.id.editTextTenSP);
        btnThem = view.findViewById(R.id.buttonSua);
        anhSp = view.findViewById(R.id.anhSP);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ///initiate cac nut cac text
        initialGetThing(view);
        /// Them listen cho nut chọn date
        onListen(view);


        return view;
    }
}