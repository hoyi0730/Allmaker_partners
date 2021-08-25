package allmaker_partners.adamstore.co.kr.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.data.LawData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerLawBinding;
import allmaker_partners.adamstore.co.kr.dialog.DlgLaw;

public class LawAdapter extends RecyclerView.Adapter {
    private ArrayList<LawData> lawList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerLawBinding binding;
        public MainHoder(RecyclerLawBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public LawAdapter(ArrayList<LawData> historyData, Context con) {
        this.ctx = con;
        this.lawList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerLawBinding mainBinding = RecyclerLawBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new LawAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        LawAdapter.MainHoder mainHoder = (LawAdapter.MainHoder) holder;
        final RecyclerLawBinding binding = mainHoder.binding;
        binding.tvTitle.setText(lawList.get(position).getTitle());
        binding.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DlgLaw.class);
                intent.putExtra("idx",lawList.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });

        /*RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (((Activity) ctx).isFinishing()) {
            return;
        }
        Glide.with(ctx)
                .load(lawList.get(position).getBannerImg())
                .circleCrop()
                .apply(requestOptions)
                .error(R.drawable.simbol_logoin)
                .into(binding.ivBanner);*/


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
        return lawList.size();
    }


    //여기 부터
    public LawData getItem(int pos) {
        return lawList.get(pos);
    }

    public void addItem(ArrayList<LawData> carBrands){
        lawList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<LawData> array_data) {
        this.lawList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        lawList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        lawList.remove(pos);
    }

}