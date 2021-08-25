package allmaker_partners.adamstore.co.kr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActConstructInfoDetail;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerAddressBinding;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerBannerBinding;

public class ViewPagerAdapter extends RecyclerView.Adapter {
    ArrayList<ConstructData> commercialList;
    Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerBannerBinding binding;

        public MainHoder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public ViewPagerAdapter(ArrayList<ConstructData> historyData, Context con) {
        this.ctx = con;
        this.commercialList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }
    

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_banner, parent, false);
        return new MainHoder(view);

        /*RecyclerBannerBinding mainBinding = RecyclerBannerBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new ViewPagerAdapter.MainHoder(mainBinding);*/
        
        //return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewPagerAdapter.MainHoder mainHoder = (ViewPagerAdapter.MainHoder) holder;
        final RecyclerBannerBinding binding = mainHoder.binding;
        binding.tvTitle.setText(commercialList.get(position).getTitle());
        binding.tvSub.setText(commercialList.get(position).getSub());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (((Activity) ctx).isFinishing()) {
            return;
        }
        Glide.with(ctx)
                .load(commercialList.get(position).getImg())
                .apply(requestOptions)
                .error(R.drawable.simbol_logoin)
                .into(binding.ivBanner);
        //     .error(R.drawable.simbol_logoin)
        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActConstructInfoDetail.class);
                intent.putExtra("idx",commercialList.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return commercialList.size();
    }
    

    public void setItems(ArrayList<ConstructData> array_data) {
        this.commercialList = array_data;
        notifyDataSetChanged();
    }

    public void deleteItem(int pos) {
        commercialList.remove(pos);
        notifyDataSetChanged();
    }

}