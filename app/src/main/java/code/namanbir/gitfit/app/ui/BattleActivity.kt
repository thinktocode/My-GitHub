package code.namanbir.gitfit.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import code.namanbir.gitfit.app.R
import code.namanbir.gitfit.app.data.model.ResultModel
import code.namanbir.gitfit.app.databinding.ActivityBattleBinding

class BattleActivity : AppCompatActivity() {

    //todo help option in appbar to show dialog to tell how result is calculated.

    companion object {
        private const val INTENT_BATTLE_DATA = "intent_battle_data"
        fun start(context: Context, resultModel: ResultModel) {
            val intent = Intent(context, BattleActivity::class.java)
            intent.putExtra(INTENT_BATTLE_DATA, resultModel)
            context.startActivity(intent)
        }
    }

    private lateinit var viewBinding: ActivityBattleBinding

    private var resultModel = ResultModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_battle)
        setSupportActionBar(viewBinding.toolBattle)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        handleIntent()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                showDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setCancelable(true)
        alertDialog.setTitle("Score Calculator")
        alertDialog.setMessage("Star gives you 3 points\nFork gives you 5 points\nFollow gives you 1 point\nPublic repo give 1 point")
        alertDialog.show()
    }

    private fun handleIntent() {
        resultModel = intent.getParcelableExtra<ResultModel>(INTENT_BATTLE_DATA) as ResultModel
    }

    private fun updateWinner(user: ResultModel.User?, score: String){

        Glide.with(this)
            .load(user?.avatarUrl)
            .into(viewBinding.imUserImage1)

        viewBinding.tvUserName1.text = user?.name
        viewBinding.tvUserScore1.text = score
        viewBinding.tvPublicRepo1.text = "Public Repo : ${user?.publicRepos}"
        viewBinding.tvFollowersUser1.text = user?.followers.toString()
        viewBinding.tvFollowingUser1.text = user?.following.toString()
    }

    private fun updateLoser(user: ResultModel.User?, score: String) {

        Glide.with(this)
            .load(user?.avatarUrl)
            .into(viewBinding.imUserImage2)

        viewBinding.tvUserName2.text = user?.name
        viewBinding.tvUserScore2.text = score
        viewBinding.tvPublicRepo2.text = "Public Repo : ${user?.publicRepos}"
        viewBinding.tvFollowersUser2.text = user?.followers.toString()
        viewBinding.tvFollowingUser2.text = user?.following.toString()
    }

    /**winner parameter = {1-> User 1 is winner; 2 -> User 2 is winner; 0 -> Draw}*/
    private fun findWinner():Int{
        return when {
            resultModel.score1!! == resultModel.score2!! -> 0
            resultModel.score1!! > resultModel.score2!! -> 1
            else -> 2
        }
    }

    private fun updateUI() {
        when (val winner = findWinner()) {
            0, 1 -> {

                if (winner == 0) { //Draw case
                    viewBinding.imageView3.setImageResource(R.drawable.party)
                    viewBinding.resultLayout.visibility = View.GONE
                    viewBinding.drawResult.visibility = View.VISIBLE
                }
                updateWinner(resultModel.user1, resultModel.score1.toString())
                updateLoser(resultModel.user2, resultModel.score2.toString())
            }
            2 -> {
                updateWinner(resultModel.user2, resultModel.score2.toString())
                updateLoser(resultModel.user1, resultModel.score1.toString())
            }
        }
    }
}