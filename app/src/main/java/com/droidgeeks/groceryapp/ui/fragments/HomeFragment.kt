package com.droidgeeks.groceryapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidgeeks.groceryapp.R
import com.droidgeeks.groceryapp.databinding.FragmentHomeBinding
import com.droidgeeks.groceryapp.interfaces.GenericAdapterCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), GenericAdapterCallback {

    lateinit var navGraph: NavController

    lateinit var binding: FragmentHomeBinding

    lateinit var lastFragment: Fragment

    lateinit var pendingFragment: PendingFragment

    lateinit var allGroceriesFragment: AllGroceriesFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(view)
    }

    private fun initData(view: View) {
        navGraph = Navigation.findNavController(view)

        binding.callBack = this@HomeFragment

        binding.bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.bottomNavView.selectedItemId = R.id.navigation_home
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    if (!::pendingFragment.isInitialized) {
                        pendingFragment = PendingFragment(this@HomeFragment)
                    }

                    lastFragment = pendingFragment

                    launchNewFragment(pendingFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_all -> {
                    if (!::allGroceriesFragment.isInitialized) {
                        allGroceriesFragment = AllGroceriesFragment(this@HomeFragment)
                    }

                    lastFragment = allGroceriesFragment

                    launchNewFragment(allGroceriesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> return@OnNavigationItemSelectedListener false
            }
        }

    private fun launchNewFragment(fragment: Fragment) {
        try {
            val backStateName = fragment.javaClass.name
            val fragmentManager: FragmentManager = childFragmentManager
            val fragmentPopped =
                fragmentManager.popBackStackImmediate(backStateName, 0)
            if (!fragmentPopped) {
                val fragmentTransaction =
                    fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, fragment, backStateName)
                fragmentTransaction.addToBackStack(fragment.javaClass.name)
                fragmentTransaction.commit()
            }
        } catch (e: Exception) {
            Log.d("dashboard", e.message!!)
        }
    }

    override fun <T> getClickedObject() {
        val action = HomeFragmentDirections.actionHomeFragmentToAddGroceryFragment(null)
        navGraph.navigate(action)
    }

    override fun setBottomNavVisibility(visibility: Int) {
        binding.buttonAddGrocery.visibility = visibility
        binding.bottomNavView.visibility = visibility
    }

    override fun onResume() {
        super.onResume()

        if (::lastFragment.isInitialized) {
            launchNewFragment(lastFragment)
        }
    }

}