package com.zjw.mvvm_demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.databinding.ItemNotebookBinding;
import com.zjw.mvvm_demo.db.bean.Notebook;
import org.jetbrains.annotations.NotNull;


public class NotebookAdapter extends BaseQuickAdapter<Notebook, BaseDataBindingHolder<ItemNotebookBinding>> {


    private boolean isBatchDeletion;

    public void setBatchDeletion(boolean batchDeletion) {
        isBatchDeletion = batchDeletion;
    }

    public NotebookAdapter() {
        super(R.layout.item_notebook);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemNotebookBinding> bindingHolder, Notebook notebook) {
        ItemNotebookBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setNotebook(notebook);
            dataBinding.setIsBatchDeletion(isBatchDeletion);
            dataBinding.executePendingBindings();
        }
    }
}
