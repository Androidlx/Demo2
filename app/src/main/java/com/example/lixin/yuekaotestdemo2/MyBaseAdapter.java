package com.example.lixin.yuekaotestdemo2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lixin.yuekaotestdemo2.bean.Yuekao2Info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hua on 2017/8/22.
 */

public class MyBaseAdapter extends BaseAdapter {


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)
            .cacheInMemory(true)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .build();


    private List<Yuekao2Info.ResultBean.DataBean> data;
    private Context context;
    MyBaseAdapter(List<Yuekao2Info.ResultBean.DataBean> data, Context context){
        this.data = data;
        this.context =context;
    }
    public void loadMore(List<Yuekao2Info.ResultBean.DataBean> datas,boolean flag){

        for (Yuekao2Info.ResultBean.DataBean bean:datas) {
            if (flag){
                data.add(0,bean);
            }else {
                data.add(bean);
            }
        }

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(context,R.layout.item,null);
             holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(data.get(position).getTitle());
        ImageLoader.getInstance().displayImage(data.get(position).getThumbnail_pic_s(),holder.iv,options);
        return convertView;
    }
    class ViewHolder{
        TextView tv;
        ImageView iv;
    }
}
