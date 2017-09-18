package rakshith.com.chessclock

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import kotlinx.android.synthetic.main.activity_chess_rules.*
import android.text.Spanned


class ChessRulesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chess_rules)

        val result: Spanned = Html.fromHtml(resources.getString(R.string.chess_rules))
        result.to(activity_chess_rules_tv_rules)
    }
}
