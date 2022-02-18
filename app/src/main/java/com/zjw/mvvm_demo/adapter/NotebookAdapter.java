package com.zjw.mvvm_demo.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemNotebookBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import com.zjw.mvvm_demo.ui.activity.NoteEditActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotebookAdapter extends BaseQuickAdapter<Notebook, BaseDataBindingHolder<ItemNotebookBinding>> {

    private static final int STATE_DEFAULT = 0;//默认状态
    int mEditMode = STATE_DEFAULT;

    private boolean isSelect = false;

    public NotebookAdapter(List<Notebook> notebooks) {
        super(R.layout.item_notebook, notebooks);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemNotebookBinding> bindingHolder, Notebook notebook) {
        ItemNotebookBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setNotebook(notebook);
            //dataBinding.setOnClick(new NotebookAdapter.ClickBinding());
            dataBinding.executePendingBindings();

            if (mEditMode == STATE_DEFAULT) {
                dataBinding.ivSelect.setVisibility(View.GONE);
            } else {
                dataBinding.ivSelect.setVisibility(View.VISIBLE);
            }

            dataBinding.ivSelect.setBackgroundResource(notebook.isChecked() ? R.mipmap.ic_selected : R.mipmap.ic_noselect);

            bindingHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEditMode == STATE_DEFAULT) {
                        Intent intent = new Intent(v.getContext(), NoteEditActivity.class);
                        intent.putExtra("uid", notebook.getUid());
                        v.getContext().startActivity(intent);
                    } else {
                        if (notebook.isChecked()) {
                            dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_noselect);
                            notebook.setChecked(false);
                        } else {
                            dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_selected);
                            notebook.setChecked(true);
                        }
                        Log.d("TAG", "isChecked: " + notebook.isChecked());
                    }
                }
            });
        }
    }

//    public static class ClickBinding {
//
//        public void itemClick(Notebook notebook, View view) {
//
//            Intent intent = new Intent(view.getContext(), NoteEditActivity.class);
//            intent.putExtra("uid", notebook.getUid());
//            view.getContext().startActivity(intent);
//        }
//    }

    /**
     * 设置编辑状态   接收Activity中传递的值，并改变Adapter的状态
     */
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();//刷新
    }

}
