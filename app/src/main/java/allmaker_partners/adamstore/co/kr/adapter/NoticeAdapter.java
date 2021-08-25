package allmaker_partners.adamstore.co.kr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.data.NoticeData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;


public class NoticeAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<NoticeData> qnaList;
    private LayoutInflater myinf = null;
    private String[] child,group;
    private boolean check=false;

    public NoticeAdapter(Context context, int groupLay, int chlidLay, ArrayList<NoticeData> DataList){
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
        TextView tvDate = (TextView)convertView.findViewById(R.id.tv_date);
        TextView tvCnt = (TextView)convertView.findViewById(R.id.tv_view_cnt);
        ImageView ivArrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
        tvTitle.setText(qnaList.get(groupPosition).getTitle());
        tvDate.setText(qnaList.get(groupPosition).getDate());
        tvCnt.setText(qnaList.get(groupPosition).getView_cnt());

        if(isExpanded==true){
            ivArrow.setImageResource(R.drawable.up_bd_0901);
            setCount(qnaList.get(groupPosition).getIdx());
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
        LinearLayout llFile = (LinearLayout)convertView.findViewById(R.id.ll_file);
        TextView tvFile = (TextView)convertView.findViewById(R.id.tv_file);
        tvSub.setText(qnaList.get(groupPosition).getSub());
        if(HoUtils.isNull(qnaList.get(groupPosition).getFile())){
            llFile.setVisibility(View.GONE);
        }else{
            llFile.setVisibility(View.VISIBLE);
            tvFile.setText(qnaList.get(groupPosition).getFile());
        }

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
    public NoticeData getGroup(int groupPosition) {
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

    public void setCount(String idx) {
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "setNoticeHitsUp");
                    mParam.put("b_idx", idx);
                    conn.sendPost(mUrl, mParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    HoUtils.hideProgressDialog();
                }
            }
        }).start();
    }

}
