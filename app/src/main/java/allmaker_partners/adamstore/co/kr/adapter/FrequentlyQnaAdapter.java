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
import allmaker_partners.adamstore.co.kr.data.QnaData;

public class FrequentlyQnaAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<QnaData> qnaList;
    private LayoutInflater myinf = null;

    public FrequentlyQnaAdapter(Context context, int groupLay, int chlidLay, ArrayList<QnaData> DataList){
        this.qnaList = DataList;
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
        tvTitle.setText(qnaList.get(groupPosition).getTitle());


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

        TextView tvSub = (TextView)convertView.findViewById(R.id.tv_sub);
        tvSub.setText(qnaList.get(groupPosition).getSub());


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
        return qnaList.get(groupPosition).getSub();
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
    public QnaData getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return qnaList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return qnaList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

}
