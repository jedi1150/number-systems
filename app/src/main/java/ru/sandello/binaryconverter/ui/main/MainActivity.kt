package ru.sandello.binaryconverter.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.*
import com.google.accompanist.insets.ui.TopAppBar
import com.google.android.gms.ads.interstitial.InterstitialAd
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.databinding.ActivityMainBinding
import ru.sandello.binaryconverter.model.Screen
import ru.sandello.binaryconverter.ui.calculator.CalculatorScreen
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterScreen

class MainActivity : ComponentActivity() {
//    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
//    private val model: CalculatorViewModel by viewModels()

    private lateinit var ad: InterstitialAd

    @OptIn(ExperimentalMaterial3Api::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val items = listOf(
            Screen.Converter,
            Screen.Calculator,
        )

        setContent {
            val navController = rememberNavController()

            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.navigationBarsWithImePadding(),
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                    label = { Text(stringResource(screen.resourceId)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    },
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            SmallFloatingActionButton(
                                onClick = { /*TODO*/ },
                            ) {
                                Icon(painter = painterResource(R.drawable.close), contentDescription = null)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LargeFloatingActionButton(
                                onClick = { /*TODO*/ },
                            ) {
                                Icon(painter = painterResource(R.drawable.explanation), contentDescription = null)
                            }
                        }
                    },
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Converter.route,
                        modifier = Modifier.padding(contentPadding),
                    ) {
                        composable(Screen.Converter.route) { ConverterScreen() }
                        composable(Screen.Calculator.route) { CalculatorScreen() }
//                        composable("settings") {  }
                    }
                }
            }
        }

//        Shared.resourcesHelper = ResourcesHelper(applicationContext)
//        Shared.preferencesHelper = PreferencesHelper(applicationContext)
//        setNightMode()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.mainContainer.setOnApplyWindowInsetsListener { _, insets ->
//            binding.bottomNavigation.updatePadding(bottom = insets.systemWindowInsetBottom, right = insets.systemWindowInsetRight, left = insets.systemWindowInsetLeft)
//            insets
//        }

//        val themePref = getSharedPreferences("decLength", Context.MODE_PRIVATE)
//        Shared.FRACTION_COUNT = themePref.getInt("decLength", 12)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        setupBottomNavMenu(navController)
        /*       navController.addOnDestinationChangedListener { _, destination, _ ->
                   if (destination.label.toString() == "Settings") {
                       binding.clearFab.hide()
                       binding.explanationFab.hide()
                       val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                       inputMethodManager.hideSoftInputFromWindow(binding.mainContainer.windowToken, 0)
                   }
                   if (destination.label.toString() == "Calculator") {
                       binding.explanationFab.hide()
                   }
               }*/

//        binding.bottomNavigation.setOnNavigationItemReselectedListener { }

        /*val adDialog = AlertDialog.Builder(this)
        adDialog.setTitle(getString(R.string.ad))
        adDialog.setMessage(getString(R.string.adMessage))
        adDialog.setCancelable(false)
        adDialog.setPositiveButton(getString(R.string.adWatch)) { _, _ ->
            ad.show(this)
        }
        adDialog.setNeutralButton(android.R.string.cancel) { _, _ ->
        }

        val adRequest = AdRequest.Builder().build()

        MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(listOf("E391F97E4B9B64A011FDEE11C58AEECF")).build())
        InterstitialAd.load(this, "ca-app-pub-3591700046184217/4193658783", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("adMob", adError.message)
                ad
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("adMob", "Ad was loaded.")
                ad = interstitialAd
                GlobalScope.launch(Dispatchers.Main) {
                    delay(DurationUnit.SECONDS.toMillis(30))
                    adDialog.show()
                }
            }
        })*/
    }

/*    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNavigation.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }*/

//    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()


//    @SuppressLint("InlinedApi")
//    private fun setNightMode() {
////        val isNightMode = this.resources.configuration.uiMode
////                .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
////        window.decorView.systemUiVisibility = (
////                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                )
////        if (!isNightMode) {
////            window.decorView.systemUiVisibility = (
////                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
////                            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
////                            xor View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                    )
////        }
////        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
////        if (sharedPreferences.getString("theme", "2") == "1") {
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
////            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
////        }
////        if (sharedPreferences.getString("theme", "2") == "0") {
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
////            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
////        }
////
////        if (sharedPreferences.getString("theme", "2") == "2") {
////            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
////                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
////                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
////            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
////                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
////            }
////        }
//    }

}
