package btl.skincareapp.view.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import btl.skincareapp.R;
import btl.skincareapp.view.TrungGian;
import btl.skincareapp.helper.DatabaseHelper;
import btl.skincareapp.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    Button btnLogOut, btnLuu;
    TextView txtNameLon, txtEmailLon;
    EditText editName, editEmail;
    Spinner spnLoaiDa;
    ArrayList<String> listLoaiDa = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;

    public SettingFragment() {
        // Required empty public constructor
    }
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int getSpinnerID(String loaida) {
        for (int i = 0; i <= listLoaiDa.size(); i++) {
            if(loaida.equals(listLoaiDa.get(i))) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btnLogOut = view.findViewById(R.id.buttonLogout);
        btnLuu = view.findViewById(R.id.buttonSave);
        txtNameLon = view.findViewById(R.id.txtTenNguoiDung);
        txtEmailLon = view.findViewById(R.id.hienthiemail);
        editName = view.findViewById(R.id.editTextTen);
        editEmail = view.findViewById(R.id.editTextEmail);
        spnLoaiDa = view.findViewById(R.id.spnLoaiDa2);

        listLoaiDa.add("Da khô");
        listLoaiDa.add("Da dầu");
        listLoaiDa.add("Da hỗn hợp thiên khô");
        listLoaiDa.add("Da hỗn hợp thiên dầu");

        adapter = new ArrayAdapter<String>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listLoaiDa);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spnLoaiDa.setAdapter(adapter);

        //
        SharedPreferences share = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        spnLoaiDa.setSelection(getSpinnerID(share.getString("loai_da", "")));
        txtNameLon.setText(share.getString("name", ""));
        editName.setText(share.getString("name", ""));
        editEmail.setText(share.getString("email", ""));
        txtEmailLon.setText(share.getString("email", ""));


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                editor.clear();
                editor.apply();

                Intent intent = new Intent(view.getContext(), TrungGian.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                User newUser = db.updateUser(new User(share.getInt("currentUserID", -1),editName.getText().toString(), spnLoaiDa.getSelectedItem().toString(), editEmail.getText().toString(), ""));
                if(newUser != null) {
                    editor.putString("email", newUser.getEmail());
                    editor.putString("name", newUser.getName());
                    editor.putString("loai_da", newUser.getLoaiDa());
                    editor.apply();

                    txtNameLon.setText(newUser.getName());
                    txtEmailLon.setText(newUser.getEmail());
                    spnLoaiDa.setSelection(getSpinnerID(newUser.getLoaiDa()));
                    Toast.makeText(view.getContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Lỗi khi lưu! Thử lại sau", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
}