package mvp.view.game;

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
import manulorenzo.me.kittentrate.R;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;

/**
 * Created by Manuel Lorenzo on 13/03/2017.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.CardViewHolder> {
    private final Context context;
    private final List<Card> cardList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Card item, ViewFlipper viewFlipper);
    }

    GameAdapter(OnItemClickListener onItemClickListener, Context context) {
        this.onItemClickListener = onItemClickListener;
        this.context = context.getApplicationContext();
        cardList = new ArrayList<>();
    }

    void setDataCardImages(List<PhotoEntity> entityList) {
        for (int i = 0; i < entityList.size(); i++) {
            Card card = new Card();
            card.setImageCoverUrl(entityList.get(i).getUrl());
            card.setId(entityList.get(i).getId());
            cardList.add(card);
        }
        notifyDataSetChanged();
    }

    void removeItem(String id) {
        for (int position = 0; position < cardList.size(); position++) {
            String cardId = cardList.get(position).getId();
            if (cardId.equals(id)) {
                cardList.remove(position);
                notifyItemRemoved(position);
                //notifyItemRangeChanged(position, cardList.size());
                break;
            }
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        // Inflate the custom layout
        View cardView = inflater.inflate(R.layout.card_item, parent, false);

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
