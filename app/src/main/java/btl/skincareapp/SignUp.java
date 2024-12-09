package btl.skincareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.User;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    Spinner spnLoaiDa;
    EditText txtEmail, txtPassword, txtPasswordConfirm, txtName;
    Button btnSubmit;
    TextView txtChuyenHuong;

    private void thongBao(String tb) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Thông báo");
        alert.setMessage(tb);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        spnLoaiDa = findViewById(R.id.spnLoaiDa);
        txtEmail = findViewById(R.id.edemail);
        txtPassword = findViewById(R.id.edpassword);
        txtPasswordConfirm = findViewById(R.id.edrppassword);
        txtName = findViewById(R.id.edName);
        btnSubmit = findViewById(R.id.btnsignup);
        txtChuyenHuong = findViewById(R.id.txtLogin);
        ArrayList<String> listLoaiDa = new ArrayList<String>();
        ArrayAdapter<String> adapter = null;
        listLoaiDa.add("Da khô");
        listLoaiDa.add("Da dầu");
        listLoaiDa.add("Da hỗn hợp thiên khô");
        listLoaiDa.add("Da hỗn hợp thiên dầu");
        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listLoaiDa);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spnLoaiDa.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                if (txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty() || txtPasswordConfirm.getText().toString().isEmpty() || !txtPassword.getText().toString().equals(txtPasswordConfirm.getText().toString()) || txtName.getText().toString().isEmpty()) {
                    thongBao("Nhập đầy đủ các trường thông tin");
                } else {
                    DatabaseHelper db = new DatabaseHelper(view.getContext());
                    User user = new User(-1, txtName.getText().toString(), spnLoaiDa.getSelectedItem().toString(), txtEmail.getText().toString(), txtPassword.getText().toString());
                    User newUser = db.dangky(user);
                    if(newUser == null) {
                        Toast.makeText(SignUp.this, "email được đăng ký! chọn email khác", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        SharedPreferences shared = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt("currentUserID", newUser.getId());
                        editor.putString("email", newUser.getEmail());
                        editor.putString("name", newUser.getName());
                        editor.putString("loai_da", newUser.getLoaiDa());
                        editor.apply();

                        //// Intend
                        /// Intent itent = new Intent(SplashActivity.this, Login.class)
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
        txtChuyenHuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
    }




}