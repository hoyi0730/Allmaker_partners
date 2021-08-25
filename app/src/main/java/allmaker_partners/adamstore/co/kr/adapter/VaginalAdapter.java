package allmaker_partners.adamstore.co.kr.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.data.VaginalData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerVaginalBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgVaginal;


public class VaginalAdapter extends RecyclerView.Adapter {
    private ArrayList<VaginalData> vaginalList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerVaginalBinding binding;
        public MainHoder(RecyclerVaginalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public VaginalAdapter(ArrayList<VaginalData> historyData, Context con) {
        this.ctx = con;
        this.vaginalList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerVaginalBinding mainBinding = RecyclerVaginalBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new VaginalAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        VaginalAdapter.MainHoder mainHoder = (VaginalAdapter.MainHoder) holder;
        final RecyclerVaginalBinding binding = mainHoder.binding;
        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DlgVaginal.class);
                intent.putExtra("question",vaginalList.get(position).getBl_question());
                intent.putExtra("answer",vaginalList.get(position).getBl_answer());
                intent.putExtra("reference",vaginalList.get(position).getBl_reference());
                ctx.startActivity(intent);
            }
        });

        binding.tvTitle.setText(vaginalList.get(position).getExample());

    }

    @Override
    public int getItemCount() {
        return vaginalList.size();
    }


    //여기 부터
    public VaginalData getItem(int pos) {
        return vaginalList.get(pos);
    }

    public void addItem(ArrayList<VaginalData> carBrands){
        vaginalList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<VaginalData> array_data) {
        this.vaginalList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        vaginalList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        vaginalList.remove(pos);
    }

}