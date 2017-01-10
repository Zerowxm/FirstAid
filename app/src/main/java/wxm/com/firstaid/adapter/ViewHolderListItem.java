package wxm.com.firstaid.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxm.com.firstaid.R;

/**
 * Created by Zero on 12/8/2016.
 */

public class ViewHolderListItem extends RecyclerView.ViewHolder{
    @BindView(R.id.swipe_item)
    SwipeLayout swipeLayout;
    @BindView(R.id.ill_name)
    TextView illName;
    @BindView(R.id.delete)
    TextView delete;
    boolean isOpen;
    String id;

    public ViewHolderListItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        isOpen=false;
    }
}
