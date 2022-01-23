package com.zjw.mvvm_demo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zjw.mvvm_demo.bean.WallPaperResponse;
import com.zjw.mvvm_demo.databinding.ItemWallPaperBinding;
import com.zjw.mvvm_demo.network.utils.KLog;

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
        binding.setWallPager(verticalList.get(position));
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

        public ViewHolder(@NonNull ItemWallPaperBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ClickBinding {

        public void itemClick(WallPaperResponse.Res.Vertical vertical, View view) {
            Intent intent = new Intent(view.getContext(), PictureViewActivity.class);
            intent.putExtra("img", vertical.getImg());
            view.getContext().startActivity(intent);
        }
    }
}
