package kittentrate.score;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittentrate.data.di.Injection;
import kittentrate.data.repository.model.PhotoEntity;
import kittentrate.data.repository.GameRepository;
import kittentrate.data.rest.NetworkCallback;
import manulorenzo.me.kittentrate.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScoresFragment extends Fragment implements ScoresContract.View, NetworkCallback {
    @BindView(R.id.scores_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.empty_textview)
    TextView emptyTextView;
    private ScoresAdapter scoresAdapter;

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
        View view = inflater.inflate(R.layout.scores_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (recyclerView.getAdapter() == null) {
            scoresAdapter = new ScoresAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context applicationContext = getContext().getApplicationContext();

        GameRepository gameRepository = Injection.provideRepository(applicationContext);

        ScoresLoader scoresLoader = new ScoresLoader(applicationContext, gameRepository);
        ScoresPresenter scoresPresenter = new ScoresPresenter(this, scoresLoader, getLoaderManager());
        scoresPresenter.start();
    }

    @Override
    public void showScores(List<PlayerScore> data) {
        emptyTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(scoresAdapter);
        }
        scoresAdapter.setData(data);
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
