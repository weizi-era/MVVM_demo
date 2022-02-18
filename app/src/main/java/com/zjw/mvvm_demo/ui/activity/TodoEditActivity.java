package com.zjw.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityTodoEditBinding;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.viewmodels.TodoEditViewModel;

public class TodoEditActivity extends BaseActivity implements View.OnClickListener {

    private ActivityTodoEditBinding binding;
    private TodoEditViewModel viewModel;

    private boolean isComplete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_edit);

        viewModel = new ViewModelProvider(this).get(TodoEditViewModel.class);
        setStatusBar(true);

        initView();

    }

    private void initView() {
        binding.editBack.setOnClickListener(this);

        binding.switchImport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.ivImport.setImageResource(R.mipmap.ic_import);
            } else {
                binding.ivImport.setImageResource(R.mipmap.ic_no_import);
            }
        });

        binding.ivState.setOnClickListener(this);

        binding.editOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state:
                if (!isComplete) {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_done);
                    isComplete = true;
                } else {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_doing);
                    isComplete = false;
                }
                break;
            case R.id.edit_ok:
                addTodo();
                break;
            case R.id.edit_back:
                finish();
                break;
        }
    }

    private void addTodo() {
        Todo todo = new Todo();
        todo.setTitle(binding.todoTitle.getText().toString());
        todo.setRemark(binding.todoRemark.getText().toString());
        todo.setDate("2022.02.18");
        todo.setTime("16:48:42");
        viewModel.addTodo(todo);
        finish();
    }
}