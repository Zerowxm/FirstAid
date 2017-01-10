package wxm.com.firstaid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wxm.com.firstaid.MyApplication;
import wxm.com.firstaid.R;
import wxm.com.firstaid.module.Record;
import wxm.com.firstaid.util.VolleyUtil;

/**
 * Created by Zero on 12/8/2016.
 */

public class RecyclerviewAdapter extends RecyclerSwipeAdapter<ViewHolderListItem> {

//    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    private Context mContext;
    private List<Record> mDataset;
    private boolean is_slow;

    public RecyclerviewAdapter(boolean is_slow,List<Record> records){
        is_slow=this.is_slow;
        mDataset=records;

    }
    @Override
    public ViewHolderListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolderListItem holder = new ViewHolderListItem(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolderListItem holder,final int position) {
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.isOpen=false;
        holder.illName.setText(mDataset.get(position).getContent());
        holder.id=mDataset.get(position).getDisease_id();
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {

            @Override
            public void onStartOpen(SwipeLayout layout) {
                holder.isOpen=true;
            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                holder.isOpen=false;
            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        ViewTreeObserver.OnGlobalLayoutListener swipeGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (holder.isOpen) {
                    // Opens the layout without animation
                    holder.swipeLayout.open(false);
                }
            }
        };
        holder.swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(swipeGlobalLayoutListener);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataset.remove(position);
                notifyItemRemoved(position);
                delete(holder.id);
                notifyItemRangeChanged(position, mDataset.size());
            }
        });
//        holder.textViewPos.setText((position + 1) + ".");
//        viewHolder.textViewData.setText(item);
    }

    public void addItem(int position,String name){
        mDataset.add(position,new Record(name));
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_item;
    }

    public void delete(String id){
        String url = VolleyUtil.url_prefix+"deleteDisease?disease_id="+id;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("delete", response.toString());

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("delete", "Error: " + error.getMessage());
                }
            });
            MyApplication.getInstnce().addToRequestQueue(jsonObjReq);
    }
}
