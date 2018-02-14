package kittentrate.ui.game;

import android.content.Context;
import android.support.v7.util.DiffUtil;
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
import kittentrate.data.model.PhotoEntity;
import manulorenzo.me.kittentrate.R;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Copyright 2018 Manuel Lorenzo
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.CardViewHolder> {
    private final List<Card> cardList;
    private OnItemClickListener onItemClickListener;

    GameAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        cardList = new ArrayList<>();
    }

//    void setDataCardImages(List<PhotoEntity> entityList) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(entityList, cardList));
//        diffResult.dispatchUpdatesTo(this);
//    }

    void setDataCardImages(List<PhotoEntity> entityList) {
        cardList.clear();
        for (int i = 0; i < entityList.size(); i++) {
            Card card = new Card(entityList.get(i).getId(), entityList.get(i).getUrl());
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

    private class MyDiffCallback extends DiffUtil.Callback {
        private final List<PhotoEntity> newPhotoEntityList;
        private final List<Card> oldCardList;

        MyDiffCallback(List<PhotoEntity> newPhotoEntityList, List<Card> oldCardList) {
            this.newPhotoEntityList = newPhotoEntityList;
            this.oldCardList = oldCardList;
        }

        @Override
        public int getOldListSize() {
            return oldCardList.size();
        }

        @Override
        public int getNewListSize() {
            return newPhotoEntityList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            String id = oldCardList.get(oldItemPosition).component1();
            return id.equals(newPhotoEntityList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldCardList.get(oldItemPosition).getId().equals(newPhotoEntityList.get(newItemPosition).getId());
        }
    }
}
