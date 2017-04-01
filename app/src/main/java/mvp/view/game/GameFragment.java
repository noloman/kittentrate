package mvp.view.game;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import manulorenzo.me.kittentrate.R;
import mvp.model.di.component.DaggerRepositoryComponent;
import mvp.model.di.module.ApplicationModule;
import mvp.model.di.module.RepositoryModule;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.repository.GameRepository;
import mvp.model.repository.model.PlayerScore;
import mvp.model.utils.Constants;
import mvp.view.custom.AutofitRecyclerView;
import mvp.view.custom.NameScoreDialogFragment;

public class GameFragment extends Fragment implements GameContract.View, GameAdapter.OnItemClickListener, NameScoreDialogFragment.NameScoreKeyListener {
    public static final String SCORE_BUNDLE_KEY = "score";
    private GameAdapter gameAdapter;
    private Map<ViewFlipper, Card> viewFlipperCardWeakHashMap = new WeakHashMap<>(Constants.NUMBER_MATCHING_CARDS);
    private ProgressDialog loadingDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private GamePresenter gamePresenter;
    @Inject
    GameRepository gameRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.floating_textview)
    TextView floatingTextView;
    @BindView(R.id.recycler_view)
    AutofitRecyclerView autofitRecyclerView;

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

        DaggerRepositoryComponent.builder()
                .applicationModule(new ApplicationModule(getActivity().getApplication()))
                .repositoryModule(new RepositoryModule())
                .build()
                .inject(this);

        gamePresenter = new GamePresenter(gameRepository, this);
        gamePresenter.start();

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        gameAdapter = new GameAdapter(this, getContext().getApplicationContext());
    }

    @Override
    public void onDestroyView() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
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
        viewFlipper.showNext();
        viewFlipperCardWeakHashMap.put(viewFlipper, card);
        gamePresenter.onItemClicked(position, card, viewFlipper);
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
    public void onGameFinished() {
        showScoreFragmentDialog(gamePresenter.getScore());
    }

    @Override
    public void onScoreIncreased(int score) {
        floatingTextView.setText(Integer.toString(score));
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
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("There was an unexpected error").setMessage("Please try again").show();
    }

    @Override
    public boolean shouldDispatchTouchEvent() {
        return gamePresenter.shouldDispatchTouchEvent();
    }

    @Override
    public void onEnterKeyPressed(PlayerScore playerScore) {
        gameRepository.addTopScore(playerScore);
        getActivity().finish();
    }

    private void showScoreFragmentDialog(int score) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NameScoreDialogFragment nameScoreDialogFragment = NameScoreDialogFragment.newInstance(score);
        nameScoreDialogFragment.setTargetFragment(this, 0);
        nameScoreDialogFragment.show(fm, "fragment_score");
    }
}
