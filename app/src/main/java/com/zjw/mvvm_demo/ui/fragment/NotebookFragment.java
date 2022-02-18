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
import com.zjw.mvvm_demo.databinding.FragmentNotebookBinding;
import com.zjw.mvvm_demo.databinding.NewsFragmentBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;
import com.zjw.mvvm_demo.view.RecyclerViewAnimation;
import com.zjw.mvvm_demo.viewmodels.NewsViewModel;
import com.zjw.mvvm_demo.viewmodels.NotebookViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotebookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotebookFragment extends BaseFragment {

    private NotebookViewModel viewModel;
    private FragmentNotebookBinding binding;

    private boolean hasNotebook;

    private boolean isDelete;

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_EDIT = 1;
    private int mEditMode = STATE_DEFAULT;
    private boolean editorStatus = false;
    private int index = 0;
    private boolean isAllSelected = false;

    private NotebookAdapter adapter;

    public static NotebookFragment newInstance() {
       return new NotebookFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notebook, container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NotebookViewModel.class);

        viewModel.getNotebooks();
        viewModel.notebooks.observe(context, notebooks -> {
            if (notebooks.size() > 0) {
                //binding.tvEdit.setVisibility(View.VISIBLE);
                binding.rvNotebook.setLayoutManager(new LinearLayoutManager(context));
                adapter = new NotebookAdapter(notebooks);
                binding.rvNotebook.setAdapter(adapter);
                RecyclerViewAnimation.runLayoutAnimation(binding.rvNotebook);
                hasNotebook = true;
            } else {
               // binding.tvEdit.setVisibility(View.GONE);
                hasNotebook = false;
            }
            binding.setHasNotebook(hasNotebook);
        });

        binding.fabAddNotebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(NoteEditActivity.class);
            }
        });
    }
}