package ru.sandello.binaryconverter.ui.main

//import androidx.compose.material.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.Screen
import ru.sandello.binaryconverter.ui.calculator.CalculatorScreen
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.components.Explanation
import ru.sandello.binaryconverter.ui.converter.ConverterScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.ShapesTop

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val converterViewModel: ConverterViewModel by viewModels()
    private val calculatorViewModel: CalculatorViewModel by viewModels()

    private lateinit var ad: InterstitialAd

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val screens = listOf(
            Screen.Converter,
            Screen.Calculator,
        )

        setContent {
            val keyboardController = LocalSoftwareKeyboardController.current

            NumberSystemsTheme {
                val scope = rememberCoroutineScope()

                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                val navController = rememberNavController()

                val imeIsVisible = WindowInsets.ime.asPaddingValues().calculateBottomPadding() > 0.dp
                val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

                SideEffect {
                    systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = useDarkIcons)
                }

                LaunchedEffect(converterViewModel.showExplanation.value) {
                    if (converterViewModel.showExplanation.value) {
                        scope.launch {
                            keyboardController?.hide()
                            bottomSheetState.show()
                            viewModel.acceptValues(converterViewModel.numberSystem10.value, converterViewModel.numberSystem2.value)
                        }
                    } else {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    }
                }

                LaunchedEffect(bottomSheetState.isVisible) {
                    if (!bottomSheetState.isVisible) {
                        converterViewModel.hideExplanation()
                    }
                }

                ModalBottomSheetLayout(
                    sheetState = bottomSheetState,
                    sheetShape = ShapesTop.extraLarge,
                    sheetContent = {
                        Surface(modifier = Modifier.imePadding()) {
                            Explanation(viewModel.explanationState)
                        }
                    },
                ) {
                    Scaffold(
                        bottomBar = {
                            Surface(
                                tonalElevation = 3.dp
                            ) {
                                NavigationBar(
                                    modifier = Modifier.navigationBarsPadding(),
                                    tonalElevation = 0.dp,
                                ) {
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    val currentDestination = navBackStackEntry?.destination
                                    screens.forEach { screen ->
                                        NavigationBarItem(
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
                                            icon = {
                                                Icon(
                                                    painterResource(screen.iconId),
                                                    contentDescription = null,
                                                )
                                            },
                                            label = { Text(stringResource(screen.resourceId)) },
                                        )
                                    }
                                }
                            }
                        },
                    ) { contentPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Converter.route,
                        ) {
                            composable(Screen.Converter.route) { ConverterScreen(converterViewModel, contentPadding) }
                            composable(Screen.Calculator.route) { CalculatorScreen(calculatorViewModel, contentPadding) }
//                        composable("settings") {  }
                        }

                        // FAB clear fields
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    bottom = maxOf(
                                        WindowInsets.ime
                                            .asPaddingValues()
                                            .calculateBottomPadding(),
                                        WindowInsets.navigationBars
                                            .asPaddingValues()
                                            .calculateBottomPadding(),
                                        contentPadding.calculateBottomPadding(),
                                    )
                                ),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            val fabVisible by derivedStateOf {
                                return@derivedStateOf when (currentDestination?.route) {
                                    Screen.Converter.route -> converterViewModel.hasData.value
                                    Screen.Calculator.route -> calculatorViewModel.hasData.value
                                    else -> false
                                }
                            }

                            ConstraintLayout {
                                val (clearFab, explanationFab) = createRefs()

                                AnimatedVisibility(
                                    visible = fabVisible,
                                    modifier = Modifier.constrainAs(clearFab) {
                                        end.linkTo(parent.end, margin = 16.dp)
                                        bottom.linkTo(explanationFab.top, margin = 16.dp, goneMargin = 16.dp)
                                    },
                                    enter = scaleIn(),
                                    exit = scaleOut(),
                                ) {
                                    SmallFloatingActionButton(
                                        onClick = {
                                            if (navController.currentDestination?.route == Screen.Converter.route) {
                                                converterViewModel.clear()
                                            }
                                            if (navController.currentDestination?.route == Screen.Calculator.route) {
                                                calculatorViewModel.clear()
                                            }
                                        },
                                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                                    ) {
                                        Icon(painter = painterResource(R.drawable.close), contentDescription = null)
                                    }
                                }

                                AnimatedVisibility(
                                    visible = fabVisible,
                                    modifier = Modifier.constrainAs(explanationFab) {
                                        end.linkTo(parent.end, margin = 16.dp)
                                        bottom.linkTo(parent.bottom, margin = 16.dp)
                                    },
                                    enter = scaleIn(),
                                    exit = scaleOut(),
                                ) {
                                    ExtendedFloatingActionButton(
                                        text = { Text(text = stringResource(id = R.string.explanation)) },
                                        icon = { Icon(painter = painterResource(R.drawable.explanation), contentDescription = stringResource(id = R.string.explanation)) },
                                        onClick = {
                                            if (navController.currentDestination?.route == Screen.Converter.route) {
                                                converterViewModel.showExplanation()
                                            }
                                            if (navController.currentDestination?.route == Screen.Calculator.route) {
//                                                calculatorViewModel.clear()
                                            }
                                        },
                                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                                    )
                                }

                            }
                        }
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
