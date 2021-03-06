package rakshith.com.chessclock

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.RadioGroup


class MainActivity : AppCompatActivity() {

    var ONE_SECOND: Long = 1000
    var ONE_MINUTE: Long = 60 * ONE_SECOND
    var ALERT_TIME: Long = 30 * ONE_SECOND
    var TEXT_TIME_COMPLETE_SIZE = 25.0F
    var ANIMATION_DURATION: Long = 2500
    var incrementTime: Long = 0 * ONE_SECOND

    var animation: Animation = AlphaAnimation(0.0f, 1.0f)

    var isPlayerOneTurn: Boolean = true

    internal var countDownTimer1: CountDownTimer? = null
    var timerLength1: Long = 0 * ONE_MINUTE
    var timeLeft1: Long = timerLength1

    private var isPause1: Boolean = true

    internal var countDownTimer2: CountDownTimer? = null
    var timerLength2: Long = 0 * ONE_MINUTE
    var timeLeft2: Long = timerLength2

    private var isPause2: Boolean = true

    var mediaPlayer: MediaPlayer? = null


    var llTimerPlayer1: LinearLayout? = null
    var llTimerPlayer2: LinearLayout? = null

    var tvPlayerOne: TextView? = null
    var etTime1: EditText? = null
    var tilTime1: TextInputLayout? = null
    var etIncrementTime1: EditText? = null
    var tilIncrementTime1: TextInputLayout? = null
    var etTime2: EditText? = null
    var tilTime2: TextInputLayout? = null
    var etIncrementTime2: EditText? = null
    var tilIncrementTime2: TextInputLayout? = null
    var rgTimer: RadioGroup? = null
    var rbNormalTimer: RadioButton? = null
    var rbIncrementalTimer: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_main)

        //to keep screen ON always
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //to use full screen
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        activity_main_iv_play.visibility = View.GONE

        setTimer1(timerLength1)
        setTimer2(timerLength2)

        activity_main_ll_chess_timer1.setOnClickListener {
            if (!isPause1) {
                timerPause1()
                timerResume2()
            }
        }
        activity_main_ll_chess_timer2.setOnClickListener {
            if (!isPause2) {
                timerPause2()
                timerResume1()
            }
        }

        activity_main_iv_timer.setOnClickListener {
            showTimerDialog()
        }
        activity_main_iv_play.setOnClickListener {
            if (isPause1 && isPause2) {
                timerResume1()
                activity_main_iv_play.isEnabled = false
                activity_main_iv_play.visibility = View.GONE
            }
        }
        activity_main_iv_reset.setOnClickListener {
            if (mediaPlayer?.isPlaying as Boolean || mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            }

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
//        activity_main_iv_pause.setOnClickListener {
//            if (!isPause1 || !isPause2) {
//                timerPause1()
//                timerPause2()
//            }
//        }
    }

    fun blinkAnimation(textView: TextView) {
        animation.duration = ONE_SECOND
        animation.startOffset = 20
        animation.repeatCount = Animation.INFINITE
        textView.startAnimation(animation)
    }

    fun stopBlinkAnimation() {
        animation.cancel()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    private fun showTimerDialog() {
        var dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        var timerDialogView: View? = LayoutInflater.from(this)?.inflate(R.layout.set_timer_layout, null)
        dialog.setView(timerDialogView)

        llTimerPlayer1 = timerDialogView?.findViewById(R.id.set_timer_layout_ll_player1_timer) as LinearLayout
        llTimerPlayer2 = timerDialogView?.findViewById(R.id.set_timer_layout_ll_player2_timer) as LinearLayout

        tvPlayerOne = timerDialogView?.findViewById(R.id.set_timer_layout_tv_player_one_text) as TextView

        etTime1 = timerDialogView?.findViewById(R.id.set_timer_layout_et_time1) as EditText
        tilTime1 = timerDialogView?.findViewById(R.id.set_timer_layout_til_time1) as TextInputLayout
        etIncrementTime1 = timerDialogView?.findViewById(R.id.set_timer_layout_et_increment_time1) as EditText
        tilIncrementTime1 = timerDialogView?.findViewById(R.id.set_timer_layout_til_increment_time1) as TextInputLayout

        etTime2 = timerDialogView?.findViewById(R.id.set_timer_layout_et_time2) as EditText
        tilTime2 = timerDialogView?.findViewById(R.id.set_timer_layout_til_time2) as TextInputLayout
        etIncrementTime2 = timerDialogView?.findViewById(R.id.set_timer_layout_et_increment_time2) as EditText
        tilIncrementTime2 = timerDialogView?.findViewById(R.id.set_timer_layout_til_increment_time2) as TextInputLayout

        rgTimer = timerDialogView?.findViewById(R.id.set_timer_layout_rg_timer) as RadioGroup
        rbNormalTimer = timerDialogView?.findViewById(R.id.set_timer_layout_rb_normal_timer) as RadioButton
        rbIncrementalTimer = timerDialogView?.findViewById(R.id.set_timer_layout_rb_incremental_timer) as RadioButton

        rbNormalTimer?.isChecked = true
        rgTimer?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.set_timer_layout_rb_normal_timer -> {
                    tilIncrementTime1?.visibility = View.GONE
                    hidePlayerTwoTimerView(View.GONE)
                }
                R.id.set_timer_layout_rb_incremental_timer -> {
                    tilIncrementTime1?.visibility = View.VISIBLE
                    hidePlayerTwoTimerView(View.GONE)
                }
                R.id.set_timer_layout_rb_handicap_timer -> {
                    hidePlayerTwoTimerView(View.VISIBLE)
                }
            }
        })

        dialog.setCancelable(false)

        dialog.setTitle(resources.getString(R.string.timer_length))
                .setPositiveButton(resources.getString(R.string.ok), { _, which -> setTimerLength() })
                .setNegativeButton(resources.getString(R.string.cancel), { dialog, which -> dialog.cancel() })
                .setIcon(resources.getDrawable(R.drawable.ic_timer))
                .show()
    }

    private fun hidePlayerTwoTimerView(visibility: Int) {
        llTimerPlayer2?.visibility = visibility
        tvPlayerOne?.visibility = visibility
    }

    @SuppressLint("ResourceType")
    private fun setTimerLength() {
        var enteredTimeString: String = etTime1?.text.toString()
        var enteredIncrementString: String = etIncrementTime1?.text.toString()

        if (!TextUtils.isEmpty(enteredTimeString) && !enteredTimeString.equals("0")) {
            tilTime1?.isErrorEnabled = false

            var enteredTimeLong = enteredTimeString.toLong() * ONE_MINUTE

            setTimer1(enteredTimeLong)
            setTimer2(enteredTimeLong)

            activity_main_iv_play.visibility = View.VISIBLE
            activity_main_iv_timer.visibility = View.GONE

            if (!TextUtils.isEmpty(enteredIncrementString)) {
                incrementTime = enteredIncrementString.toLong() * ONE_SECOND
            }
        } else {
            tilTime1?.setErrorTextAppearance(R.string.enter_time_in_minute)
        }
    }

    private fun setTimer1(timerLength: Long) {
        var seconds = (timerLength / ONE_SECOND)
        timeLeft1 = timerLength
        val minutes = seconds / 60
        seconds = seconds % 60
        activity_main_chess_timer1.setText("" + String.format("%02d", minutes)
                + ":" + String.format("%02d", seconds))
    }

    private fun setTimer2(timerLength: Long) {
        timeLeft2 = timerLength

        var seconds = (timerLength / ONE_SECOND)
        val minutes = seconds / 60
        seconds = seconds % 60
        activity_main_chess_timer2.setText("" + String.format("%02d", minutes)
                + ":" + String.format("%02d", seconds))
    }

    private fun getCountdownTimer1(timerLength1: Long) {
        countDownTimer1 = object : CountDownTimer(timerLength1, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {

                if (millisUntilFinished < ALERT_TIME) {
                    playAlertSound(R.raw.times_up)
                    blinkAnimation(activity_main_chess_timer1)
                }
                isPause1 = false

                timeLeft1 = millisUntilFinished
                var seconds = (millisUntilFinished / ONE_SECOND)
                val minutes = seconds / 60
                seconds = seconds % 60
                activity_main_chess_timer1.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds))
            }

            override fun onFinish() {
                activity_main_chess_timer1.setTextSize(TEXT_TIME_COMPLETE_SIZE)
                activity_main_chess_timer1.setText(resources.getString(R.string.player_two_win_by_time))
                disableClickOnTimer()
                stopBlinkAnimation()
            }
        }.start()
    }

    private fun disableClickOnTimer() {
        activity_main_ll_chess_timer1.isEnabled = false
        activity_main_ll_chess_timer2.isEnabled = false
        activity_main_iv_reset.visibility = View.VISIBLE
        playAlertSound(R.raw.times_up)
    }

    private fun playAlertSound(musicId: Int) {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
        }
        mediaPlayer = MediaPlayer.create(this, musicId)
        mediaPlayer?.start()
    }

    private fun getCountdownTimer2(timerLength2: Long) {
        countDownTimer2 = object : CountDownTimer(timerLength2, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished < ALERT_TIME) {
                    playAlertSound(R.raw.times_up)
                    blinkAnimation(activity_main_chess_timer2)
                }

                isPause2 = false

                timeLeft2 = millisUntilFinished
                var seconds = (millisUntilFinished / ONE_SECOND)
                val minutes = seconds / 60
                seconds = seconds % 60
                activity_main_chess_timer2.setText("" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds))
            }


            override fun onFinish() {
                activity_main_chess_timer2.setTextSize(TEXT_TIME_COMPLETE_SIZE)
                activity_main_chess_timer2.setText(resources.getString(R.string.player_one_win_by_time))
                disableClickOnTimer()
                stopBlinkAnimation()
            }
        }.start()
    }

    private fun timerPause1() {
        isPause1 = true
        countDownTimer1?.cancel()
        playAlertSound(R.raw.click)
        stopBlinkAnimation()
        if (incrementTime != 0 * ONE_SECOND)
            timeLeft1 += incrementTime
    }

    private fun timerResume1() {
        getCountdownTimer1(timeLeft1)

//        var animation1: Animation = CustomAnimation(2f, 1f, activity_main_ll_chess_timer1)
//        animation.duration = ANIMATION_DURATION
//        activity_main_ll_chess_timer1.startAnimation(animation1)

//        var animation2: Animation = CustomAnimation(1f, 2f, activity_main_ll_chess_timer2)
//        animation.duration = ANIMATION_DURATION
//        activity_main_ll_chess_timer2.startAnimation(animation2)

        activity_main_ll_chess_timer1.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1F))
        activity_main_ll_chess_timer2.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2F))
    }

    private fun timerPause2() {
        countDownTimer2?.cancel()
        isPause2 = true
        playAlertSound(R.raw.click)
        stopBlinkAnimation()

        if (incrementTime != 0 * ONE_SECOND)
            timeLeft2 += incrementTime
    }

    private fun timerResume2() {
        getCountdownTimer2(timeLeft2)

//        var animation1: Animation = CustomAnimation(1f, 2f, activity_main_ll_chess_timer1)
//        animation.duration = ANIMATION_DURATION
//        activity_main_ll_chess_timer1.startAnimation(animation1)

//        var animation2: Animation = CustomAnimation(2f, 1f, activity_main_ll_chess_timer2)
//        animation.duration = ANIMATION_DURATION
//        activity_main_ll_chess_timer2.startAnimation(animation2)

        activity_main_ll_chess_timer2.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1F))
        activity_main_ll_chess_timer1.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2F))
    }

    override fun onStop() {
        if (!isPause1 || !isPause2)
            if (!isPause1) {
                timerPause1()
                isPlayerOneTurn = true
            } else if (!isPause2) {
                timerPause2()
                isPlayerOneTurn = false
            }
        super.onStop()
    }

    override fun onRestart() {
        if (isPlayerOneTurn)
            timerResume1()
        else
            timerResume2()
        super.onRestart()
    }
}