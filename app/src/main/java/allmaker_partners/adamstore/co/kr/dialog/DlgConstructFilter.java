package allmaker_partners.adamstore.co.kr.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.adapter.ConstructAdapter;
import allmaker_partners.adamstore.co.kr.adapter.DlgFilterAdapter;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.data.FilterData;
import allmaker_partners.adamstore.co.kr.databinding.DlgConstructFilterBinding;
import allmaker_partners.adamstore.co.kr.databinding.DlgRequestCompleteBinding;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.ItemDecorationAlbumColumns;

public class DlgConstructFilter extends Activity implements View.OnClickListener {
    DlgConstructFilterBinding binding;
    Activity act;
    ArrayList<FilterData> filterData;
    LinearLayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= DataBindingUtil.setContentView(this, R.layout.dlg_construct_filter);
        setLayout();
    }

    private void setLayout(){
        act=this;
        type = getIntent().getStringExtra("type");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        binding.ivBack.setOnClickListener(this);
        filterData = new ArrayList<>();
        if(type.equalsIgnoreCase("design")||type.equalsIgnoreCase("construction")){
            for (int i = 0;i<4;i++){
                FilterData fData = new FilterData();
                if(i==0){
                    fData.setChk("0");
                    fData.setName("????????????");
                    filterData.add(fData);
                }else if(i==1){
                    fData.setChk("0");
                    fData.setName("????????????/?????????");
                    filterData.add(fData);
                }else if(i==2){
                    fData.setChk("0");
                    fData.setName("??????");
                    filterData.add(fData);
                }else if(i==3){
                    fData.setChk("0");
                    fData.setName("??????/??????");
                    filterData.add(fData);
                }
            }
        }else if(type.equalsIgnoreCase("material")){
            for (int i = 0;i<4;i++){
                FilterData fData = new FilterData();
                if(i==0){
                    fData.setChk("0");
                    fData.setName("?????????");
                    filterData.add(fData);
                }else if(i==1){
                    fData.setChk("0");
                    fData.setName("???????????????");
                    filterData.add(fData);
                }else if(i==2){
                    fData.setChk("0");
                    fData.setName("???????????????");
                    filterData.add(fData);
                }else if(i==3){
                    fData.setChk("0");
                    fData.setName("??????");
                    filterData.add(fData);
                }
            }
        }else if(type.equalsIgnoreCase("broker")){
            for (int i = 0;i<4;i++){
                FilterData fData = new FilterData();
                if(i==0){
                    fData.setChk("0");
                    fData.setName("??????");
                    filterData.add(fData);
                }else if(i==1){
                    fData.setChk("0");
                    fData.setName("??????");
                    filterData.add(fData);
                }else if(i==2){
                    fData.setChk("0");
                    fData.setName("???");
                    filterData.add(fData);
                }else if(i==3){
                    fData.setChk("0");
                    fData.setName("???");
                    filterData.add(fData);
                }
            }
        }


        mAdapter = new DlgFilterAdapter(filterData, act);
        mManager = new LinearLayoutManager(act);
        binding.rcvList.setLayoutManager(mManager);
        mAdapter.setHasStableIds(false);
        binding.rcvList.setAdapter(mAdapter);
        //binding.rcvList.addItemDecoration(new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen.dp10), 2));
        binding.tvSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                String a = "";
                int cnt = 0;
                for (int i = 0;i<filterData.size();i++){
                    if(filterData.get(i).getChk().equalsIgnoreCase("1")){
                        Log.d(HoUtils.TAG,"?????? ????????? : "+filterData.get(i).getName());
                        cnt ++;
                        if(HoUtils.isNull(a)){
                            a = filterData.get(i).getName();
                        }else{
                            a = a+","+filterData.get(i).getName();
                        }
                    }
                }
                Intent intent = new Intent();
                if(cnt==4||cnt==0){
                    intent.putExtra("result","??????");
                }else{
                    intent.putExtra("result",a);
                }
                setResult(RESULT_OK,intent);
                onBackPressed();
                break;
        }
    }
}