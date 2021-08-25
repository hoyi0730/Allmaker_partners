package allmaker_partners.adamstore.co.kr.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.data.FilterData;
import allmaker_partners.adamstore.co.kr.data.FilterData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerFilterBinding;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerFilterBinding;

public class DlgFilterAdapter extends RecyclerView.Adapter {
    private ArrayList<FilterData> filterList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerFilterBinding binding;
        public MainHoder(RecyclerFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public DlgFilterAdapter(ArrayList<FilterData> historyData, Context con) {
        this.ctx = con;
        this.filterList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerFilterBinding mainBinding = RecyclerFilterBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new DlgFilterAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        DlgFilterAdapter.MainHoder mainHoder = (DlgFilterAdapter.MainHoder) holder;
        final RecyclerFilterBinding binding = mainHoder.binding;
        binding.tvName.setText(filterList.get(position).getName());
        if(filterList.get(position).getChk().equalsIgnoreCase("1")){
            binding.ivChk.setImageResource(R.drawable.btn_check_on_4);
        }else{
            binding.ivChk.setImageResource(R.drawable.btn_check_off_4);
        }

        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterList.get(position).getChk().equalsIgnoreCase("0")){
                    filterList.get(position).setChk("1");
                    binding.ivChk.setImageResource(R.drawable.btn_check_on_4);
                }else{
                    filterList.get(position).setChk("0");
                    binding.ivChk.setImageResource(R.drawable.btn_check_off_4);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    //여기 부터
    public FilterData getItem(int pos) {
        return filterList.get(pos);
    }

    public void addItem(ArrayList<FilterData> carBrands){
        filterList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<FilterData> array_data) {
        this.filterList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        filterList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        filterList.remove(pos);
    }

}