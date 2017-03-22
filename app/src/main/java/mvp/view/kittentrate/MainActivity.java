package mvp.view.kittentrate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import manulorenzo.me.kittentrate.R;
import mvp.model.entity.Card;
import mvp.model.entity.PhotoEntity;
import mvp.model.utils.Constants;
import mvp.view.custom.AutofitRecyclerView;

public class MainActivity extends AppCompatActivity implements KittenContract.View, KittenAdapter.OnItemClickListener {
    private KittenAdapter adapter;
    private KittenGamePresenter kittenGamePresenter;
    private Map<ViewFlipper, Card> viewFlipperCardWeakHashMap = new WeakHashMap<>(Constants.NUMBER_MATCHING_CARDS);
    private ProgressDialog loadingDialog;
    private AlertDialog.Builder alertDialogBuilder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.floating_textview)
    TextView floatingTextView;
    @BindView(R.id.recycler_view)
    AutofitRecyclerView autofitRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStrictMode();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        kittenGamePresenter = new KittenGamePresenter(this);
        kittenGamePresenter.start();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        adapter = new KittenAdapter(this, this);
        autofitRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (kittenGamePresenter.shouldDispatchTouchEvent()) {
            super.dispatchTouchEvent(ev);
        }
        return false;
    }

    @Override
    public void onItemClick(final int position, Card card, final ViewFlipper viewFlipper) {
        viewFlipper.showNext();
        viewFlipperCardWeakHashMap.put(viewFlipper, card);
        kittenGamePresenter.onItemClicked(position, card, viewFlipper);
    }

    @Override
    public void showLoadingView() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
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
        }
    }

    @Override
    public void turnCardsOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kittenGamePresenter.removeCardsFromMaps();
            }
        }, Constants.ROTATION_TIME);
    }

    @Override
    public void notifyAdapterItemChanged(int pos) {
        adapter.notifyItemChanged(pos);
    }

    @Override
    public void notifyAdapterItemRemoved(String id) {
        adapter.removeItem(id);
    }

    @Override
    public void onGameFinished() {
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Finished!");
        alertDialogBuilder.show();
    }

    @Override
    public void onScoreIncreased(int score) {
        floatingTextView.setText(score + "");
    }

    @Override
    public void setAdapterData(List<PhotoEntity> photoEntityList) {
        adapter.setDataCardImages(photoEntityList);
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
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("There was an unexpected error").setMessage("Please try again").show();
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