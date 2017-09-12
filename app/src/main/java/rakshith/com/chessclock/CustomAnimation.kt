package rakshith.com.chessclock

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

/**
 * Created by rakshith on 9/11/17.
 */

class CustomAnimation(startWeight: Float, endWeight: Float, content: LinearLayout) : Animation() {
    var mStartWeight = startWeight
    var mEndWeight = endWeight
    var mContent = content

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val lp: LinearLayout.LayoutParams = mContent.getLayoutParams() as LinearLayout.LayoutParams
        lp.weight = mStartWeight + mEndWeight * interpolatedTime
        mContent.setLayoutParams(lp)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}