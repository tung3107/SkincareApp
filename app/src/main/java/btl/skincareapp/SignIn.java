package btl.skincareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SignIn extends AppCompatActivity {
    
    EditText txtEmail, txtPassword;
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
        setContentView(R.layout.activity_sign_in);

        txtEmail = findViewById(R.id.edemailLg);
        txtPassword = findViewById(R.id.edpasswordLg);
        btnSubmit = findViewById(R.id.btnLogin);
        txtChuyenHuong = findViewById(R.id.txtSignup);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                if (txtEmail.getText().toString() == "" || txtPassword.getText().toString() == "") {
                    thongBao("Nhập đầy đủ các trường thông tin");
                } else { 

                    DatabaseHelper db = new DatabaseHelper(view.getContext());
                    User user = db.dangnhap(txtEmail.getText().toString(), txtPassword.getText().toString());

                    if(user == null) {
                        Toast.makeText(SignIn.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignIn.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        SharedPreferences shared = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putInt("currentUserID", user.getId());
                        editor.putString("email", user.getEmail());
                        editor.putString("name", user.getName());
                        editor.putString("loai_da", user.getLoaiDa());
                        editor.apply();

                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        // Intent intent = new Intent(SignIn.this, MainActivity.class);
                        // startActivity(intent);
                        // finish();
                    }
                }
            } 
        });
        txtChuyenHuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

}