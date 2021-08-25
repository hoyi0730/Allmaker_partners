package allmaker_partners.adamstore.co.kr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.activity.ActFindAddress;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerAddressBinding;

public class AddressAdapter extends RecyclerView.Adapter {
    private ArrayList<String> addressList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerAddressBinding binding;
        public MainHoder(RecyclerAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public AddressAdapter(ArrayList<String> historyData, Context con) {
        this.ctx = con;
        this.addressList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerAddressBinding mainBinding = RecyclerAddressBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new AddressAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        AddressAdapter.MainHoder mainHoder = (AddressAdapter.MainHoder) holder;
        final RecyclerAddressBinding binding = mainHoder.binding;
        binding.tvAddress.setText(addressList.get(position));

        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActFindAddress)ctx).setResult(addressList.get(position));
                /*((ActSearchAddress)ctx).setResult(addressList.get(position).getId(),addressList.get(position).getPointx(),addressList.get(position).getPointy());*/
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
        return addressList.size();
    }


    //여기 부터
    public String getItem(int pos) {
        return addressList.get(pos);
    }

    public void addItem(ArrayList<String> carBrands){
        addressList.addAll(carBrands);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<String> array_data) {
        this.addressList = array_data;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        addressList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int pos){
        addressList.remove(pos);
    }

}