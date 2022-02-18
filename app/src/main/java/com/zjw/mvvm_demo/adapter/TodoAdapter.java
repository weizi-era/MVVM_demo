package com.zjw.mvvm_demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemNotebookBinding;
import com.zjw.mvvm_demo.databinding.ItemTodoBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.db.bean.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoAdapter extends BaseQuickAdapter<Todo, BaseDataBindingHolder<ItemTodoBinding>> {

    public TodoAdapter(List<Todo> todos) {
        super(R.layout.item_todo, todos);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemTodoBinding> bindingHolder, Todo todo) {

        ItemTodoBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setTodo(todo);
            dataBinding.executePendingBindings();
        }
    }
}
