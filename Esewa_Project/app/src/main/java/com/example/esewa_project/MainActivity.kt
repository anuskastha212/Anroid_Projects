package com.example.esewa_project

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.esewa_project.databinding.ActivityMainBinding
import com.example.esewa_project.ui.fragments.CartFragment
import com.example.esewa_project.ui.fragments.FavouriteFragment
import com.example.esewa_project.ui.fragments.HomeFragment
import com.example.esewa_project.ui.fragments.MoreFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedTab = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        val shop: LinearLayout? = findViewById(R.id.navItemShop)
        val cart: LinearLayout? = findViewById(R.id.navItemCart)
        val favourite: LinearLayout? = findViewById(R.id.navItemFavourite)
        val more: LinearLayout? = findViewById(R.id.navItemMore)

        val shopIcon: ImageView? = findViewById(R.id.iconShop)
        val cartIcon: ImageView? = findViewById(R.id.iconCart)
        val favouriteIcon: ImageView? = findViewById(R.id.iconFavourite)
        val moreIcon: ImageView? = findViewById(R.id.iconMore)

        val shopText: TextView? = findViewById(R.id.textShop)
        val cartText: TextView? = findViewById(R.id.textCart)
        val favouriteText: TextView? = findViewById(R.id.textFavourite)
        val moreText: TextView? = findViewById(R.id.textMore)

        if(intent.getBooleanExtra("openHome", false)){
            loadFragment(HomeFragment())
        }

        shop?.setOnClickListener {
            if (selectedTab != 1) {
                loadFragment(HomeFragment())

                onSelect(shop, shopText, shopIcon)
                onDeselect(cart, cartText, cartIcon)
                onDeselect(favourite, favouriteText, favouriteIcon)
                onDeselect(more, moreText, moreIcon)

                selectedTab = 1
            }
        }

        cart?.setOnClickListener {
            if (selectedTab != 2) {
                loadFragment(CartFragment())

                onSelect(cart, cartText, cartIcon)
                onDeselect(shop, shopText, shopIcon)
                onDeselect(favourite, favouriteText, favouriteIcon)
                onDeselect(more, moreText, moreIcon)

                selectedTab = 2
            }
        }

        favourite?.setOnClickListener {
            if (selectedTab != 3) {
                loadFragment(FavouriteFragment())

                onSelect(favourite, favouriteText, favouriteIcon)
                onDeselect(shop, shopText, shopIcon)
                onDeselect(cart, cartText, cartIcon)
                onDeselect(more, moreText, moreIcon)

                selectedTab = 3
            }
        }

        more?.setOnClickListener {
            if (selectedTab != 4) {
                loadFragment(MoreFragment())

                onSelect(more, moreText, moreIcon)
                onDeselect(shop, shopText, shopIcon)
                onDeselect(cart, cartText, cartIcon)
                onDeselect(favourite, favouriteText, favouriteIcon)

                selectedTab = 4
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    private fun onSelect(linearLayout: LinearLayout?, textView: TextView?, imageView: ImageView?) {
        textView?.visibility = View.VISIBLE
        imageView?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
        linearLayout?.setBackgroundResource(R.drawable.bg_bottom_nav)
        linearLayout?.animate()
            ?.scaleX(1.1f)
            ?.scaleY(1.1f)
            ?.setDuration(100)
            ?.withEndAction {
                linearLayout?.animate()
                    ?.scaleX(1f)
                    ?.scaleY(1f)
                    ?.setDuration(100)
            }
    }

    private fun onDeselect(
        linearLayout: LinearLayout?,
        textView: TextView?,
        imageView: ImageView?
    ) {
        textView?.visibility = View.GONE
        imageView?.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        linearLayout?.setBackgroundResource(android.R.color.transparent)
    }
}


