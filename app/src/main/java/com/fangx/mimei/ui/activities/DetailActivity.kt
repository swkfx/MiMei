package com.fangx.mimei.ui.activities

import android.os.Bundle
import com.fangx.mimei.R
import com.fangx.mimei.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity() {

    companion object {
        const val KEY_ML_ID = "key_ml_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        textView.text = intent.getStringExtra(KEY_ML_ID)
    }
}
