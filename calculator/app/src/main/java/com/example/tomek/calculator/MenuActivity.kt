package com.example.tomek.calculator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.menu_activity.*


class MenuActivity : AppCompatActivity() {

    //TODO: capture orientation changes and change button margins
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        configureExitBtnAction()
        configureAboutBtn()
        configureAdvancedCalculatorBtnAction()
        configureBasicCalculatorBtnAction()
    }


    private fun configureAboutBtn() {
        aboutBtn.setOnClickListener { startNewActivity(AboutActivity::class.java) }
    }


    private fun configureExitBtnAction() {
        exitBtn.setOnClickListener { finish() }
    }

    private fun configureAdvancedCalculatorBtnAction() {
        advancedModeBtn.setOnClickListener { startNewActivity(AdvancedCalculatorActivity::class.java) }
    }

    private fun configureBasicCalculatorBtnAction() {
        simpleModeBtn.setOnClickListener { startNewActivity(SimpleCalculatorActivity::class.java) }
    }


    //TODO: extract this to the constant
    private fun startNewActivity(activityToStart: Class<out Any>) {
        val intent = Intent(this, activityToStart)
        startActivity(intent)
    }

}
