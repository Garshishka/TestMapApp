package ru.garshishka.lilmapapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.garshishka.lilmapapp.IntArg
import ru.garshishka.lilmapapp.cards
import ru.garshishka.lilmapapp.databinding.FragmentCardBinding

class PositionCardFragment: Fragment() {
    private val binding: FragmentCardBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cardNumber = arguments?.cardNum
        cardNumber?.let {
            val card = cards[cardNumber-1]
            binding.apply {
                backButton.setOnClickListener {
                    findNavController().navigateUp()
                }
                cardName.text = card.name
                description.text = card.description
                image.setImageResource(card.pic)
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.cardNum by IntArg
    }
}