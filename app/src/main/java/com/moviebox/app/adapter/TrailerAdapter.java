package com.moviebox.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviebox.app.R;
import com.moviebox.app.vo.data.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 01/03/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Video> mVideos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private int mSize;

    public <T> TrailerAdapter(List<T> videos, Context context){
        mVideos = (List<Video>)videos;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case R.layout.item_nodata:
                itemView = mLayoutInflater.inflate(R.layout.item_nodata, parent, false);
                return new ItemNoTrailerViewHolder(itemView);
            case R.layout.item_video:
                itemView = mLayoutInflater.inflate(R.layout.item_video, parent, false);
                return new ItemTrailerViewHolder(itemView);
            default:
                itemView = mLayoutInflater.inflate(R.layout.item_video, parent, false);
                return new ItemTrailerViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case R.layout.item_nodata:
                ItemNoTrailerViewHolder itemNoTrailerViewHolder = (ItemNoTrailerViewHolder) holder;
                itemNoTrailerViewHolder.mNoDataTitle.setText(mContext.getResources().getString(R.string.movie_no_trailer));
                break;
            case R.layout.item_video:
                Video video;
                ItemTrailerViewHolder itemTrailerViewHolder = (ItemTrailerViewHolder) holder;
                if(position < mVideos.size()){
                    video  = mVideos.get(position);
                }else{
                    video = mVideos.get(mVideos.size() - 1);
                }
                String url = mContext.getResources().getString(R.string.youtube_video_thumbnail,video.getKey());
                Picasso.with(mContext)
                        .load(url)
                        .into(itemTrailerViewHolder.mVideoThumbnail);
                itemTrailerViewHolder.mVideoThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mContext.getResources().getString(R.string.youtube_video_url, video.getKey())));
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mVideos.size() == 0){
            mSize = mVideos.size() + 1;
        }else{
            mSize = mVideos.size() + 1;
        }
        return mSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (mSize >= 2) {
            return R.layout.item_video;
        } else {
            return R.layout.item_nodata;
        }
    }

    public class ItemTrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView mVideoThumbnail;
        private View.OnClickListener onClickListener;

        public ItemTrailerViewHolder(View itemView) {
            super(itemView);
            mVideoThumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
        }
    }

    public class ItemNoTrailerViewHolder extends RecyclerView.ViewHolder{
        TextView mNoDataTitle;
        public ItemNoTrailerViewHolder(View itemView) {
            super(itemView);
            mNoDataTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
