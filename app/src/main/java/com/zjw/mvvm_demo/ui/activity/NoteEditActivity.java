package com.zjw.mvvm_demo.ui.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.zjw.mvvm_demo.EditWatcher;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ActivityNoteEditBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.utils.EasyDate;
import com.zjw.mvvm_demo.viewmodels.NoteEditViewModel;

public class NoteEditActivity extends BaseActivity implements View.OnClickListener {

    private ActivityNoteEditBinding binding;

    private NoteEditViewModel viewModel;

    private int uid;
    private Notebook mNotebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note_edit);
        viewModel = new ViewModelProvider(this).get(NoteEditViewModel.class);
        setStatusBar(true);

        initView();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        binding.editBack.setOnClickListener(v -> finish());
        binding.noteTitle.addTextChangedListener(new EditWatcher(binding.editOk));
        binding.noteContent.addTextChangedListener(new EditWatcher(binding.editOk));
        binding.editOk.setOnClickListener(this);
        uid = getIntent().getIntExtra("uid", -1);
        if (uid == -1) {
            binding.tvToday.setText("今天");
            binding.noteTime.setText(EasyDate.getHoursMinutes());
            showSoftInputFromWindow(binding.noteContent);
            binding.editOk.setClickable(false);
        } else {
            binding.editOk.setVisibility(View.GONE);
            viewModel.queryById(uid);
            viewModel.notebook.observe(this, notebook -> {
                mNotebook = notebook;
                binding.setNotebook(notebook);
                binding.tvToday.setText(notebook.getDate());
                binding.noteTime.setText(notebook.getTime());
            });

            binding.noteTitle.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.editOk.setVisibility(View.VISIBLE);
                }
                return false;
            });

            binding.noteContent.setOnTouchListener((v, event) -> {
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
            case R.id.edit_ok:
                if (uid == -1) {
                    addNotebook();  // 添加笔记
                } else {
                    updateNotebook();  // 更新笔记
                }
               finish();
               break;
        }
    }

    private void addNotebook() {
        Notebook notebook = new Notebook();
        notebook.setTitle(binding.noteTitle.getText().toString());
        notebook.setContent(binding.noteContent.getText().toString());
        notebook.setDate(EasyDate.getTheYearMonthAndDayCn());
        notebook.setTime(EasyDate.getHoursMinutes());
        viewModel.addNotebook(notebook);
    }

    private void updateNotebook() {
        mNotebook.setTitle(binding.noteTitle.getText().toString());
        mNotebook.setContent(binding.noteContent.getText().toString());
        mNotebook.setDate(EasyDate.getTheYearMonthAndDayCn());
        mNotebook.setTime(EasyDate.getHoursMinutes());
        viewModel.updateNotebook(mNotebook);
    }
}