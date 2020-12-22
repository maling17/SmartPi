package com.example.smartpi.view.landingpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.smartpi.R
import com.example.smartpi.adapter.LandingPagePagerAdapter
import com.example.smartpi.databinding.ActivityBaseLandingPageBinding
import com.example.smartpi.view.sign.SignInActivity


class BaseLandingPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityBaseLandingPageBinding
    private var currentPos = 0
    private val next = "Next"
    private val done = "Done"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseLandingPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.viewPagerLanding.adapter = LandingPagePagerAdapter(supportFragmentManager)
        binding.viewPagerLanding.currentItem = currentPos

        binding.tvNext.setOnClickListener { nextSlide() }
        binding.tvSkip.setOnClickListener { binding.viewPagerLanding.currentItem = 2 }

        val changeListener: OnPageChangeListener = object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                currentPos = position
                when (position) {
                    0 -> {
                        binding.circle1.setImageResource(R.drawable.cirlce_blue)
                        binding.circle2.setImageResource(R.drawable.cirlce_grey)
                        binding.circle3.setImageResource(R.drawable.cirlce_grey)
                        binding.tvSkip.visibility = View.VISIBLE
                        binding.tvNext.text = next
                        binding.tvNext.setOnClickListener { nextSlide() }
                        binding.tvSkip.setOnClickListener {
                            binding.viewPagerLanding.currentItem = 2
                        }
                    }
                    1 -> {
                        binding.circle1.setImageResource(R.drawable.cirlce_grey)
                        binding.circle2.setImageResource(R.drawable.cirlce_blue)
                        binding.circle3.setImageResource(R.drawable.cirlce_grey)
                        binding.tvSkip.visibility = View.VISIBLE
                        binding.tvNext.text = next
                        binding.tvNext.setOnClickListener { nextSlide() }
                        binding.tvSkip.setOnClickListener {
                            binding.viewPagerLanding.currentItem = 2
                        }
                    }
                    2 -> {
                        binding.circle1.setImageResource(R.drawable.cirlce_grey)
                        binding.circle2.setImageResource(R.drawable.cirlce_grey)
                        binding.circle3.setImageResource(R.drawable.cirlce_blue)
                        binding.tvSkip.visibility = View.GONE
                        binding.tvNext.text = done
                        binding.tvNext.setOnClickListener {
                            startActivity(
                                Intent(
                                    this@BaseLandingPageActivity,
                                    SignInActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                    else -> {

                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }
        binding.viewPagerLanding.addOnPageChangeListener(changeListener)
    }

    fun nextSlide() {
        binding.viewPagerLanding.currentItem = currentPos + 1
    }
}