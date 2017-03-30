package mvp.view.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import manulorenzo.me.kittentrate.R;
import mvp.view.custom.NameScoreDialogFragment;

public class GameActivity extends AppCompatActivity {
    private GameFragment gameFragment;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        gameFragment = (GameFragment) getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        if (gameFragment == null) {
            // Create the fragment
            gameFragment = GameFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_fragment, gameFragment);
            transaction.commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gameFragment != null && gameFragment.isVisible() && gameFragment.shouldDispatchTouchEvent()) {
            super.dispatchTouchEvent(ev);
        }
        return false;
    }

    public void showScoreFragmentDialog(int score) {
        FragmentManager fm = getSupportFragmentManager();
        NameScoreDialogFragment nameScoreDialogFragment = NameScoreDialogFragment.newInstance(score);
        nameScoreDialogFragment.show(fm, "fragment_score");
    }
}