package com.zjw.mvvm_demo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.NotebookAdapter;
import com.zjw.mvvm_demo.adapter.TodoAdapter;
import com.zjw.mvvm_demo.bean.TodoState;
import com.zjw.mvvm_demo.databinding.FragmentTodoBinding;
import com.zjw.mvvm_demo.db.bean.Todo;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;
import com.zjw.mvvm_demo.ui.activity.TodoEditActivity;
import com.zjw.mvvm_demo.view.RecyclerViewAnimation;
import com.zjw.mvvm_demo.viewmodels.TodoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends BaseFragment implements View.OnClickListener {

    private TodoViewModel viewModel;
    private FragmentTodoBinding binding;

    private TodoAdapter adapter;

    private boolean hasTodo;

    private int index;

    private List<TodoState> itemSelectedList = new ArrayList<>();

    private boolean isAllSelected;

    public static TodoFragment newInstance() {

        return new TodoFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back(binding.toolbar);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        adapter = new TodoAdapter();
        binding.rvTodo.setLayoutManager(new LinearLayoutManager(context));
        viewModel.getTodos();
        viewModel.todos.observe(context, todos -> {
            if (todos.size() > 0) {
                itemSelectedList.clear();
                adapter.setNewInstance(todos);
                binding.rvTodo.setAdapter(adapter);
                for (int i = 0; i < todos.size(); i++) {
                    TodoState todoState = new TodoState(todos.get(i).getUid(), false);
                    itemSelectedList.add(todoState);
                }

               // RecyclerViewAnimation.runLayoutAnimation(binding.rvTodo);
                hasTodo = true;
            } else {
                hasTodo = false;
            }
            binding.setHasTodo(hasTodo);

            adapter.addChildClickViewIds(R.id.iv_state);

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    ImageView iv_select = view.findViewById(R.id.iv_select);
                    if (iv_select.getVisibility() == View.VISIBLE) {
                        TodoState todoState = itemSelectedList.get(position);
                        if (todoState.isSelected()) {
                            iv_select.setImageResource(R.mipmap.ic_noselect);
                            todoState.setSelected(false);
                        } else {
                            iv_select.setImageResource(R.mipmap.ic_selected);
                            todoState.setSelected(true);
                        }
                        itemSelectedList.set(position, todoState);

                        List<Boolean> booleans = new ArrayList<>();
                        for (TodoState state : itemSelectedList) {
                            booleans.add(state.isSelected());
                        }
                        int frequency = Collections.frequency(booleans, true);
                        if (frequency == 0) {
                            binding.tvDelete.setText("删除");
                        } else {
                            binding.tvDelete.setText("删除(" + frequency + ")");
                        }

                    } else {
                        Todo todo = (Todo) adapter.getItem(position);
                        Intent intent = new Intent(view.getContext(), TodoEditActivity.class);
                        intent.putExtra("uid", todo.getUid());
                        view.getContext().startActivity(intent);
                    }
                }
            });


            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

                    Todo todo = (Todo) adapter.getItem(position);
                    ImageView iv_state = view.findViewById(R.id.iv_state);

                    if (iv_state != null) {
                        if (todo.isDone()) {
                            iv_state.setImageResource(R.mipmap.ic_todo_doing);
                            todo.setDone(false);
                        } else {
                            iv_state.setImageResource(R.mipmap.ic_todo_done);
                            todo.setDone(true);
                        }
                        viewModel.updateTodo(todo);
                    }
                }
            });

            adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    //onLongPressed();
                    Todo todo = (Todo) adapter.getItem(position);
                    binding.tvEdit.setVisibility(View.VISIBLE);
                    ImageView iv_state = view.findViewById(R.id.iv_state);
                    ImageView iv_select = view.findViewById(R.id.iv_select);
                    iv_state.setVisibility(View.GONE);
                    iv_select.setVisibility(View.VISIBLE);
                    iv_select.setImageResource(R.mipmap.ic_selected);
                    TodoState todoState = itemSelectedList.get(position);
                    todoState.setSelected(true);
                    itemSelectedList.set(position, todoState);

                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        ImageView select = (ImageView) adapter.getViewByPosition(i, R.id.iv_select);
                        ImageView state = (ImageView) adapter.getViewByPosition(i, R.id.iv_state);
                        select.setVisibility(View.VISIBLE);
                        state.setVisibility(View.GONE);
                    }

                    binding.layBottom.setVisibility(View.VISIBLE);
                    binding.fabAddTodo.setVisibility(View.GONE);

                    return true;
                }
            });

        });

        binding.fabAddTodo.setOnClickListener(this);
        binding.tvCheckAll.setOnClickListener(this);
        binding.tvDelete.setOnClickListener(this);
        binding.tvEdit.setOnClickListener(this);
    }

    //全部选中
    private void setAllItemChecked() {

        if (adapter == null) return;

        List<Todo> todos = adapter.getData();

        if (todos.size() > 0) {
            for (int i = 0; i < todos.size(); i++) {
                TodoState todoState = itemSelectedList.get(i);
                todoState.setSelected(true);
                itemSelectedList.set(i, todoState);
                ImageView select = (ImageView) adapter.getViewByPosition(i, R.id.iv_select);
                select.setImageResource(R.mipmap.ic_selected);
            }
            index = todos.size();
        }
        binding.tvDelete.setText("删除(" + index + ")");
        binding.tvCheckAll.setText("取消全选");
    }


    //取消全部选中
    private void setAllItemUnchecked() {
        if (adapter == null) return;
        List<Todo> todos = adapter.getData();
        if (todos != null && todos.size() > 0) {
            for (int i = 0; i < todos.size(); i++) {
                TodoState todoState = itemSelectedList.get(i);
                todoState.setSelected(false);
                itemSelectedList.set(i, todoState);
                ImageView select = (ImageView) adapter.getViewByPosition(i, R.id.iv_select);
                select.setImageResource(R.mipmap.ic_noselect);
            }
            index = 0;
        }
        binding.tvDelete.setText("删除");
        binding.tvCheckAll.setText("全选");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_all:
                if (!isAllSelected) {
                    setAllItemChecked();
                    isAllSelected = true;
                } else {
                    setAllItemUnchecked();
                    isAllSelected = false;
                }
                break;
            case R.id.tv_delete:
                deleteTodo();
                break;

            case R.id.fab_add_todo:
                jumpActivity(TodoEditActivity.class);
                break;
            case R.id.tv_edit:
                cancelDelete();
                break;
        }
    }

    private void cancelDelete() {
        binding.layBottom.setVisibility(View.GONE);
        binding.fabAddTodo.setVisibility(View.VISIBLE);
        binding.tvEdit.setVisibility(View.GONE);

        for (int i = 0; i < itemSelectedList.size(); i++) {
            TodoState todoState = itemSelectedList.get(i);
            todoState.setSelected(false);
            itemSelectedList.set(i, todoState);
        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            ImageView select = (ImageView) adapter.getViewByPosition(i, R.id.iv_select);
            ImageView state = (ImageView) adapter.getViewByPosition(i, R.id.iv_state);
            select.setVisibility(View.GONE);
            state.setVisibility(View.VISIBLE);
        }
    }

    private void deleteTodo() {
        int index = 0;
        List<Integer> uidList = new ArrayList<>();
        for (int i = 0; i < itemSelectedList.size(); i++) {
            TodoState todoState = itemSelectedList.get(i);
            if (todoState.isSelected()) {
                uidList.add(todoState.getUid());
                index ++;
            }
        }

        if (index == 0) {
            return;
        }
        if (index == itemSelectedList.size()) {
            itemSelectedList.clear();
            viewModel.deleteAll();
        } else {
            for (int i = 0; i < uidList.size(); i++) {
                itemSelectedList.remove(i);
                viewModel.deleteById(uidList.get(i));
            }
        }

        binding.tvDelete.setText("删除");
        cancelDelete();
    }

    @Override
    public boolean onLongPressed() {
        return super.onLongPressed();
    }
}