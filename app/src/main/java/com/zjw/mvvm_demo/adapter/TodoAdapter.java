package com.zjw.mvvm_demo.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemNotebookBinding;
import com.zjw.mvvm_demo.databinding.ItemTodoBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;
import com.zjw.mvvm_demo.ui.activity.TodoEditActivity;
import com.zjw.mvvm_demo.viewmodels.TodoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoAdapter extends BaseQuickAdapter<Todo, BaseDataBindingHolder<ItemTodoBinding>> {

    private boolean isDone = false;

    public TodoAdapter() {
        super(R.layout.item_todo);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemTodoBinding> bindingHolder, Todo todo) {

        ItemTodoBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setTodo(todo);
            dataBinding.executePendingBindings();

            if (todo.isImport()) {
                dataBinding.ivImport.setVisibility(View.VISIBLE);
            } else {
                dataBinding.ivImport.setVisibility(View.GONE);
            }

            if (todo.isDone()) {
                dataBinding.ivState.setImageResource(R.mipmap.ic_todo_done);
                dataBinding.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                dataBinding.tvTitle.setTextColor(getContext().getColor(R.color.Grey500));
            } else {
                dataBinding.ivState.setImageResource(R.mipmap.ic_todo_doing);
                dataBinding.tvTitle.getPaint().setFlags(0);
                dataBinding.tvTitle.setTextColor(getContext().getColor(R.color.Black));
            }

            if (TextUtils.isEmpty(todo.getDate())) {
                dataBinding.tvTime.setVisibility(View.GONE);
            } else {
                dataBinding.tvTime.setVisibility(View.VISIBLE);
            }
        }
    }
}
