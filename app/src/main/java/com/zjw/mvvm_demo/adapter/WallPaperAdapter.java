package com.zjw.mvvm_demo.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zjw.mvvm_demo.ui.activity.PictureViewActivity;
import com.zjw.mvvm_demo.R;
import com.zjw.mvvm_demo.bean.WallPaperResponse;
import com.zjw.mvvm_demo.databinding.ItemWallPaperBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.ViewHolder> {

    private List<WallPaperResponse.Res.Vertical> verticalList;

    public WallPaperAdapter(List<WallPaperResponse.Res.Vertical> verticalList) {
        this.verticalList = verticalList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWallPaperBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_wall_paper, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemWallPaperBinding binding = holder.getBinding();
        binding.setWallPaper(verticalList.get(position));
        binding.setOnClick(new ClickBinding());
        binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return verticalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemWallPaperBinding binding;

        public ItemWallPaperBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemWallPaperBinding binding) {
            this.binding = binding;
        }

        public ViewHolder(@NonNull ItemWallPaperBinding inflate) {
            super(inflate.getRoot());
            this.binding = inflate;
        }
    }

    public static class ClickBinding {
        public void itemClick(WallPaperResponse.Res.Vertical vertical, View view) {
            Log.d("TAG", "itemClick:  执行这步没？？？");
            Intent intent = new Intent(view.getContext(), PictureViewActivity.class);
            intent.putExtra("img", vertical.getImg());
            view.getContext().startActivity(intent);
        }
    }

}
