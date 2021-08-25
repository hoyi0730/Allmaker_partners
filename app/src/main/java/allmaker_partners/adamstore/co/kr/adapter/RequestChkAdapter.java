package allmaker_partners.adamstore.co.kr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActRequestAnswer;
import allmaker_partners.adamstore.co.kr.data.RequestChkData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerRequestChkBinding;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerRequestChkBinding;
import allmaker_partners.adamstore.co.kr.util.HoUtils;

public class RequestChkAdapter extends RecyclerView.Adapter {
    private ArrayList<RequestChkData> requestChkList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerRequestChkBinding binding;
        public MainHoder(RecyclerRequestChkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public RequestChkAdapter(ArrayList<RequestChkData> historyData, Context con) {
        this.ctx = con;
        this.requestChkList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerRequestChkBinding mainBinding = RecyclerRequestChkBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new RequestChkAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RequestChkAdapter.MainHoder mainHoder = (RequestChkAdapter.MainHoder) holder;
        final RecyclerRequestChkBinding binding = mainHoder.binding;
        binding.tvDate.setText(requestChkList.get(position).getDate().substring(0,11));
        binding.tvNick.setText(requestChkList.get(position).getName());
        binding.tvSub.setText(requestChkList.get(position).getRequest());
        if(HoUtils.isNull(requestChkList.get(position).getAnswer())){
            binding.tvReply.setTextColor(ctx.getResources().getColor(R.color.white));
            binding.tvReply.setBackgroundResource(R.drawable.btn_answer_iq_bg_120201);
            binding.tvReply.setText("답변달기");
        }else{
            binding.tvReply.setTextColor(ctx.getResources().getColor(R.color.color_999999));
            binding.tvReply.setBackgroundResource(R.drawable.btn_modify_iq_bg_120201);
            binding.tvReply.setText("수정하기");
        }

        binding.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActRequestAnswer.class);
                intent.putExtra("status",requestChkList.get(position).getStatus());
                intent.putExtra("request",requestChkList.get(position).getRequest());
                intent.putExtra("name",requestChkList.get(position).getName());
                intent.putExtra("date",requestChkList.get(position).getDate());
                intent.putExtra("idx",requestChkList.get(position).getIdx());
                intent.putExtra("answer",requestChkList.get(position).getAnswer());
                intent.putExtra("yIdx",requestChkList.get(position).getCYidx());
                ctx.startActivity(intent);
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return requestChkList.size();
    }


    //여기 부터
    public RequestChkData getItem(int pos) {
        return requestChkList.get(pos);
    }

    public void addItem(ArrayList<RequestChkData> carBrands){
        requestChkList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<RequestChkData> array_data) {
        this.requestChkList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        requestChkList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        requestChkList.remove(pos);
    }

}