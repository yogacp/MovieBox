package com.moviebox.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviebox.app.R;
import com.moviebox.app.vo.data.Review;

import java.util.List;

/**
 * Created by user on 01/03/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Review> mReviews;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private int mSize;

    public <T> ReviewAdapter(List<T> reviews, Context context){
        mReviews = (List<Review>)reviews;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case R.layout.item_nodata:
                itemView = mLayoutInflater.inflate(R.layout.item_nodata, parent, false);
                return new ItemNoTrailerViewHolder(itemView);
            case R.layout.item_review:
                itemView = mLayoutInflater.inflate(R.layout.item_review, parent, false);
                return new ItemReviewViewHolder(itemView);
            default:
                itemView = mLayoutInflater.inflate(R.layout.item_review, parent, false);
                return new ItemReviewViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case R.layout.item_nodata:
                ItemNoTrailerViewHolder itemNoTrailerViewHolder = (ItemNoTrailerViewHolder) holder;
                itemNoTrailerViewHolder.mNoDataTitle.setText(mContext.getResources().getString(R.string.movie_no_review));
                break;
            case R.layout.item_review:
                Review review;
                ItemReviewViewHolder itemReviewViewHolder = (ItemReviewViewHolder) holder;
                if(position < mReviews.size()){
                    review  = mReviews.get(position);
                }else{
                    review = mReviews.get(mReviews.size() - 1);
                }
                itemReviewViewHolder.mReviewTitle.setText(review.getAuthor());
                itemReviewViewHolder.mReviewContent.setText(review.getContent());
                break;
        }

    }

    @Override
    public int getItemCount() {
        if(mReviews.size() == 0){
            mSize = mReviews.size() + 1;
        }else{
            mSize = mReviews.size() + 1;
        }
        return mSize;
    }


    @Override
    public int getItemViewType(int position){
        if (mSize >= 2) {
            return R.layout.item_review;
        } else {
            return R.layout.item_nodata;
        }
    }

    public class ItemReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewTitle;
        private TextView mReviewContent;

        public ItemReviewViewHolder(View itemView) {
            super(itemView);
            mReviewTitle = (TextView) itemView.findViewById(R.id.review_title);
            mReviewContent = (TextView) itemView.findViewById(R.id.review_content);
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
