package allmaker_partners.adamstore.co.kr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.data.RequestData;

public class RequestAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<RequestData> requestList;
    private LayoutInflater myinf = null;

    public RequestAdapter(Context context, int groupLay, int chlidLay, ArrayList<RequestData> DataList){
        this.requestList = DataList;
        this.groupLayout = groupLay;
        this.chlidLayout = chlidLay;
        this.context = context;
        this.myinf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = myinf.inflate(this.groupLayout, parent, false);
        }

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
        ImageView ivArrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
        TextView tvViewCnt = (TextView)convertView.findViewById(R.id.tv_view_cnt);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tv_date);
        TextView tvStatus = (TextView)convertView.findViewById(R.id.tv_status);

        tvTitle.setText(requestList.get(groupPosition).getTitle());
        tvDate.setText(requestList.get(groupPosition).getTDate().substring(0,11));
        tvViewCnt.setText(requestList.get(groupPosition).getViewCnt());
        if(requestList.get(groupPosition).getStatus().equalsIgnoreCase("Y")){
            tvStatus.setBackgroundResource(R.drawable.btn_finish_bg);
            tvStatus.setTextColor(context.getResources().getColor(R.color.white));
            tvStatus.setText("완료");
        }else{
            tvStatus.setBackgroundResource(R.drawable.btn_answer_bg);
            tvStatus.setTextColor(context.getResources().getColor(R.color.color_999999));
            tvStatus.setText("대기");
        }


        if(isExpanded==true){
            ivArrow.setImageResource(R.drawable.up_bd_0901);
        }else{
            ivArrow.setImageResource(R.drawable.down_bd_0901);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = myinf.inflate(this.chlidLayout, parent, false);
        }

        TextView tvReply = (TextView)convertView.findViewById(R.id.tv_reply);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tv_date);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
        tvReply.setText(requestList.get(groupPosition).getSub());
        tvDate.setText(requestList.get(groupPosition).getReplyDate().substring(0,11));
        tvTitle.setText(requestList.get(groupPosition).getTitle());


        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return requestList.get(groupPosition).getSub();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public RequestData getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return requestList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return requestList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

}
