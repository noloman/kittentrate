package mvp.view.intro;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import manulorenzo.me.kittentrate.R;
import mvp.view.game.GameActivity;
import mvp.view.scores.ScoresActivity;

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
        // setStrictMode();
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.scores_button)
    public void scoresButtonClick() {
        startActivity(ScoresActivity.newIntent(this));
    }

    @OnClick(R.id.new_game_button)
    public void newGameButtonClick() {
        startActivity(GameActivity.newIntent(this));
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
