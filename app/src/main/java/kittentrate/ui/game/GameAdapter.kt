package kittentrate.ui.game

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewFlipper
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kittentrate.data.model.PhotoEntity
import manulorenzo.me.kittentrate.R

/**
 * Copyright 2018 Manuel Lorenzo
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class GameAdapter internal constructor(private val onItemClickListener: OnItemClickListener) :
        RecyclerView.Adapter<GameAdapter.CardViewHolder>() {
    private val cardList: MutableList<Card> = mutableListOf()

    internal fun setDataCardImages(entityList: List<PhotoEntity>) {
        cardList.clear()
        entityList.indices.mapTo(cardList) { Card(entityList[it].id, entityList[it].url) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val parentContext = parent.context
        val inflater = LayoutInflater.from(parentContext)

        // Inflate the custom layout
        val cardView = inflater.inflate(R.layout.card_item, parent, false)

        // Return a new holder instance
        val cardViewHolder = CardViewHolder(cardView)
        cardView.setOnClickListener {
            val pos = cardViewHolder.adapterPosition
            if (pos != NO_POSITION) {
                val card = cardList[pos]
                onItemClickListener.onItemClick(pos, card, cardViewHolder.viewFlipper)
            }
        }
        return cardViewHolder
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: Card, viewFlipper: ViewFlipper)
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.viewFlipper)
        lateinit var viewFlipper: ViewFlipper
        @BindView(R.id.cardBack)
        lateinit var cardBack: ImageView
        @BindView(R.id.cardFront)
        lateinit var cardFront: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: Card) {
            Glide.with(cardFront.context)
                    .load(item.imageCoverUrl)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cardFront)
        }
    }
}
