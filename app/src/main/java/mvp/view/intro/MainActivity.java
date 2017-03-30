package mvp.view.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import manulorenzo.me.kittentrate.R;
import mvp.view.kittentrate.GameActivity;

/**
 * Created by Manuel Lorenzo
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.new_game_button)
    Button newGameButton;
    @BindView(R.id.scores_button)
    Button topScoresButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.scores_button)
    public void scoresButtonClick() {
    }

    @OnClick(R.id.new_game_button)
    public void newGameButtonClick() {
        startActivity(GameActivity.newIntent(this));
        finish();
    }
}
