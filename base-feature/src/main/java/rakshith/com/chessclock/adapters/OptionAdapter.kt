package rakshith.com.chessclock.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.option_row.view.*
import rakshith.com.chessclock.ChessRulesActivity
import rakshith.com.chessclock.MainActivity
import rakshith.com.chessclock.R

/**
 * Created ChessClock by rakshith on 9/13/17.
 */

class OptionAdapter(context: Context, optionList: ArrayList<String>) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {
    var mContext = context
    var mOptionList = optionList

    override fun onBindViewHolder(holder: OptionViewHolder?, position: Int) {
        holder?.bindItems(mOptionList.get(position))

        holder?.cvItemCard?.setOnClickListener {
            if (position != mOptionList.size - 1)
                mContext.startActivity(Intent(mContext, MainActivity::class.java))
            else
                mContext.startActivity(Intent(mContext, ChessRulesActivity::class.java))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OptionViewHolder {
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.option_row, parent, false)
        return OptionViewHolder(view)
    }

    class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rowOptionString: TextView? = null
        var cvItemCard: CardView? = null

        fun bindItems(option: String) {
            cvItemCard = itemView?.findViewById<CardView>(R.id.option_row_cv_item_card)
            rowOptionString = itemView?.findViewById<TextView>(R.id.option_row_tv_timer_name)
            rowOptionString?.setText(option)
        }
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }
}
