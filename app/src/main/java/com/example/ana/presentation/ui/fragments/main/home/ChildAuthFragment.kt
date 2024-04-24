package com.example.ana.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ana.databinding.FragmentChildAuthBinding
import com.teenteen.teencash.presentation.base.BaseFragment

class ChildAuthFragment : BaseFragment<FragmentChildAuthBinding>() {

    private var state = Screen.AGE

    enum class Screen {
        AGE, NAME, DONE
    }

    override fun setupViews() {
        val directions = ChildAuthFragmentDirections.actionChildAuthFragmentToHomeFragment()
        binding.back.setOnClickListener { findNavController().navigate(directions) }
        binding.btnNext.setOnClickListener {
            when(state) {
                Screen.AGE -> {
                    if (binding.input.text.toString().isNotBlank()) {
                        state = Screen.NAME
                        prefs.saveChildAge(binding.input.text.toString())
                        binding.textField.hint = "Child's name"
                        binding.title.text = "What is your child’s name?"
                        binding.input.setText("")
                    }
                }
                Screen.NAME -> {
                    if (binding.input.text.toString().isNotBlank()) {
                        state = Screen.DONE
                        prefs.saveChildName(binding.input.text.toString())
                        binding.title.text = "Thank you for provided information"
                        binding.textField.visibility = View.GONE
                        binding.image.visibility = View.VISIBLE
                        binding.btnNext.text = "Go back"
                    }
                }

                Screen.DONE -> { findNavController().navigate(directions) }
            }
        }
    }

    override fun subscribeToLiveData() {}

    override fun attachBinding(
        list: MutableList<FragmentChildAuthBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentChildAuthBinding.inflate(layoutInflater, container, attachToRoot))
    }

}