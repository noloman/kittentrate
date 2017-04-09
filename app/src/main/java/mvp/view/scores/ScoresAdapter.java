package mvp.view.scores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manulorenzo.me.kittentrate.R;
import mvp.model.repository.model.PlayerScore;

/**
 * Created by Manuel Lorenzo
 */

class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoresViewHolder> {
    private List<PlayerScore> scoreList;

    ScoresAdapter() {
    }

    public void setData(List<PlayerScore> playerScores) {
        scoreList = playerScores;
        notifyDataSetChanged();
    }

    @Override
    public ScoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.score_item_layout, parent, false);
        return new ScoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoresViewHolder holder, int position) {
        holder.bind(scoreList.get(position));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    static class ScoresViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.score_textview)
        TextView scoreTextView;
        @BindView(R.id.name_textview)
        TextView nameTextView;

        ScoresViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(PlayerScore playerScore) {
            nameTextView.setText(playerScore.getPlayerName());
            scoreTextView.setText(playerScore.getPlayerScore());
        }
    }
}
