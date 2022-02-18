package com.zjw.mvvm_demo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.adapter.NotebookAdapter;
import com.zjw.mvvm_demo.databinding.ActivityNotebookBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.view.RecyclerViewAnimation;
import com.zjw.mvvm_demo.viewmodels.NotebookViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotebookActivity extends BaseActivity {

    private ActivityNotebookBinding binding;
    private NotebookViewModel viewModel;

    private boolean hasNotebook;

    private boolean isDelete;

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_EDIT = 1;
    private int mEditMode = STATE_DEFAULT;
    private boolean editorStatus = false;
    private int index = 0;
    private boolean isAllSelected = false;

    private NotebookAdapter adapter;

    private Animation inAnimation;
    private Animation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notebook);

        viewModel = new ViewModelProvider(this).get(NotebookViewModel.class);
        setStatusBar(true);

        back(binding.toolbar);
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.dialog_from_bottom_anim_in);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.dialog_from_bottom_anim_out);

        initView();

//        binding.fabAddNotebook.setOnClickListener(this);
//        binding.tvEdit.setOnClickListener(this);
//        binding.tvDelete.setOnClickListener(this);
//        binding.tvCheckAll.setOnClickListener(this);
    }

    private void initView() {
        //获取navController
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.notebook_fragment:
                    binding.tvTitle.setText("全部笔记");
                    controller.navigate(R.id.notebook_fragment);
                    break;
                case R.id.todo_fragment:
                    binding.tvTitle.setText("全部待办");
                    controller.navigate(R.id.todo_fragment);
                    break;
            }

            return true;
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        viewModel.getNotebooks();
//        viewModel.notebooks.observe(this, notebooks -> {
//            if (notebooks.size() > 0) {
//                binding.tvEdit.setVisibility(View.VISIBLE);
//                binding.rvNotebook.setLayoutManager(new LinearLayoutManager(context));
//                adapter = new NotebookAdapter(notebooks);
//                binding.rvNotebook.setAdapter(adapter);
//                RecyclerViewAnimation.runLayoutAnimation(binding.rvNotebook);
//                hasNotebook = true;
//            } else {
//                binding.tvEdit.setVisibility(View.GONE);
//                hasNotebook = false;
//            }
//            binding.setHasNotebook(hasNotebook);
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fab_add_notebook:
//                jumpActivity(NoteEditActivity.class);
//                break;
//            case R.id.tv_edit:
//                updateEditState();
//                break;
//            case R.id.tv_delete:
//                deleteNotebook();
//                break;
//            case R.id.tv_check_all:
//                if (!isAllSelected) {
//                    setAllItemChecked();
//                    isAllSelected = true;
//                } else {
//                    setAllItemUnchecked();
//                    isAllSelected = false;
//                }
//                break;
//        }
//    }
//
//    private void deleteNotebook() {
//
//    }
//
//    private void updateEditState() {
//        mEditMode = mEditMode == STATE_DEFAULT ? STATE_EDIT : STATE_DEFAULT;
//        if (mEditMode == STATE_EDIT) {
//            binding.tvEdit.setText("取消");
//            binding.fabAddNotebook.setVisibility(View.GONE);
//            binding.layBottom.setVisibility(View.VISIBLE);
//            binding.layBottom.setAnimation(inAnimation);
//            editorStatus = true;
//        } else {
//            binding.tvEdit.setText("编辑");
//            binding.fabAddNotebook.setVisibility(View.VISIBLE);
//            binding.layBottom.setVisibility(View.GONE);
//            binding.layBottom.setAnimation(outAnimation);
//            editorStatus = false;
//
//            setAllItemUnchecked();
//        }
//
//        adapter.setEditMode(mEditMode);
//
//        adapter.addChildClickViewIds(R.id.iv_select);
//        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
//                Log.d("TAG", "进来没: " + editorStatus);
//                if (editorStatus) {
//                    Notebook notebook = viewModel.notebooks.getValue().get(position);
//                    Log.d("TAG", "onItemChildClick: " + notebook.getTitle());
//                    boolean isSelect = notebook.isChecked();
//                    if (!isSelect) {
//                        index ++;
//                        notebook.setChecked(true);
//                    } else {
//                        notebook.setChecked(false);
//                        index --;
//                    }
//
//                    if (index == 0) {
//                        binding.tvDelete.setText("删除");
//                    } else {
//                        binding.tvDelete.setText("删除(" + index + ")");
//                    }
//
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//    }
//
//    //全部选中
//    private void setAllItemChecked() {
//
//        if (adapter == null) return;
//        List<Notebook> notebooks = viewModel.notebooks.getValue();
//        if (notebooks != null && notebooks.size() > 0) {
//            for (int i = 0; i < notebooks.size(); i++) {
//                notebooks.get(i).setChecked(true);
//            }
//            adapter.notifyDataSetChanged();
//            index = notebooks.size();
//        }
//
//        binding.tvDelete.setText("删除(" + index + ")");
//        binding.tvCheckAll.setText("取消全选");
//    }
//
//
//    //取消全部选中
//    private void setAllItemUnchecked() {
//        if (adapter == null) return;
//        List<Notebook> notebooks = viewModel.notebooks.getValue();
//        if (notebooks != null && notebooks.size() > 0) {
//            for (int i = 0; i < notebooks.size(); i++) {
//                notebooks.get(i).setChecked(false);
//            }
//            adapter.notifyDataSetChanged();
//            index = 0;
//        }
//
//        binding.tvDelete.setText("删除");
//        binding.tvCheckAll.setText("全选");
//    }

}