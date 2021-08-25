package allmaker_partners.adamstore.co.kr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import allmaker_partners.adamstore.co.kr.activity.ActConstructModify;
import allmaker_partners.adamstore.co.kr.activity.ActConstructMyList;
import allmaker_partners.adamstore.co.kr.data.ConstructData;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerConstructMyListBinding;
import allmaker_partners.adamstore.co.kr.databinding.RecyclerConstructMyListBinding;
import allmaker_partners.adamstore.co.kr.util.AppUserData;
import allmaker_partners.adamstore.co.kr.util.CHttpUrlConnection;
import allmaker_partners.adamstore.co.kr.util.HoUtils;
import allmaker_partners.adamstore.co.kr.util.JsonUrl;

public class ConstructMylistAdapter extends RecyclerView.Adapter {
    private ArrayList<ConstructData> constructList;
    private Context ctx;

    public class MainHoder extends RecyclerView.ViewHolder {
        RecyclerConstructMyListBinding binding;
        public MainHoder(RecyclerConstructMyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public ConstructMylistAdapter(ArrayList<ConstructData> historyData, Context con) {
        this.ctx = con;
        this.constructList = historyData;
        //Log.d(CommonUtil.TAG,"확인 : "+storeItemData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        RecyclerConstructMyListBinding mainBinding = RecyclerConstructMyListBinding.inflate(LayoutInflater.from(ctx), parent, false);
        vhItem = new ConstructMylistAdapter.MainHoder(mainBinding);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ConstructMylistAdapter.MainHoder mainHoder = (ConstructMylistAdapter.MainHoder) holder;
        final RecyclerConstructMyListBinding binding = mainHoder.binding;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (((Activity) ctx).isFinishing()) {
            return;
        }
        Glide.with(ctx)
                .load(constructList.get(position).getImg())
                .apply(requestOptions)
                .centerCrop()
                .error(R.drawable.simbol_logoin)
                .into(binding.ivImg);

        binding.tvSub.setText(constructList.get(position).getSub());
        binding.tvTitle.setText(constructList.get(position).getTitle());
        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ctx).setTitle("로그아웃")
                        .setMessage("해당 게시글을 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delCommercial(constructList.get(position).getIdx(),position);
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).create().show();
            }
        });

        binding.tvModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActConstructModify.class);
                intent.putExtra("type",constructList.get(position).getType());
                intent.putExtra("idx",constructList.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });

        binding.llTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActConstructInfoDetail.class);
                intent.putExtra("idx",constructList.get(position).getIdx());
                ctx.startActivity(intent);
            }
        });


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
        notifyDataSetChanged();
    }

    public void delCommercial(String idx, int posi) {
        HoUtils.showProgressDialog((ActConstructMyList)ctx);
        final String mUrl = JsonUrl.CONTROL;
        final CHttpUrlConnection conn = new CHttpUrlConnection();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result;
                try {
                    HashMap<String, String> mParam = new HashMap<String, String>();
                    mParam.put("dbControl", "delCommercial");
                    mParam.put("m_idx", AppUserData.getData(ctx,"idx"));
                    mParam.put("cb_idx", idx);
                    result = conn.sendPost(mUrl, mParam);
                    ((ActConstructMyList)ctx).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HoUtils.hideProgressDialog();
                                JSONObject joList = new JSONObject(result);
                                if(HoUtils.getStr(joList,"result").equalsIgnoreCase("Y")){
                                    deleteItem(posi);
                                    ((ActConstructMyList)ctx).setList();
                                    Toast.makeText(ctx, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ctx, HoUtils.getStr(joList,"msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                HoUtils.hideProgressDialog();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    HoUtils.hideProgressDialog();
                }
            }
        }).start();
    }
}