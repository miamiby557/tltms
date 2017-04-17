package cn.xyy.tltms.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.OrderException;

/**
 * Created by admin on 2017/3/19.
 */

public class OrderExceptionAdapter extends BaseAdapter {
    private Context context;
    private List<OrderException> orderExceptions;

    public OrderExceptionAdapter(Context context, List<OrderException> orderExceptions) {
        this.context = context;
        this.orderExceptions = orderExceptions;
    }

    @Override
    public int getCount() {
        return orderExceptions.size();
    }

    @Override
    public Object getItem(int position) {
        return orderExceptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.nameTv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderException deliveryTaskListDto = orderExceptions.get(position);
        holder.tvName.setText(deliveryTaskListDto.getName());
        return  convertView;
    }
    private class ViewHolder {

        private TextView tvName;


    }

}
