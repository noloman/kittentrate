package mvp.view.kittentrate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import manulorenzo.me.kittentrate.R;
import mvp.model.utils.Constants;

/**
 * Created by manu on 13/03/2017.
 */

class KittenAdapter extends RecyclerView.Adapter<KittenAdapter.CardViewHolder> {
    private List<Card> cardList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    KittenAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.onItemClickListener = onItemClickListener;
        this.context = context.getApplicationContext();
        cardList = new ArrayList<>();
        for (int i = 0; i < 4 * Constants.NUMBER_MATCHING_CARDS; i++) {
            cardList.add(new Card());
        }
    }

    void setDataCardImages(List<PhotoEntity> entityList) {
        for (int i = 0; i < entityList.size(); i++) {
            cardList.get(i).setImageCoverUrl(entityList.get(i).getUrl());
            cardList.get(i).setId(entityList.get(i).getId());
        }
        notifyDataSetChanged();
    }

    void removeItem(String id) {
        for (int position = 0; position < cardList.size(); position++) {
            String cardId = cardList.get(position).getId();
            if (cardId.equals(id)) {
                cardList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cardList.size());
                break;
            }
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, Card item, ViewFlipper viewFlipper);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View cardView = inflater.inflate(R.layout.card_layout, parent, false);

        // Return a new holder instance
        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(position, cardList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewFlipper)
        ViewFlipper viewFlipper;
        @BindView(R.id.cardBack)
        ImageView cardBack;
        @BindView(R.id.cardFront)
        ImageView cardFront;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final int position, final Card item, final OnItemClickListener listener) {
            Glide.with(cardFront.getContext())
                    .load(item.getImageCoverUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cardFront);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, item, viewFlipper);
                }
            });
        }
    }
}
