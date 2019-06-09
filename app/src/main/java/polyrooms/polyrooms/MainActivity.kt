
package polyrooms.polyrooms

import android.animation.Animator
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.util.*
import android.R.id.edit
import android.content.SharedPreferences.Editor
import com.google.gson.Gson
import android.text.method.TextKeyListener.clear
import android.R.id.edit
import android.animation.AnimatorListenerAdapter
import android.content.SharedPreferences




class MainActivity : AppCompatActivity() {

    var ghour: Int = -1
    var gmin: Int = -1
    var gday: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        //STORE A CUSTOM OBJECT TO SHARED PREFERENCES
        /*
        val prefsEditor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(myObject) // myObject - instance of MyObject
        prefsEditor.putString("MyObject", json)
        prefsEditor.commit()
        */

        //RETRIEVE INSTANCE OF CUSTOM OBJECT
        /*
        val gson = Gson()
        val json = sharedPreferences.getString("MyObject", "")
        val obj = gson.fromJson<MyObject>(json, MyObject::class.java!!)
        */


        var savedReservation: String = "null"
        if (sharedPreferences.contains("first_reservation")) {
            savedReservation = sharedPreferences.getString("first_reservation", "")
        }

        object : CountDownTimer(850, 1000) {
            override fun onFinish() {
                loadingProgressBarHorizontal.visibility = View.GONE
                rootView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.mountainMeadow))
                prIconImageView.setImageResource(R.drawable.pricon)
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()

        Toast.makeText(this, savedReservation, Toast.LENGTH_LONG).show()
    }

    private fun startAnimation() {

        prIconImageView.animate().apply {
            x(50f)//50
            y(100f)
            duration = 450
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
                //nothing
            }

            override fun onAnimationEnd(p0: Animator?) {
                afterAnimationView.visibility = VISIBLE
            }

            override fun onAnimationCancel(p0: Animator?) {
                //nothing
            }

            override fun onAnimationStart(p0: Animator?) {
                //nothing
            }
        })
    }

    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            ghour = h
            gmin = m

            val hconverted = convertH(h)
            val mconverted = convertM(m)
            val ampm = getAP(h)
            timeButton.text = hconverted.toString() + ":" + mconverted + " " + ampm

            sharedPreferences.edit().putString("first_reservation", hconverted.toString()).apply()

        }),hour,minute,false)

        tpd.show()
    }

    fun clickDate(view: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_items)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.Sun -> {
                    gday = item.title.toString()
                }
                R.id.Mon -> {
                    gday = item.title.toString()
                }
                R.id.Tue -> {
                    gday = item.title.toString()
                }
                R.id.Wed -> {
                    gday = item.title.toString()
                }
                R.id.Thur -> {
                    gday = item.title.toString()
                }
                R.id.Fri -> {
                    gday = item.title.toString()
                }
                R.id.Sat -> {
                    gday = item.title.toString()
                }
            }
            dateButton.text = this.gday
            true
        })

        popup.show()
    }

    fun clickFind(view: View) {
        if (ghour == -1) {
            Toast.makeText(this, "You must select a time", Toast.LENGTH_LONG).show()
        }

        else if (gday.equals("null")) {
            Toast.makeText(this, "You must select a day", Toast.LENGTH_LONG).show()
        }

        else {
            var time = Time(Day.SUN, 0)

            if (gday.equals("Sunday")) {
                time = Time(Day.SUN, ghour)
            }
            else if (gday.equals("Monday")) {
                time = Time(Day.MON, ghour)
            }
            else if (gday.equals("Tuesday")) {
                time = Time(Day.TUE, ghour)
            }
            else if (gday.equals("Wednesday")) {
                time = Time(Day.WED, ghour)
            }
            else if (gday.equals("Thursday")) {
                time = Time(Day.THU, ghour)
            }
            else if (gday.equals("Friday")) {
                time = Time(Day.FRI, ghour)
            }
            else if (gday.equals("Saturday")) {
                time = Time(Day.SAT, ghour)
            }
            else {
                //wig out man
            }
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("time", time as Serializable)
            startActivity(intent)
        }
    }

    fun clickReport(view: View){
        val intent = Intent(this, ReportActivity::class.java)
        startActivity(intent)
    }

    fun clickReservation(view: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(this, view)
        popup.inflate(R.menu.res_buttons)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.check_in -> {
                    clickCheckIn()
                }
                R.id.cancel -> {
                    clickCancel()
                }
            }
            true
        })
        popup.show()
    }

    fun clickCancel() {
        val sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.clear()
        prefsEditor.apply()

        Toast.makeText(this, "You have cancelled your reservation", Toast.LENGTH_LONG).show()

        reservationsView.visibility = View.GONE
    }

    fun clickCheckIn() {
        val sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.clear()
        prefsEditor.apply()

        Toast.makeText(this, "Thank you, you are checked in", Toast.LENGTH_LONG).show()

        reservationsView.visibility = View.GONE
    }

}
// helper funcs for string time display
fun getAP(hour: Int): String {
    if (hour > 12){
        return "pm"
    }
    else {
        return "am"
    }
}

fun convertH(hour: Int): Int {
    if (hour > 12) {
        return hour - 12
    }
    else if (hour == 0) {
        return hour + 12
    }
    else {
        return hour
    }
}

fun convertM(min: Int): String {
    if (min < 10){
        return "0" + min
    }

    else {
        return min.toString()
    }
}
