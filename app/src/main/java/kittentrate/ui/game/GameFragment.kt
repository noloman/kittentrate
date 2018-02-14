package kittentrate.ui.game

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
import kittentrate.data.model.PhotoEntity
import kittentrate.data.viewmodel.GameViewModel
import kittentrate.data.viewmodel.NetworkViewState
import kittentrate.ui.MainActivity
import kittentrate.ui.navigation.Screen
import kittentrate.ui.score.PlayerScore
import kittentrate.ui.view.custom.AutofitRecyclerView
import manulorenzo.me.kittentrate.R
import kotlin.properties.Delegates

/**
 * Copyright 2018 Manuel Lorenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class GameFragment : Fragment(), GameAdapter.OnItemClickListener,
        NameScoreDialogFragment.NameScoreKeyListener {
    @BindView(R.id.floating_textview)
    internal lateinit var floatingTextView: TextView
    @BindView(R.id.recycler_view)
    internal lateinit var autofitRecyclerView: AutofitRecyclerView
    private lateinit var gameAdapter: GameAdapter
    private lateinit var loadingDialog: ProgressDialog
    private lateinit var gameViewModel: GameViewModel

    private var networkViewState: NetworkViewState by Delegates.observable<NetworkViewState>(
            NetworkViewState.None(),
            { _, _, newValue ->
                when (newValue) {
                    is NetworkViewState.Loading -> showLoadingView()
                    is NetworkViewState.Success<*> -> {
                        hideLoadingView()
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
        val bla: Game? = gameViewModel.gameMutableLiveData.value
        if (bla?.score != null) {
            outState.putInt(SCORE_BUNDLE_KEY, bla.score)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        gameAdapter = GameAdapter(this)
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        gameViewModel.networkViewStateMutableLiveData.observe(this,
                android.arch.lifecycle.Observer { networkViewState: NetworkViewState? ->
                    networkViewState?.let { it ->
                        this@GameFragment.networkViewState = it
                    }
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
        gameViewModel.photosMutableLiveData.observeForever({
            it?.let { it1 ->
                setAdapterData(it1)
            }
        })
        // We first fetch the photos
        gameViewModel.getPhotos()
    }

    override fun onItemClick(position: Int, card: Card, viewFlipper: ViewFlipper) {
        if (shouldDispatchTouchEvent() && !gameViewModel.viewFlipperCardWeakHashMap.containsKey(viewFlipper)) {
            viewFlipper.showNext()
            gameViewModel.viewFlipperCardWeakHashMap[viewFlipper] = card
            gameViewModel.cardFlipped(position, card)
        }
    }

    private fun showLoadingView() {
        loadingDialog = ProgressDialog(context)
        loadingDialog.setCancelable(false)
        loadingDialog.setTitle(getString(R.string.progress_dialog_loading_images_title))
        loadingDialog.setMessage(getString(R.string.progress_dialog_loading_images_message))
        loadingDialog.show()
    }

    private fun hideLoadingView() {
        loadingDialog.dismiss()
    }

    fun notifyAdapterItemChanged(pos: Int) {
        gameAdapter.notifyItemChanged(pos)
    }

    fun notifyAdapterItemRemoved(id: String) {
        // gameAdapter.removeItem(id)
    }

    private fun setAdapterData(photoEntityList: List<PhotoEntity>) {
        gameAdapter.setDataCardImages(photoEntityList)
        // TODO ugliest thing ever: we want to listen to the score changes until the adapter is initialized with the newest pictures
        gameViewModel.gameMutableLiveData.observeForever({ it -> it?.let { it1 -> onScoreChanged(it1) } })
    }

    fun removeViewFlipper() {
        for (viewFlipper in gameViewModel.viewFlipperCardWeakHashMap.keys) {
            viewFlipper.showPrevious()
        }
        gameViewModel.viewFlipperCardWeakHashMap.clear()
    }

    private fun showErrorView() {
        val alertDialogBuilder = activity?.let { AlertDialog.Builder(it) }
        alertDialogBuilder?.setTitle("There was an unexpected error")?.setMessage("Please try again")?.show()
    }

    private fun checkGameFinished() {
        if (gameAdapter.itemCount == 0) {
            val bla: Game? = gameViewModel.gameMutableLiveData.value
            if (bla != null) {
                showScoreFragmentDialog(bla.score)
            }
        }
    }

    private fun shouldDispatchTouchEvent(): Boolean = gameViewModel.shouldDispatchTouchEvent()

    private fun onScoreChanged(game: Game) {
        floatingTextView.text = Integer.toString(game.score)
        checkGameFinished()
    }

    override fun onEnterKeyPressed(playerScore: PlayerScore) {
        gameViewModel.onScoredEntered(playerScore)
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
