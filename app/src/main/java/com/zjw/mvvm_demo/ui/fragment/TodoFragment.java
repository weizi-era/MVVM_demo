package com.zjw.mvvm_demo.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.NotebookAdapter;
import com.zjw.mvvm_demo.adapter.TodoAdapter;
import com.zjw.mvvm_demo.databinding.FragmentTodoBinding;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;
import com.zjw.mvvm_demo.ui.activity.TodoEditActivity;
import com.zjw.mvvm_demo.view.RecyclerViewAnimation;
import com.zjw.mvvm_demo.viewmodels.TodoViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends BaseFragment {

    private TodoViewModel viewModel;
    private FragmentTodoBinding binding;

    private TodoAdapter adapter;

    private boolean hasTodo;

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

        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        viewModel.getTodos();
        viewModel.todos.observe(context, todos -> {
            if (todos.size() > 0) {
                //binding.tvEdit.setVisibility(View.VISIBLE);
                binding.rvTodo.setLayoutManager(new LinearLayoutManager(context));
                adapter = new TodoAdapter(todos);
                binding.rvTodo.setAdapter(adapter);
                RecyclerViewAnimation.runLayoutAnimation(binding.rvTodo);
                hasTodo = true;
            } else {
                // binding.tvEdit.setVisibility(View.GONE);
                hasTodo = false;
            }
            binding.setHasTodo(hasTodo);
        });

        binding.fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(TodoEditActivity.class);
            }
        });
    }

}