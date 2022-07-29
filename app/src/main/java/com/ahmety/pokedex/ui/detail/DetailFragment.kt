package com.ahmety.pokedex.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ahmety.pokedex.R
import com.ahmety.pokedex.databinding.FragmentDetailBinding
import com.ahmety.pokedex.service.OverlayControlService
import com.ahmety.pokedex.ui.detail.DetailViewModel.Companion.ACTION_STOP_FOREGROUND
import com.ahmety.pokedex.util.Resource
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val detailViewModel: DetailViewModel by viewModels() // define view model variable
    private var _binding: FragmentDetailBinding? = null // define view binding variable
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs() // define arguments variable to get values from previous fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel.getPokemonDetail(args.pokemonName) // call request from view model
        setUI()
        observeUI()
        setOnClickListener()
    }

    private fun setUI() {
        // set action bar title
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.pokemon_details)
    }

    // observe list and set data from list
    private fun observeUI() {
        detailViewModel.pokemonList.observe(viewLifecycleOwner) {
            when (it) {
                // run when response is success state
                is Resource.Success -> {
                    binding.apply {
                        progressBar.isVisible = false
                        btnShowOverlay.isVisible = true
                        txtError.isVisible = false
                        val data = it.data
                        Glide.with(pokemonPic).load(data?.sprites?.front_default)
                            .error(R.drawable.ic_baseline_error_outline_24)
                            .into(pokemonPic)
                        pokemonName.text = getString(R.string.pokemon_title, data?.name)
                        pokemonHeight.text = getString(R.string.pokemon_height, data?.height)
                        pokemonWeight.text = getString(R.string.pokemon_weight, data?.weight)
                        btnShowOverlay.text = getString(R.string.text_btn_show_overlay, data?.name)
                    }
                }
                // run when response is error state
                is Resource.Error -> {
                    binding.apply {
                        progressBar.isVisible = false
                        btnShowOverlay.isVisible = false
                        txtError.isVisible = true
                        it.message?.let { message ->
                            txtError.text = message
                        }
                    }
                }
                // run when response is loading state
                is Resource.Loading -> {
                    binding.apply {
                        progressBar.isVisible = true
                        btnShowOverlay.isVisible = false
                        txtError.isVisible = false
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.apply {
            btnShowOverlay.setOnClickListener {
                val intent = Intent(requireContext(), OverlayControlService::class.java)
                intent.putExtra("pokemonName",detailViewModel.pokemonList.value?.data?.name)
                intent.putExtra("frontPhoto", detailViewModel.pokemonList.value?.data?.sprites?.front_default)
                intent.putExtra("behindPhoto", detailViewModel.pokemonList.value?.data?.sprites?.back_default)
                activity?.startService(intent)
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}