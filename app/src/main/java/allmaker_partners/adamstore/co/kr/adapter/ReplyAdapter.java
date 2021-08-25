package allmaker_partners.adamstore.co.kr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import allmaker_partners.adamstore.co.kr.R;
import allmaker_partners.adamstore.co.kr.activity.ActConstructInfo;
import allmaker_partners.adamstore.co.kr.activity.ActConstructInfoDetail;
import allmaker_partners.adamstore.co.kr.data.ReplyData;
import allmaker_partners.adamstore.co.kr.util.BaseViewHolder;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>{
    private static final int VIEW_REPLY = 1;
    private static final int VIEW_REPLY_REPLY = 2;

    private Context mContext;
    private ArrayList<ReplyData> replyList;

    public ReplyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItems(ReplyData item) {
        if (replyList == null) {
            replyList = new ArrayList<>();
        }
        replyList.add(item);
        notifyDataSetChanged();
    }

    public void addItems2(ArrayList<ReplyData> array_data) {
        this.replyList = array_data;
        notifyDataSetChanged();
    }

    public void setItem(ReplyData item) {
        replyList.set(replyList.size() - 1, item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ReplyHolder extends ViewHolder{
        TextView tvDate, tvNick, tvSub, tvReply;

        public ReplyHolder(View itemView){
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvSub = (TextView) itemView.findViewById(R.id.tv_sub);
            tvReply = (TextView) itemView.findViewById(R.id.tv_reply_button);
        }
    }

    public class ReplyReHolder extends ViewHolder{
        TextView tvDate, tvNick, tvSub;

        public ReplyReHolder(View itemView){
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvNick = (TextView) itemView.findViewById(R.id.tv_nick);
            tvSub = (TextView) itemView.findViewById(R.id.tv_sub);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ReplyData currentItem = replyList.get(position);
        String type = currentItem.getReplyChk();
        if(type.equalsIgnoreCase("y")){
            return VIEW_REPLY_REPLY;
        }else{
            return VIEW_REPLY;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (viewType) {
            case VIEW_REPLY:
                view = inflater.inflate(R.layout.recycler_reply, parent, false);
                return new ReplyHolder(view);
            case VIEW_REPLY_REPLY:
                view = inflater.inflate(R.layout.recycler_reply_reply, parent, false);
                return new ReplyReHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        final ReplyData item = replyList.get(position);
        if(viewType == VIEW_REPLY){
            ReplyHolder replyHolder = (ReplyHolder) holder;
            replyHolder.tvSub.setText(replyList.get(position).getSub());
            replyHolder.tvNick.setText(replyList.get(position).getNick());
            replyHolder.tvDate.setText(replyList.get(position).getDate());
            replyHolder.tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActConstructInfoDetail)mContext).setReply(replyList.get(position).getNick(),replyList.get(position).getUserIdx(),replyList.get(position).getIdx());
                }
            });
        }else{
            ReplyReHolder replyReHolder = (ReplyReHolder) holder;
            replyReHolder.tvSub.setText(replyList.get(position).getSub());
            replyReHolder.tvNick.setText(replyList.get(position).getNick());
            replyReHolder.tvDate.setText(replyList.get(position).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return replyList == null ? 0 : replyList.size();
    }
}
