package com.zjw.mvvm_demo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zjw.mvvm_demo.Constants;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.NotebookAdapter;
import com.zjw.mvvm_demo.databinding.FragmentNotebookBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;
import com.zjw.mvvm_demo.utils.MVUtils;
import com.zjw.mvvm_demo.viewmodels.NotebookViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotebookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotebookFragment extends BaseFragment implements View.OnClickListener {

    private NotebookViewModel viewModel;
    private FragmentNotebookBinding binding;

    private boolean hasNotebook;

    //笔记列表
    private final List<Notebook> mList = new ArrayList<>();
    //是否为批量删除
    private boolean isBatchDeletion = false;
    //是否全选
    private boolean isAllSelected = false;

    //菜单Item
    private MenuItem itemViewType;

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

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        requireView().setFocusableInTouchMode(true);
        //获取当前view焦点
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            //判断用户点击了手机自带的返回键
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK) {
                if (isBatchDeletion) {
                    setBatchDeletionMode();
                    return true;
                } else {
                    return false;
                }

            }
            return false;
        });

        viewModel.getNotebooks();
        viewModel.notebooks.observe(context, notebooks -> {
            if (notebooks.size() > 0) {
                mList.clear();
                mList.addAll(notebooks);
                adapter.setList(notebooks);
                adapter.notifyDataSetChanged();
                hasNotebook = true;
            } else {
                hasNotebook = false;
            }
            binding.setHasNotebook(hasNotebook);

            binding.setShowSearchLay(hasNotebook || !binding.etSearch.getText().toString().isEmpty());
        });

    }

    private void initView() {
        binding.toolbar.setTitle("");
        context.setSupportActionBar(binding.toolbar);
        back(binding.toolbar);

        setHasOptionsMenu(true);


        initNotebookList();

        binding.fabAddNotebook.setOnClickListener(this);
        binding.tvCheckAll.setOnClickListener(this);
        binding.tvDelete.setOnClickListener(this);
        binding.tvEdit.setOnClickListener(this);
        binding.ivClear.setOnClickListener(this);

        //输入框监听
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.setIsSearch(true);
                    //搜索笔记
                    viewModel.searchNotebook(s.toString());
                } else {
                    //获取全部笔记
                    binding.setIsSearch(false);
                    viewModel.getNotebooks();
                }
            }
        });
    }

    private void initNotebookList() {
        adapter = new NotebookAdapter();
        binding.rvNotebook.setAdapter(adapter);
        binding.rvNotebook.setLayoutManager(MVUtils.getInt(Constants.NOTEBOOK_VIEW_TYPE) == 1?
                new GridLayoutManager(context, 2) : new LinearLayoutManager(context));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (isBatchDeletion) {
                    mList.get(position).setSelect(!mList.get(position).isSelect());
                    adapter.notifyDataSetChanged();

                    // 修改页面标题
                    changeTitle();
                } else {
                    Intent intent = new Intent(view.getContext(), NoteEditActivity.class);
                    intent.putExtra("uid", mList.get(position).getUid());
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 修改标题
     */
    private void changeTitle() {
        int selectedNum = 0;
        for (Notebook notebook : mList) {
            if (notebook.isSelect()) {
                selectedNum++;
            }
        }
        Log.e("TAG", "changeTitle: " + selectedNum);
        binding.tvTitle.setText("已选择 "+ selectedNum +" 项");
        binding.setIsAllSelected(selectedNum == mList.size());
    }

    /**
     * 设置批量删除模式
     */
    private void setBatchDeletionMode() {
        //进入批量删除模式
        isBatchDeletion = !isBatchDeletion;
        //设置当前页面
        binding.setIsBatchDeletion(isBatchDeletion);
        if (!isBatchDeletion) {
            //取消所有选中
            for (Notebook notebook : mList) {
                notebook.setSelect(false);
            }
        }
        //设置适配器
        adapter.setBatchDeletion(isBatchDeletion);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_all:
                allSelected();
                break;
            case R.id.tv_delete:
                showConfirmDelete();
                break;
            case R.id.fab_add_notebook:
                jumpActivity(NoteEditActivity.class);
                break;
            case R.id.iv_clear:
                binding.etSearch.setText("");
                binding.setIsSearch(false);
                break;
        }
    }

    /**
     * 显示确认删除弹窗
     */
    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage("确定要删除所选的笔记吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    List<Notebook> notebookList = new ArrayList<>();
                    //删除所选中的笔记
                    for (Notebook notebook : mList) {
                        if (notebook.isSelect()) {
                            notebookList.add(notebook);
                        }
                    }
                    Notebook[] notebooks = notebookList.toArray(new Notebook[0]);
                    viewModel.deleteNotebook(notebooks);
                    //设置批量删除模式
                    setBatchDeletionMode();
                    //请求数据
                    viewModel.getNotebooks();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }


    /**
     * 全选/取消全选
     */
    private void allSelected() {
        isAllSelected = !isAllSelected;
        //设置适配器
        for (Notebook notebook : mList) {
            notebook.setSelect(isAllSelected);
        }
        //修改页面标题
        changeTitle();
        //设置当前页面
        binding.setIsAllSelected(isAllSelected);
        //刷新适配器
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.notebook_settings, menu);
        itemViewType = menu.findItem(R.id.item_view_type).setTitle(MVUtils.getInt(Constants.NOTEBOOK_VIEW_TYPE) == 1 ? "列表视图" : "宫格视图");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        // 0 是列表视图 1 是宫格视图
        int viewType = MVUtils.getInt(Constants.NOTEBOOK_VIEW_TYPE);

        switch (item.getItemId()) {
            case R.id.item_view_type:
                if (viewType == 0) {
                    viewType = 1;
                    itemViewType.setTitle("列表视图");
                    binding.rvNotebook.setLayoutManager(new GridLayoutManager(context, 2));
                } else {
                    viewType = 0;
                    itemViewType.setTitle("宫格视图");
                    binding.rvNotebook.setLayoutManager(new LinearLayoutManager(context));
                }
                MVUtils.put(Constants.NOTEBOOK_VIEW_TYPE, viewType);
                break;
            case R.id.item_batch_deletion:
                setBatchDeletionMode();
                break;

        }
        return true;
    }

}