package kittentrate.game

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
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
import kittentrate.MainActivity
import kittentrate.data.di.Injection
import kittentrate.data.repository.model.PhotoEntity
import kittentrate.navigation.Screen
import kittentrate.score.PlayerScore
import kittentrate.utils.Constants
import kittentrate.view.custom.AutofitRecyclerView
import manulorenzo.me.kittentrate.R
import java.util.*

class GameFragment : Fragment(),
        GameContract.View,
        GameAdapter.OnItemClickListener,
        NameScoreDialogFragment.NameScoreKeyListener {

    @BindView(R.id.floating_textview)
    internal lateinit var floatingTextView: TextView
    @BindView(R.id.recycler_view)
    internal lateinit var autofitRecyclerView: AutofitRecyclerView
    private var gameAdapter: GameAdapter? = null
    private val viewFlipperCardWeakHashMap = WeakHashMap<ViewFlipper, Card>(Constants.NUMBER_MATCHING_CARDS)
    private lateinit var loadingDialog: ProgressDialog
    private lateinit var gamePresenter: GamePresenter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_BUNDLE_KEY, gamePresenter.score)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        val gameRepository = Injection.provideRepository(context?.applicationContext as Context)

        gamePresenter = GamePresenter(gameRepository, this)
        gameAdapter = GameAdapter(this, context?.applicationContext)

        gamePresenter.start()
    }

    override fun onDestroyView() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
        autofitRecyclerView.adapter = null
        super.onDestroyView()
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
        if (autofitRecyclerView.adapter == null) {
            autofitRecyclerView.adapter = gameAdapter
        }
        if (savedInstanceState != null && savedInstanceState.getInt(SCORE_BUNDLE_KEY) != 0) {
            val score = savedInstanceState.getInt(SCORE_BUNDLE_KEY)
            floatingTextView.text = Integer.toString(score)
        }
    }

    override fun onItemClick(position: Int, card: Card, viewFlipper: ViewFlipper) {
        if (shouldDispatchTouchEvent() && !viewFlipperCardWeakHashMap.containsKey(viewFlipper)) {
            viewFlipper.showNext()
            viewFlipperCardWeakHashMap.put(viewFlipper, card)
            gamePresenter.onItemClicked(position, card, viewFlipper)
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
        Handler().postDelayed({ gamePresenter.removeCardsFromMaps() }, Constants.ROTATION_TIME.toLong())
    }

    override fun notifyAdapterItemChanged(pos: Int) {
        gameAdapter?.notifyItemChanged(pos)
    }

    override fun notifyAdapterItemRemoved(id: String) {
        gameAdapter?.removeItem(id)
    }

    override fun setAdapterData(photoEntityList: List<PhotoEntity>) {
        gameAdapter?.setDataCardImages(photoEntityList)
    }

    override fun removeViewFlipper() {
        for (viewFlipper in viewFlipperCardWeakHashMap.keys) {
            viewFlipper.showPrevious()
        }
        viewFlipperCardWeakHashMap.clear()
    }

    override fun showErrorView() {
        val alertDialogBuilder = activity?.let { AlertDialog.Builder(it) }
        if (alertDialogBuilder != null) {
            alertDialogBuilder.setTitle("There was an unexpected error").setMessage("Please try again").show()
        }
    }

    override fun checkGameFinished() {
        if (gameAdapter?.itemCount == 0) {
            showScoreFragmentDialog(gamePresenter.score)
        }
    }

    override fun shouldDispatchTouchEvent(): Boolean {
        return gamePresenter.shouldDispatchTouchEvent()
    }

    @SuppressLint("SetTextI18n")
    override fun onScoreChanged(gameScore: Int) {
        floatingTextView.text = Integer.toString(gameScore)
    }

    override fun onEnterKeyPressed(playerScore: PlayerScore) {
        gamePresenter.onScoredEntered(playerScore)
        (activity as MainActivity).navigateTo(Screen.SCORES)
    }

    private fun showScoreFragmentDialog(score: Int) {
        val nameScoreDialogFragment = NameScoreDialogFragment.newInstance(score)
        nameScoreDialogFragment.setTargetFragment(this, 0)
        nameScoreDialogFragment.show(activity?.supportFragmentManager, "fragment_score")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.kittens_menu_item -> {
            gamePresenter.onKittensMenuItemClicked()
            true
        }
        R.id.puppies_menu_item -> {
            gamePresenter.onPuppiesMenuItemClicked()
            true
        }
        else -> false
    }

    companion object {
        val SCORE_BUNDLE_KEY = "score"

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
}// Required empty public constructor
