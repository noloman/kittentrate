package kittentrate.ui.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kittentrate.data.viewmodel.ScoresViewModel;
import kittentrate.ui.MainActivity;
import kittentrate.ui.navigation.Screen;
import manulorenzo.me.kittentrate.R;

public class ScoresFragment extends Fragment implements ScoresContract.View, View.OnClickListener {
    @BindView(R.id.scores_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.scores_layout)
    LinearLayout scoresLayout;
    @BindView(R.id.empty_textview)
    TextView emptyTextView;
    @BindView(R.id.scores_fab)
    FloatingActionButton scoresFab;
    private ScoresAdapter scoresAdapter;
    private ScoresViewModel scoresViewModel;

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
        setHasOptionsMenu(false);
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
        scoresFab.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ScoresViewModelFactory viewModelFactory = new ScoresViewModelFactory(GameApplication.database.playerScoreDao());
//        scoresViewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoresViewModel.class);
//        scoresViewModel.getTopScores();
    }

    @Override
    public void showScores(List<PlayerScore> data) {
        emptyTextView.setVisibility(View.GONE);
        scoresLayout.setVisibility(View.VISIBLE);
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(scoresAdapter);
        }
        scoresAdapter.setData(data);
    }

    @Override
    public void showEmptyView() {
        scoresLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).navigateTo(Screen.GAME);
    }
}
