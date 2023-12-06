package ru.garshishka.lilmapapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.garshishka.lilmapapp.R
import ru.garshishka.lilmapapp.databinding.FragmentCategoryTwoBinding
import ru.garshishka.lilmapapp.fragment.PositionCardFragment.Companion.cardNum

class CategoryTwoFragment : Fragment() {
    private val binding: FragmentCategoryTwoBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }
            posThreeButton.setOnClickListener {
                findNavController().navigate(R.id.action_categoryTwoFragment_to_positionCardFragment,
                    Bundle().apply
                    { cardNum = 3 })
            }
            posFourButton.setOnClickListener {
                findNavController().navigate(R.id.action_categoryTwoFragment_to_positionCardFragment,
                    Bundle().apply
                    { cardNum = 4 })
            }
        }
        return binding.root
    }
}