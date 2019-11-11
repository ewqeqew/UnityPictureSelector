/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.luck.picture.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * 预览图下方缩略图adapter
 */

public class PreviewThumbnailGalleryAdapter extends RecyclerView.Adapter<PreviewThumbnailGalleryAdapter.ViewHolder> {

    private Context context;
    private List<LocalMedia> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    private OnItemClickListener onItemClickListener;
    private int mCurrentShowSelect = -1;

    public PreviewThumbnailGalleryAdapter(Context context, List<LocalMedia> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mList = list;
    }

    public void bindFolderData(List<LocalMedia> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.mList != null) {
            return this.mList.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.preview_gallery_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String path = "";
        LocalMedia photoInfo = mList.get(position);
        if (photoInfo != null) {
            path = photoInfo.getPath();
        }
        if (position == mCurrentShowSelect) {
            holder.bgFrame.setBackgroundResource(R.drawable.shape_select_stroke_frame);
        } else {
            holder.bgFrame.setBackgroundResource(R.color.transparent);
        }

        Glide.with(context)
                .load(path)
                .into(holder.mIvPhoto);
    }

    public void changeSelectItem(RecyclerView recyclerView, int pos) {
        this.mCurrentShowSelect = pos;
        if(pos>=0){
            recyclerView.smoothScrollToPosition(pos);
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvPhoto;
        RelativeLayout bgFrame;

        public ViewHolder(View itemView) {
            super(itemView);
            bgFrame = itemView.findViewById(R.id.select_frame);
            mIvPhoto = itemView.findViewById(R.id.iv_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
