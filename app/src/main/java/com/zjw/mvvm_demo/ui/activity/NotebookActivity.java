package com.zjw.mvvm_demo.ui.activity;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityNotebookBinding;


public class NotebookActivity extends BaseActivity {

    private ActivityNotebookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notebook);

        setStatusBar(true);

        initView();
    }

    private void initView() {
        //获取navController
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.notebook_fragment:
                    controller.navigate(R.id.notebook_fragment);
                    break;
                case R.id.todo_fragment:
                    controller.navigate(R.id.todo_fragment);
                    break;
            }

            return true;
        });

    }
}