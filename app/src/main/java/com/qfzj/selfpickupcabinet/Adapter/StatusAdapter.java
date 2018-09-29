package com.qfzj.selfpickupcabinet.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qfzj.selfpickupcabinet.R;
import com.qfzj.selfpickupcabinet.bean.BoxStatusBean;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder>{
    private Context mContext;
    public List<BoxStatusBean> mStatusList;
    public boolean misClickable ;
    private final String openedColor="#00A600";
    private final String closedColor="#FF2D2D";
    private final String errorColor="#FF2D2D";
    public StatusAdapter(List<BoxStatusBean> statusList,boolean isClickable){
        mStatusList = statusList;
        misClickable = isClickable;
    }

    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.setting_status_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StatusAdapter.ViewHolder holder, int position) {
       final BoxStatusBean boxStatusBean = mStatusList.get(position);
       holder.boxNo.setText(boxStatusBean.boxNo);
        holder.boxNo.setBackgroundColor(
                boxStatusBean.doorStatus == 1 ?
                        Color.parseColor(openedColor) :
                        Color.parseColor(closedColor));
        if (misClickable) {
            holder.boxNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**开门或关门**/
                    holder.boxNo.setBackgroundColor(
                            boxStatusBean.doorStatusChange() == 1 ?
                                    Color.parseColor(openedColor) :
                                    Color.parseColor(closedColor));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mStatusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView boxNo;
        ImageView doorStatus,itemStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            boxNo = itemView.findViewById(R.id.setting_boxNo);
        }
    }
}
