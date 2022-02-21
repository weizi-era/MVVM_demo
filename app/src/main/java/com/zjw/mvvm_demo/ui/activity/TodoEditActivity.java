package com.zjw.mvvm_demo.ui.activity;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;
import com.zjw.mvvm_demo.EditWatcher;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.SelectPhotoContract;
import com.zjw.mvvm_demo.databinding.ActivityTodoEditBinding;
import com.zjw.mvvm_demo.databinding.DialogRemindBinding;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.utils.EasyDate;
import com.zjw.mvvm_demo.utils.PermissionUtils;
import com.zjw.mvvm_demo.view.dialog.AlertDialog;
import com.zjw.mvvm_demo.viewmodels.TodoEditViewModel;

import java.util.Date;



public class TodoEditActivity extends BaseActivity implements View.OnClickListener {

    private ActivityTodoEditBinding binding;
    private TodoEditViewModel viewModel;

    private boolean isDone = false;

    private AlertDialog remindDialog = null;

    private int uid;
    private Todo mTodo;

    /**
     * 常规使用 通过意图进行跳转
     */
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_edit);

        viewModel = new ViewModelProvider(this).get(TodoEditViewModel.class);
        setStatusBar(true);

        initView();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        binding.editBack.setOnClickListener(this);
        binding.switchImport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.ivImport.setImageResource(R.mipmap.ic_import);
            } else {
                binding.ivImport.setImageResource(R.mipmap.ic_no_import);
            }
        });

        binding.todoTitle.addTextChangedListener(new EditWatcher(binding.editOk));
        binding.todoRemark.addTextChangedListener(new EditWatcher(binding.editOk));

        binding.ivState.setOnClickListener(this);
        binding.editOk.setOnClickListener(this);
        binding.rlTime.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);

        uid = getIntent().getIntExtra("uid", -1);
        if (uid == -1) {
            binding.todoRemind.setText("添加提醒");
            showSoftInputFromWindow(binding.todoTitle);
            binding.editOk.setClickable(false);
        } else {
            binding.editOk.setVisibility(View.GONE);
            viewModel.queryById(uid);
            viewModel.todo.observe(this, todo -> {
                mTodo = todo;
                binding.setTodo(todo);
                isDone = todo.isDone();
                if (todo.isDone()) {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_done);
                    binding.todoTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.todoTitle.setTextColor(getColor(R.color.Grey500));
                } else {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_doing);
                    binding.todoTitle.getPaint().setFlags(0);
                    binding.todoTitle.setTextColor(getColor(R.color.Black));
                }

                if (todo.getDate().isEmpty()) {
                    binding.todoRemind.setText("添加提醒");
                    binding.ivRemind.setImageResource(R.mipmap.ic_no_remind);
                    binding.ivDelete.setVisibility(View.GONE);
                } else {
                    binding.todoRemind.setText(todo.getDate());
                    binding.ivRemind.setImageResource(R.mipmap.ic_remind);
                    binding.ivDelete.setVisibility(View.VISIBLE);
                }

            });

            binding.todoRemind.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });

            binding.ivDelete.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });

            binding.switchImport.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });

            binding.todoTitle.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });

            binding.todoRemark.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state:
                if (!isDone) {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_done);
                    binding.todoTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.todoTitle.setTextColor(getColor(R.color.Grey500));
                    isDone = true;
                } else {
                    binding.ivState.setImageResource(R.mipmap.ic_todo_doing);
                    binding.todoTitle.getPaint().setFlags(0);
                    binding.todoTitle.setTextColor(getColor(R.color.Black));
                    isDone = false;
                }
                binding.editOk.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_time:
                selectTime();
                break;
            case R.id.edit_ok:
                if (uid == -1) {
                    addTodo();  // 添加待办
                } else {
                    updateTodo();  // 更新待办
                }
                finish();
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

    private void updateTodo() {
        mTodo.setTitle(binding.todoTitle.getText().toString());
        mTodo.setRemark(binding.todoRemark.getText().toString());
        if (binding.todoRemind.getText().equals("添加提醒")) {
            mTodo.setDate("");
        } else {
            mTodo.setDate(binding.todoRemind.getText().toString());
        }
        mTodo.setImport(binding.switchImport.isChecked());
        mTodo.setDone(isDone);
        viewModel.updateTodo(mTodo);
    }

    private void selectTime() {

        DialogRemindBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_remind, null, false);
        binding.rbTime.setOnCheckedChangeListener((buttonView, isChecked) -> binding.rbLocation.setChecked(!isChecked));

        binding.rbLocation.setOnCheckedChangeListener((buttonView, isChecked) -> binding.rbTime.setChecked(!isChecked));
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setContentView(binding.getRoot())
                .setCancelable(false)
                .fromBottom(true)
                .setWidthAndHeight(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remindDialog.dismiss();
                    }
                }).setOnClickListener(R.id.tv_sure, v -> {
                    if (binding.rbTime.isChecked()) {
                        remindDialog.dismiss();
                        showDateAndTime();
                    } else if (binding.rbLocation.isChecked()) {
                        remindDialog.dismiss();
                        intentActivityResultLauncher.launch(new Intent(TodoEditActivity.this, RemindLocationActivity.class));
                    }
                });

        remindDialog = builder.create();
        remindDialog.show();

    }

    private void register() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && !TextUtils.isEmpty(data.getStringExtra("location"))) {
                    binding.todoRemind.setText(data.getStringExtra("location"));
                    binding.ivDelete.setVisibility(View.VISIBLE);
                    binding.ivRemind.setImageResource(R.mipmap.ic_remind);
                } else {
                    binding.todoRemind.setText("添加提醒");
                    binding.ivDelete.setVisibility(View.GONE);
                    binding.ivRemind.setImageResource(R.mipmap.ic_no_remind);
                }
            }
        });
    }

    private void showDateAndTime() {
        new CardDatePickerDialog.Builder(this)
                .setTitle("")
                .setBackGroundModel(CardDatePickerDialog.CARD)
                .setThemeColor(getColor(R.color.purple_500))
                .showDateLabel(true)
                .showFocusDateInfo(true)
                .setDisplayType(DateTimeConfig.YEAR,//显示年
                        DateTimeConfig.MONTH,//显示月
                        DateTimeConfig.DAY,//显示日
                        DateTimeConfig.HOUR,//显示时
                        DateTimeConfig.MIN)
                .setLabelText("年", "月", "日", "时", "分", "")
                .setOnChoose("选择", aLong -> {
                    binding.todoRemind.setText(EasyDate.getDateTime(new Date(aLong)));
                    binding.ivDelete.setVisibility(View.VISIBLE);
                    binding.ivRemind.setImageResource(R.mipmap.ic_remind);
                    return null;
                })
                .setOnCancel("取消", () -> null)
                .build()
                .show();
    }

    private void addTodo() {
        Todo todo = new Todo();
        if (TextUtils.isEmpty(binding.todoTitle.getText())) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        todo.setTitle(binding.todoTitle.getText().toString());
        todo.setRemark(binding.todoRemark.getText().toString());
        if (binding.todoRemind.getText().equals("添加提醒")) {
            todo.setDate("");
        } else {
            todo.setDate(binding.todoRemind.getText().toString());
        }

        todo.setDone(isDone);

        todo.setImport(binding.switchImport.isChecked());
        viewModel.addTodo(todo);
    }
}