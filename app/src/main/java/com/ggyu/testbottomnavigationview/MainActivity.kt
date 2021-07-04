package com.ggyu.testbottomnavigationview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.ggyu.testbottomnavigationview.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    enum class LottieAnimation(val value: String) {
        A("heart-fill.json"),
        B("heart-fill.json"),
        C("heart-fill.json"),
        D("heart-fill.json"),
        E("heart-fill.json"),
    }

    val list = arrayListOf(
        LottieAnimation.A,
        LottieAnimation.B,
        LottieAnimation.C,
        LottieAnimation.D,
        LottieAnimation.E,
    )

    private lateinit var binding: ActivityMainBinding

    private val mNavView: BottomNavigationView by lazy {
        val view = binding.navView
        view.inflateMenu(R.menu.bottom_nav_menu)
        view
    }

    private var mPreClickPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menu = mNavView.menu
        menu.apply {
            (0 until menu.size()).forEach { i ->
                val id = menu.getItem(i).itemId
                this.findItem(id).icon = getLottieDrawable(list[i], mNavView)
            }
        }

        mPreClickPosition = menu.getItem(0).itemId

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(menu)

        setupActionBarWithNavController(navController, appBarConfiguration)
        mNavView.setupWithNavController(navController)

        mNavView.setOnNavigationItemSelectedListener(this)
        mNavView.setOnNavigationItemReselectedListener(this)

        mNavView.selectedItemId = menu.getItem(0).itemId
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        handleNavigationItem(item)
        return true
    }

    // Optional
    override fun onNavigationItemReselected(item: MenuItem) {
        handleNavigationItem(item)
    }

    private fun handleNavigationItem(item: MenuItem) {
        handlePlayLottieAnimation(item)
        mPreClickPosition = item.itemId
    }

    private fun handlePlayLottieAnimation(item: MenuItem) {
        val icon = item.icon as? LottieDrawable
        icon?.apply { playAnimation() }

        if (item.itemId != mPreClickPosition) {
            val menu = mNavView.menu
            val id = menu.findItem(mPreClickPosition).itemId
            val pos = (0 until menu.size()).indexOfFirst { index -> menu[index].itemId == id }
            menu.findItem(mPreClickPosition).icon = getLottieDrawable(list[pos], mNavView)
        }
    }

    private fun getLottieDrawable(
        animation: LottieAnimation,
        bottomNavigationView: BottomNavigationView
    ): LottieDrawable {
        return LottieDrawable().apply {
            val result = LottieCompositionFactory.fromAssetSync(
                bottomNavigationView.context.applicationContext, animation.value
            )
            callback = bottomNavigationView
            composition = result.value
        }
    }
}