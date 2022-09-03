package ru.sandello.binaryconverter.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.Screen
import ru.sandello.binaryconverter.ui.calculator.CalculatorScreen
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.explanation.ExplanationScreen
import ru.sandello.binaryconverter.ui.explanation.ExplanationViewModel
import ru.sandello.binaryconverter.ui.theme.NumberSystemsTheme
import ru.sandello.binaryconverter.ui.theme.Shapes
import ru.sandello.binaryconverter.ui.theme.ShapesTop

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val converterViewModel: ConverterViewModel = viewModel()
    val calculatorViewModel: CalculatorViewModel = viewModel()
    val explanationViewModel: ExplanationViewModel = viewModel()

    val screens = listOf(
        Screen.Converter,
        Screen.Calculator,
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    LaunchedEffect(converterViewModel.showExplanation.value) {
        if (converterViewModel.showExplanation.value) {
            scope.launch {
                explanationViewModel.acceptValues(converterViewModel.numberSystem10.value, converterViewModel.numberSystem2.value)
                keyboardController?.hide()
                bottomSheetState.show()
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

    BackHandler(bottomSheetState.targetValue != ModalBottomSheetValue.Hidden) {
        converterViewModel.hideExplanation()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            Surface(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .statusBarsPadding()
                    .imePadding(),
                shape = ShapesTop.extraLarge,
                tonalElevation = 16.dp,
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Divider(
                            modifier = Modifier
                                .size(width = 50.dp, height = 4.dp)
                                .clip(Shapes.small),
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                    ExplanationScreen(explanationViewModel)
                }
            }
        },
    ) {
        Scaffold(
            bottomBar = {
                Surface(
                    tonalElevation = NavigationBarDefaults.Elevation,
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
                            Icon(painter = painterResource(ru.sandello.binaryconverter.R.drawable.close), contentDescription = null)
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

@Preview
@Composable
fun PreviewMainScreen() {
    NumberSystemsTheme {
        Surface {
            MainScreen()
        }
    }
}
