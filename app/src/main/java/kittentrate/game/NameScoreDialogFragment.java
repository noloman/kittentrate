package kittentrate.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittentrate.score.PlayerScore;
import manulorenzo.me.kittentrate.R;

/**
 * Created by Manuel Lorenzo
 */

public class NameScoreDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {
    @BindView(R.id.name_edittext)
    EditText nameEditText;
    @BindView(R.id.score_textview)
    TextView scoreTextView;
    int score;
    private NameScoreKeyListener nameScoreKeyListener;

    public interface NameScoreKeyListener {
        void onEnterKeyPressed(PlayerScore playerScore);
    }

    public NameScoreDialogFragment() {

    }

    public static NameScoreDialogFragment newInstance(int score) {
        Bundle args = new Bundle();
        args.putInt(GameFragment.SCORE_BUNDLE_KEY, score);
        NameScoreDialogFragment fragment = new NameScoreDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            nameScoreKeyListener = (NameScoreKeyListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString() + " must implement NameScoreKeyListener");
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_ScoreDialog);
        if (getArguments() != null && getArguments().getInt(GameFragment.SCORE_BUNDLE_KEY, 0) != 0) {
            score = getArguments().getInt(GameFragment.SCORE_BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.score_dialog_title);
        View view = inflater.inflate(R.layout.name_score_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_ScoreDialog);
        scoreTextView.setText(String.valueOf(score));

        nameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the Screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the Screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(50);
        // Call super onResume after sizing
        getDialog().setOnKeyListener(this);
        super.onResume();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                String playerName = nameEditText.getText().toString();
                Integer scoreInt = Integer.valueOf(scoreTextView.getText().toString());
                PlayerScore playerScore = new PlayerScore(playerName, scoreInt);
                nameScoreKeyListener.onEnterKeyPressed(playerScore);
                dismiss();
                return true;
        }
        return false;
    }
}
