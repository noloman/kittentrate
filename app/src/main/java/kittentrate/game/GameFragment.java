package kittentrate.game;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittentrate.MainActivity;
import kittentrate.data.di.Injection;
import kittentrate.data.repository.model.PhotoEntity;
import kittentrate.data.repository.GameRepository;
import kittentrate.score.PlayerScore;
import kittentrate.navigation.Screen;
import kittentrate.utils.Constants;
import kittentrate.view.custom.AutofitRecyclerView;
import manulorenzo.me.kittentrate.R;

public class GameFragment extends Fragment implements GameContract.View, GameAdapter.OnItemClickListener, NameScoreDialogFragment.NameScoreKeyListener {
    public static final String SCORE_BUNDLE_KEY = "score";
    @BindView(R.id.floating_textview)
    TextView floatingTextView;
    @BindView(R.id.recycler_view)
    AutofitRecyclerView autofitRecyclerView;
    private GameAdapter gameAdapter;
    private Map<ViewFlipper, Card> viewFlipperCardWeakHashMap = new WeakHashMap<>(Constants.NUMBER_MATCHING_CARDS);
    private ProgressDialog loadingDialog;
    private GamePresenter gamePresenter;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameFragment.
     */
    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_BUNDLE_KEY, gamePresenter.getScore());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        GameRepository gameRepository = Injection.provideRepository(getContext().getApplicationContext());

        gamePresenter = new GamePresenter(gameRepository, this);
        gameAdapter = new GameAdapter(this, getContext().getApplicationContext());

        gamePresenter.start();
    }

    @Override
    public void onDestroyView() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        autofitRecyclerView.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.game_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (autofitRecyclerView.getAdapter() == null) {
            autofitRecyclerView.setAdapter(gameAdapter);
        }
        if (savedInstanceState != null && savedInstanceState.getInt(SCORE_BUNDLE_KEY) != 0) {
            int score = savedInstanceState.getInt(SCORE_BUNDLE_KEY);
            floatingTextView.setText(Integer.toString(score));
        }
    }

    @Override
    public void onItemClick(final int position, Card card, final ViewFlipper viewFlipper) {
        if (shouldDispatchTouchEvent() && !viewFlipperCardWeakHashMap.containsKey(viewFlipper)) {
            viewFlipper.showNext();
            viewFlipperCardWeakHashMap.put(viewFlipper, card);
            gamePresenter.onItemClicked(position, card, viewFlipper);
        }
    }

    @Override
    public void onDetach() {
        if (loadingDialog != null) {
            loadingDialog = null;
        }
        super.onDetach();
    }

    @Override
    public void showLoadingView() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getContext());
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setTitle(getString(R.string.progress_dialog_loading_images_title));
        loadingDialog.setMessage(getString(R.string.progress_dialog_loading_images_message));
        loadingDialog.show();
    }

    @Override
    public void hideLoadingView() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void turnCardsOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gamePresenter.removeCardsFromMaps();
            }
        }, Constants.ROTATION_TIME);
    }

    @Override
    public void notifyAdapterItemChanged(int pos) {
        gameAdapter.notifyItemChanged(pos);
    }

    @Override
    public void notifyAdapterItemRemoved(String id) {
        gameAdapter.removeItem(id);
    }

    @Override
    public void setAdapterData(List<PhotoEntity> photoEntityList) {
        gameAdapter.setDataCardImages(photoEntityList);
        hideLoadingView();
    }

    @Override
    public void removeViewFlipper() {
        for (ViewFlipper viewFlipper : viewFlipperCardWeakHashMap.keySet()) {
            viewFlipper.showPrevious();
        }
        viewFlipperCardWeakHashMap.clear();
    }

    @Override
    public void showErrorView() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("There was an unexpected error").setMessage("Please try again").show();
    }

    @Override
    public void checkGameFinished() {
        if (gameAdapter.getItemCount() == 0) {
            showScoreFragmentDialog(gamePresenter.getScore());
        }
    }

    @Override
    public boolean shouldDispatchTouchEvent() {
        return gamePresenter.shouldDispatchTouchEvent();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onScoreChanged(int gameScore) {
        floatingTextView.setText(Integer.toString(gameScore));
    }

    @Override
    public void onEnterKeyPressed(PlayerScore playerScore) {
        gamePresenter.onScoredEntered(playerScore);
        ((MainActivity) getActivity()).navigateTo(Screen.SCORES);
    }

    private void showScoreFragmentDialog(int score) {
        NameScoreDialogFragment nameScoreDialogFragment = NameScoreDialogFragment.newInstance(score);
        nameScoreDialogFragment.setTargetFragment(this, 0);
        nameScoreDialogFragment.show(getActivity().getSupportFragmentManager(), "fragment_score");
    }
}
