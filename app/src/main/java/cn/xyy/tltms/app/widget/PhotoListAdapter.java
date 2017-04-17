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

package cn.xyy.tltms.app.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.xyy.tltms.app.R;


/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class PhotoListAdapter extends BaseAdapter {

    private List<MediaBean> mediaBeen;
    private int mScreenWidth = 720;
    private int mRowWidth = mScreenWidth / 3;

    private Context context;

    public PhotoListAdapter(Context context, List<MediaBean> mediaBeen) {
        this.mediaBeen = mediaBeen;
        this.context = context;
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    @Override
    public int getCount() {
        return mediaBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (null == convertView) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gf_adapter_photo_list_item, null);
            vh.mIvThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final MediaBean mediaBean = mediaBeen.get(position);

        Glide.with(context).load(mediaBean.getOriginalPath())
                .placeholder(R.drawable.ic_gf_default_photo)
                .crossFade()
                .animate(R.anim.gf_flip_horizontal_in)
                .into(vh.mIvThumb);

        return convertView;
    }


    private class ViewHolder {
        private ImageView mIvThumb;
    }

}
