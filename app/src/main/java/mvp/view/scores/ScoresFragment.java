package mvp.view.scores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import manulorenzo.me.kittentrate.R;
import mvp.model.entity.PhotoEntity;
import mvp.model.mapping.PhotoEntityMapper;
import mvp.model.mapping.PhotoEntityMapperInterface;
import mvp.model.repository.GameRepository;
import mvp.model.repository.ScoresLoader;
import mvp.model.repository.local.GameLocalDataSource;
import mvp.model.repository.remote.GameRemoteDataSource;
import mvp.model.rest.NetworkCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScoresFragment extends Fragment implements ScoresContract.View, NetworkCallback {
    @BindView(R.id.scores_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty_textview)
    TextView emptyTextView;

    public ScoresFragment() {
    }

    public static ScoresFragment newInstance() {
        Bundle args = new Bundle();
        ScoresFragment fragment = new ScoresFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameLocalDataSource gameLocalDataSource = new GameLocalDataSource();
        PhotoEntityMapperInterface serviceMapper = new PhotoEntityMapper();
        GameRemoteDataSource gameRemoteDataSource = new GameRemoteDataSource(this, serviceMapper);
        GameRepository gameRepository = new GameRepository(gameLocalDataSource, gameRemoteDataSource);
        ScoresLoader scoresLoader = new ScoresLoader(getContext().getApplicationContext(), gameRepository);
        ScoresPresenter scoresPresenter = new ScoresPresenter(this, scoresLoader, getLoaderManager());
        scoresPresenter.start();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showScores() {
        emptyTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView() {
        emptyTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(List<PhotoEntity> success) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void onFailure(Throwable t) {
        throw new UnsupportedOperationException("Unsupported");
    }
}
