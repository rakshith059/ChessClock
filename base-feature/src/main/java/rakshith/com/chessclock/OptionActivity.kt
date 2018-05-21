package rakshith.com.chessclock

import android.animation.ValueAnimator
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.activity_option.*
import rakshith.com.chessclock.adapters.OptionAdapter


class OptionActivity : Activity()/*, View.OnTouchListener */ {

    var dX: Float = 0.toFloat()
    var dY: Float = 0.toFloat()
    var animate: TranslateAnimation? = null

    var rvRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        rvRecyclerView = findViewById<RecyclerView>(R.id.activity_option_rv_list)

//        animate = TranslateAnimation(500f, 100f, 500f, 100f)
//        animate?.duration = 3000
//        activity_option_iv_image.startAnimation(animate)

//        activity_option_iv_image.setOnTouchListener(this)

//        activity_option_iv_image.animate().translationX(-250f).translationY(-350f).duration = 3000

        activity_option_iv_image.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        rvRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val options: ArrayList<String> = ArrayList()
        options.add(resources.getString(R.string.normal_timer))
        options.add(resources.getString(R.string.incremental_timer))
        options.add(resources.getString(R.string.handicap_timer))
        options.add(resources.getString(R.string.hourglass_timer))
        options.add(resources.getString(R.string.rules))

        rvRecyclerView?.adapter = OptionAdapter(this, options)
    }

//    override fun onTouch(view: View, event: MotionEvent): Boolean {
//
//        when (event.action) {
//
//            MotionEvent.ACTION_DOWN -> {
//
//                dX = view.getX() - event.rawX
//                dY = view.getY() - event.rawY
//            }
//
//            MotionEvent.ACTION_MOVE ->
//
//                view.animate()
//                        .x(event.rawX + dX)
//                        .y(event.rawY + dY)
//                        .setDuration(0)
//                        .start()
//            else -> return false
//        }
//        return true
//    }

}
