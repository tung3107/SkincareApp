package btl.skincareapp.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import btl.skincareapp.R;
import btl.skincareapp.databinding.ActivityMainBinding;
import btl.skincareapp.view.product.AddProductFragment;
import btl.skincareapp.view.product.AnalysisFragment;
import btl.skincareapp.view.product.MyListFragment;
import btl.skincareapp.view.user.SettingFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
            } else if(item.getItemId() == R.id.nav_add_product) {
                replaceFragment(new AddProductFragment());
            } else if(item.getItemId() == R.id.nav_analysis) {
                replaceFragment(new AnalysisFragment());
            } else if(item.getItemId() == R.id.nav_settings) {
                replaceFragment(new SettingFragment());
            } else if(item.getItemId() == R.id.nav_mylist) {
                replaceFragment(new MyListFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}