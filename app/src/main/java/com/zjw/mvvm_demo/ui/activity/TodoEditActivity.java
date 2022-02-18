package com.zjw.mvvm_demo.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.github.gzuliyujiang.calendarpicker.CalendarPicker;
import com.github.gzuliyujiang.calendarpicker.OnSingleDatePickListener;
import com.github.gzuliyujiang.calendarpicker.core.ColorScheme;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityTodoEditBinding;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.utils.EasyDate;
import com.zjw.mvvm_demo.viewmodels.TodoEditViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

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
        binding.rlTime.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            case R.id.rl_time:
                selectTime();
                break;
            case R.id.edit_ok:
                addTodo();
                break;
            case R.id.edit_back:
                finish();
                break;
            case R.id.iv_delete:
                binding.todoRemind.setText("添加提醒");
                binding.ivDelete.setVisibility(View.GONE);
                binding.ivRemind.setImageResource(R.mipmap.ic_no_remind);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void selectTime() {

//        DatePicker picker = new DatePicker(this);
//        picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                binding.todoRemind.setText(year + monthOfYear + dayOfMonth);
//                binding.ivDelete.setVisibility(View.VISIBLE);
//            }
//        });

        CalendarPicker picker = new CalendarPicker(this);
        picker.setColorScheme(new ColorScheme()
                .daySelectBackgroundColor(0xFF0000FF)
                .dayStressTextColor(0xFF0000DD));
        picker.setOnSingleDatePickListener(new OnSingleDatePickListener() {
            @Override
            public void onSingleDatePicked(@NonNull @NotNull Date date) {
                String tomorrow = EasyDate.getTomorrow(date);
                binding.todoRemind.setText(tomorrow);
                binding.ivDelete.setVisibility(View.VISIBLE);
                binding.ivRemind.setImageResource(R.mipmap.ic_remind);
            }
        });
        picker.show();
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