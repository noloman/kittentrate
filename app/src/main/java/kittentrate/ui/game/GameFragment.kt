package kittentrate.ui.game

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.rxkotlin.subscribeBy
import kittentrate.data.model.PhotoEntity
import kittentrate.data.viewmodel.GameViewModel
import kittentrate.data.viewmodel.NetworkViewState
import kittentrate.data.viewmodel.factory.RemoteViewModelFactory
import kittentrate.ui.MainActivity
import kittentrate.ui.navigation.Screen
import kittentrate.ui.score.PlayerScore
import kittentrate.ui.view.custom.AutofitRecyclerView
import kittentrate.utils.Constants
import manulorenzo.me.kittentrate.R
import java.util.*
import kotlin.properties.Delegates

class GameFragment : Fragment(),
        GameContract.View,
        GameAdapter.OnItemClickListener,
        NameScoreDialogFragment.NameScoreKeyListener {
    @BindView(R.id.floating_textview)
    internal lateinit var floatingTextView: TextView
    @BindView(R.id.recycler_view)
    internal lateinit var autofitRecyclerView: AutofitRecyclerView
    private lateinit var gameAdapter: GameAdapter
    private val viewFlipperCardWeakHashMap = WeakHashMap<ViewFlipper, Card>(Constants.NUMBER_MATCHING_CARDS)
    private lateinit var loadingDialog: ProgressDialog
    private lateinit var gameViewModel: GameViewModel

    override var networkViewState: NetworkViewState by Delegates.observable<NetworkViewState>(
            NetworkViewState.None(),
            { _, _, newValue ->
                when (newValue) {
                    is NetworkViewState.Loading -> showLoadingView()
                    is NetworkViewState.Success<*> -> {
                        hideLoadingView()
                        setAdapterData(newValue.item as List<PhotoEntity>)
                    }
                    is NetworkViewState.Error -> showErrorView()
                }
            })

    fun GameFragment() {
        // Required empty constructor
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // TODO NO! Blocking first
        outState.putInt(SCORE_BUNDLE_KEY, gameViewModel.gameScore.blockingFirst())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        val remoteViewModelFactory = RemoteViewModelFactory()
        gameViewModel = ViewModelProviders.of(this, remoteViewModelFactory).get(GameViewModel::class.java)
        gameViewModel.networkViewStateMutableLiveData.observe(this,
                android.arch.lifecycle.Observer { networkViewState: NetworkViewState? ->
                    networkViewState?.let { it -> this@GameFragment.networkViewState = it }
                }
        )
    }

    override fun onPause() {
        super.onPause()
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
        autofitRecyclerView.adapter = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.game_fragment, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autofitRecyclerView.adapter = gameAdapter
        if (savedInstanceState != null && savedInstanceState.getInt(SCORE_BUNDLE_KEY) != 0) {
            val score = savedInstanceState.getInt(SCORE_BUNDLE_KEY)
            floatingTextView.text = Integer.toString(score)
        }
    }

    override fun onItemClick(position: Int, card: Card, viewFlipper: ViewFlipper) {
        if (shouldDispatchTouchEvent() && !viewFlipperCardWeakHashMap.containsKey(viewFlipper)) {
            viewFlipper.showNext()
            viewFlipperCardWeakHashMap[viewFlipper] = card
            gameViewModel.cardFlipped(position, card)
        }
    }

    override fun showLoadingView() {
        loadingDialog = ProgressDialog(context)
        loadingDialog.setCancelable(false)
        loadingDialog.setTitle(getString(R.string.progress_dialog_loading_images_title))
        loadingDialog.setMessage(getString(R.string.progress_dialog_loading_images_message))
        loadingDialog.show()
    }

    override fun hideLoadingView() {
        loadingDialog.dismiss()
    }

    override fun turnCardsOver() {
        Handler().postDelayed({ gameViewModel.removeCardsFromMaps() }, Constants.ROTATION_TIME.toLong())
        removeViewFlipper()
    }

    override fun notifyAdapterItemChanged(pos: Int) {
        gameAdapter.notifyItemChanged(pos)
    }

    override fun notifyAdapterItemRemoved(id: String) {
        gameAdapter.removeItem(id)
    }

    override fun setAdapterData(photoEntityList: List<PhotoEntity>) {
        gameAdapter.setDataCardImages(photoEntityList)
    }

    override fun removeViewFlipper() {
        for (viewFlipper in viewFlipperCardWeakHashMap.keys) {
            viewFlipper.showPrevious()
        }
        viewFlipperCardWeakHashMap.clear()
    }

    override fun showErrorView() {
        val alertDialogBuilder = activity?.let { AlertDialog.Builder(it) }
        alertDialogBuilder?.setTitle("There was an unexpected error")?.setMessage("Please try again")?.show()
    }

    override fun checkGameFinished() {
        if (gameAdapter.itemCount == 0) {
            // TODO Blocking first
            showScoreFragmentDialog(gameViewModel.gameScore.blockingFirst())
        }
    }

    override fun shouldDispatchTouchEvent(): Boolean = gameViewModel.shouldDispatchTouchEvent()

    override fun onScoreChanged(gameScore: Int) {
        floatingTextView.text = Integer.toString(gameScore)
        checkGameFinished()
    }

    override fun onEnterKeyPressed(playerScore: PlayerScore) {
        gameViewModel.onScoredEntered(playerScore)
                .subscribeBy(onNext = { l: Long ->
                    if (l == -1L) {
                        // TODO ERROR
                        networkViewState =
                                NetworkViewState.Error("score insertion is -1")
                    }
                })
        (activity as MainActivity).navigateTo(Screen.SCORES)
    }

    private fun showScoreFragmentDialog(score: Int) {
        val nameScoreDialogFragment = NameScoreDialogFragment.newInstance(score.toInt())
        nameScoreDialogFragment.setTargetFragment(this, 0)
        nameScoreDialogFragment.show(activity?.supportFragmentManager, "fragment_score")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.kittens_menu_item -> {
            gameViewModel.onKittensMenuItemClicked()
            true
        }
        R.id.puppies_menu_item -> {
            gameViewModel.onPuppiesMenuItemClicked()
            true
        }
        else -> false
    }

    companion object {
        const val SCORE_BUNDLE_KEY = "score"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment GameFragment.
         */
        fun newInstance(): GameFragment {
            val fragment = GameFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
