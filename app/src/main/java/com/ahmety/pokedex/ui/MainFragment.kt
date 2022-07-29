package com.ahmety.pokedex.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ahmety.pokedex.MainActivity
import com.ahmety.pokedex.R
import com.ahmety.pokedex.databinding.FragmentMainBinding
import com.ahmety.pokedex.model.Pokemon
import com.ahmety.pokedex.ui.adapter.MainAdapter
import com.ahmety.pokedex.ui.adapter.MainLoadStateAdapter
import com.ahmety.pokedex.util.hasInternetConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var mainAdapter: MainAdapter? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBarVisibility()
        setupAdapter()
        setupClickListener()

    }

    override fun onResume() {
        super.onResume()
        setUI()
        setupList()
    }

    private fun initActionBarVisibility() {
        if (checkDrawOverlayPermission()) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
            (activity as MainActivity).enableDisableDrawer(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            (activity as MainActivity).enableDisableDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setUI() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = getString(R.string.pokemons)
        binding.btnGivePermission.isVisible = !checkDrawOverlayPermission()
        binding.recyclerViewMain.isVisible = checkDrawOverlayPermission()
        binding.groupError.isVisible = if (checkDrawOverlayPermission()) { !hasInternetConnection(requireContext())} else{ false}
    }

    private fun setupAdapter() {
        mainAdapter = MainAdapter(::onClickPokemon)
        binding.recyclerViewMain.adapter = mainAdapter?.withLoadStateHeaderAndFooter(
            header = MainLoadStateAdapter { mainAdapter?.retry()},
            footer = MainLoadStateAdapter { mainAdapter?.retry()}
        )

        binding.recyclerViewMain.apply {
            setHasFixedSize(true)
        }
    }

    private fun setupList() {
        if (checkDrawOverlayPermission()) {
            if (hasInternetConnection(requireContext())){
                lifecycleScope.launch {
                    homeViewModel.listData.collectLatest { pagedData ->
                        mainAdapter?.submitData(pagedData)
                    }
                }
            }
        }

    }

    private fun onClickPokemon(item: Pokemon) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(item.name)
        findNavController().apply {
            currentDestination?.getAction(action.actionId)?.run {
                navigate(action)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
        }
    }

    private fun setupClickListener() {
        binding.apply {
            btnGivePermission.setOnClickListener{
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${requireActivity().packageName}")
                )
                resultLauncher.launch(intent)
            }


            btnTryAgain.setOnClickListener {
                setupList()
            }
        }
    }

    private fun checkDrawOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return Settings.canDrawOverlays(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainAdapter = null
    }

}