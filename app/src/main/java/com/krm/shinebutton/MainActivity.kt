package com.krm.shinebutton

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mTAG = "MainActivity"
    private lateinit var shineButton: ShineButton
    private lateinit var porterShapeImageView1: ShineButton
    private lateinit var porterShapeImageView2: ShineButton
    private lateinit var porterShapeImageView3: ShineButton
    private lateinit var listDemo: Button
    private lateinit var fragmentDemo: Button
    private lateinit var dialogDemo: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar
        shineButton = findViewById(R.id.po_image0)
        listDemo = findViewById(R.id.btn_list_demo)
        fragmentDemo = findViewById(R.id.btn_fragment_demo)
        dialogDemo = findViewById(R.id.btn_dialog_demo)
        val linearLayout = findViewById<LinearLayout>(R.id.wrapper)
        shineButton.init(this)
        porterShapeImageView1 = findViewById(R.id.po_image1)
        porterShapeImageView1.init(this)
        porterShapeImageView2 = findViewById(R.id.po_image2)
        porterShapeImageView2.init(this)
        porterShapeImageView3 = findViewById(R.id.po_image3)
        porterShapeImageView3.init(this)
        val shineButtonJava = ShineButton(this)
        shineButtonJava.setBtnColor(Color.GRAY)
        shineButtonJava.setBtnFillColor(Color.RED)
        shineButtonJava.setShapeResource(R.raw.heart)
        shineButtonJava.setAllowRandomColor(true)
        shineButtonJava.setShineSize(100)
        val layoutParams = LinearLayout.LayoutParams(100, 100)
        shineButtonJava.layoutParams = layoutParams
        linearLayout?.addView(shineButtonJava)
        shineButton.setOnClickListener {
            Log.e(
                mTAG,
                "click"
            )
        }
        shineButton.setOnCheckStateChangeListener { _: View?, checked: Boolean ->
            Log.e(
                mTAG,
                "click $checked"
            )
        }
        porterShapeImageView2.setOnClickListener {
            Log.e(
                mTAG,
                "click"
            )
        }
        porterShapeImageView3.setOnClickListener {
            Log.e(
                mTAG,
                "click"
            )
        }
        listDemo.setOnClickListener { v: View ->
            startActivity(
                Intent(v.context, ListDemoActivity::class.java)
            )
        }
        fragmentDemo.setOnClickListener { showFragmentPage() }
        dialogDemo.setOnClickListener(fun(_: View) {
            val dialog = Dialog(this@MainActivity)
            @SuppressLint("InflateParams") val view =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog, null)
            val shineButton: ShineButton = view.findViewById(R.id.po_image)
            shineButton.setFixDialog(dialog)
            shineButton.setOnClickListener {
                Log.e(
                    mTAG,
                    "click"
                )
            }
            dialog.setContentView(view)
            dialog.show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fragment_page) {
            showFragmentPage()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragmentPage() {
        FragmentDemo().showFragment(supportFragmentManager)
    }

    companion object {
        fun setFullScreen(activity: AppCompatActivity) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}