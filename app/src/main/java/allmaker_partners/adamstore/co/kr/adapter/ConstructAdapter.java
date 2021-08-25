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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActConstructInfoDetail;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerConstructBinding;


public class ConstructAdapter extends RecyclerView.Adapter {
    private ArrayList<ConstructData> constructList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerConstructBinding binding;
        public MainHoder(RecyclerConstructBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public ConstructAdapter(ArrayList<ConstructData> historyData, Context con) {
        this.ctx = con;
        this.constructList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerConstructBinding mainBinding = RecyclerConstructBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new ConstructAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ConstructAdapter.MainHoder mainHoder = (ConstructAdapter.MainHoder) holder;
        final RecyclerConstructBinding binding = mainHoder.binding;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(new CenterCrop(),new RoundedCorners(16));
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (((Activity) ctx).isFinishing()) {
            return;
        }
        Glide.with(ctx)
                .load(constructList.get(position).getImg())
                .centerCrop()
                .apply(requestOptions)
                .error(R.drawable.simbol_logoin)
                .into(binding.ivBanner);

        binding.tvSub.setText(constructList.get(position).getTitle());
        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActConstructInfoDetail.class);
                intent.putExtra("idx",constructList.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });

        /*binding.tvNick.setText(friendData.get(position).getNick());
        binding.tvId.setText(friendData.get(position).getId());
        binding.tvPhone.setText(friendData.get(position).getPhone());
        binding.ivFavoriteChk.setVisibility(View.GONE);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (((Activity) ctx).isFinishing()) {
            return;
        }
        Glide.with(ctx)
                .load(friendData.get(position).getImg())
                .circleCrop()
                .apply(requestOptions)
                .error(R.drawable.profile_noimg)
                .into(binding.ivProfile);

        binding.ivChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragMain4)fragment).setBookMark(friendData.get(position).getIdx());
            }
        });

        binding.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DlgMyProfileInfo.class);
                intent.putExtra("type","other");
                intent.putExtra("idx",friendData.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return constructList.size();
    }


    //여기 부터
    public ConstructData getItem(int pos) {
        return constructList.get(pos);
    }

    public void addItem(ArrayList<ConstructData> carBrands){
        constructList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<ConstructData> array_data) {
        this.constructList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        constructList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        constructList.remove(pos);
    }

}