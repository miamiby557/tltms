package cn.xyy.tltms.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cn.xyy.tltms.app.R;
import cn.xyy.tltms.app.data.model.DeliveryTaskListDto;
import cn.xyy.tltms.app.utils.DateUtil;


/**
 * Created by admin on 2017/3/18.
 */

public class LoadingOrderAdapter extends BaseAdapter {

    private Context context;
    private List<DeliveryTaskListDto> data;

    public LoadingOrderAdapter(Context context, List<DeliveryTaskListDto> data) {
        this.context = context;
        this.data = data;
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
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.delivery_task_layout, parent, false);
            holder = new ViewHolder();
            holder.tvIdOrderNo = (TextView) convertView.findViewById(R.id.tvIdOrderNo);
            holder.tvTatus = (TextView) convertView.findViewById(R.id.tvTatus);
            holder.tvlPackageQty = (TextView) convertView.findViewById(R.id.tvlPackageQty);
            holder.tvVolume = (TextView) convertView.findViewById(R.id.tvVolume);
            holder.tvWeight = (TextView) convertView.findViewById(R.id.tvWeight);
            holder.btn01 = (Button) convertView.findViewById(R.id.btn01);
            holder.btn02 = (Button) convertView.findViewById(R.id.btn02);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeliveryTaskListDto deliveryTaskListDto = data.get(position);
        holder.tvIdOrderNo.setText(deliveryTaskListDto.getLdOrderNo());
        holder.tvStartTime.setText(DateUtil.getDate(deliveryTaskListDto.getStartTime()));
        holder.tvTatus.setText(deliveryTaskListDto.getStatus().getText());
        holder.tvlPackageQty.setText(deliveryTaskListDto.getPackageQty()+"");
        holder.tvVolume.setText(deliveryTaskListDto.getVolume().toString()+"立方");
        holder.tvWeight.setText(deliveryTaskListDto.getWeight().toString()+"公斤");
        return convertView;
    }


    private class ViewHolder {

        private TextView tvIdOrderNo, tvStartTime, tvTatus, tvlPackageQty, tvVolume, tvWeight;
        private Button btn01, btn02;


    }
}
