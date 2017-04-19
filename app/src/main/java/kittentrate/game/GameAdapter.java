package kittentrate.game;

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
import kittentrate.data.repository.model.PhotoEntity;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by Manuel Lorenzo on 13/03/2017.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.CardViewHolder> {
    private final Context context;
    private final List<Card> cardList;
    private OnItemClickListener onItemClickListener;

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
        final CardViewHolder cardViewHolder = new CardViewHolder(cardView);
        cardView.setOnClickListener(v -> {
            int pos = cardViewHolder.getAdapterPosition();
            if (pos != NO_POSITION) {
                Card card = cardList.get(pos);
                onItemClickListener.onItemClick(pos, card, cardViewHolder.viewFlipper);
            }
        });
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Card item, ViewFlipper viewFlipper);
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

        void bind(final Card item) {
            Glide.with(cardFront.getContext())
                    .load(item.getImageCoverUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cardFront);
        }
    }
}
