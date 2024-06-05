package com.example.ana.presentation.ui.fragments.onboarding

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.databinding.FragmentOnboardingBinding
import com.example.ana.presentation.extensions.activityNavController
import com.example.ana.presentation.extensions.navigateSafely
import com.example.ana.presentation.ui.adapters.SliderAdapter
import com.teenteen.teencash.presentation.base.BaseFragment

class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    private lateinit var sliderAdapter: SliderAdapter
    private var dots: Array<TextView?>? = null
    private lateinit var layouts: Array<Int>

    override fun attachBinding(
        list: MutableList<FragmentOnboardingBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentOnboardingBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun setupViews() {
        init()
        dataSet()
        interactions()
    }

    private fun init() {
        layouts = arrayOf(
            R.layout.onboarding_slide1,
            R.layout.onboarding_slide2,
            R.layout.onboarding_slide3
        )
        sliderAdapter = SliderAdapter(requireActivity(), layouts)
    }

    private fun dataSet() {
        addBottomDots(0)
        binding.slider.apply {
            adapter = sliderAdapter
            addOnPageChangeListener(sliderChangeListener)
        }
    }

    private val sliderChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (layouts.size.minus(1) == position) binding.button1.setText(R.string.start)
            else binding.button1.setText(R.string.next)
        }

        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        binding.dotsLayout.removeAllViews()
        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(requireActivity())
            dots!![i]?.text = Html.fromHtml("&#8226;")
            dots!![i]?.textSize = 35f
            dots!![i]?.setTextColor(resources.getColor(R.color.dots_grey))
            binding.dotsLayout.addView(dots!![i])
        }

        if (dots!!.isNotEmpty()) {
            dots!![currentPage]?.setTextColor(resources.getColor(R.color.purple))
        }
    }

    private fun interactions() {
        binding.button1.setOnClickListener {
            val current = getCurrentScreen(+1)
            if (current < layouts.size) {
                binding.slider.currentItem = current
            } else {
                prefs.setFirstTimeLaunch(PrefsSettings.NOT_AUTH)
                activityNavController().navigateSafely(R.id.action_global_authFlowFragment)
            }
        }
    }

    private fun getCurrentScreen(i: Int): Int = binding.slider.currentItem.plus(i)
    override fun subscribeToLiveData() {}
}