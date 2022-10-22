package ru.sandello.binaryconverter.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.model.NumberSystem
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    converterViewModel: ConverterViewModel = viewModel(),
    calculatorViewModel: CalculatorViewModel = viewModel(),
    explanationViewModel: ExplanationViewModel = viewModel(),
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
    showExplanation: (NumberSystem, NumberSystem) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    val navController = rememberNavController()

    val screens = listOf(
        Screen.Converter,
        Screen.Calculator,
    )

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
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Divider(
                            modifier = Modifier
                                .size(width = 50.dp, height = 4.dp)
                                .clip(Shapes.small),
                            color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                        )
                    }
                    ExplanationScreen(
                        explanationState = explanationViewModel.explanationState.collectAsState(),
                        radixes = explanationViewModel.radixes,
                        updateRadix = explanationViewModel::updateRadix,
                        swapRadixes = explanationViewModel::swapRadixes,
                    )
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
                        modifier = Modifier
                            .padding(
                                start = WindowInsets.navigationBars
                                    .asPaddingValues()
                                    .calculateStartPadding(layoutDirection) + WindowInsets.displayCutout
                                    .asPaddingValues()
                                    .calculateStartPadding(layoutDirection),
                                end = WindowInsets.navigationBars
                                    .asPaddingValues()
                                    .calculateEndPadding(layoutDirection) + WindowInsets.displayCutout
                                    .asPaddingValues()
                                    .calculateEndPadding(layoutDirection),
                            )
                            .navigationBarsPadding(),
                        tonalElevation = 0.dp,
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        screens.forEach { screen ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = screen.iconId),
                                        contentDescription = null,
                                    )
                                },
                                label = { Text(text = stringResource(screen.resourceId)) },
                            )
                        }
                    }
                }
            },
        ) { contentPadding ->
            MainScreenContent(
                converterViewModel = converterViewModel,
                calculatorViewModel = calculatorViewModel,
                navController = navController,
                contentPadding = contentPadding,
                showExplanation = showExplanation,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreenContent(
    converterViewModel: ConverterViewModel = viewModel(),
    calculatorViewModel: CalculatorViewModel = viewModel(),
    contentPadding: PaddingValues,
    navController: NavHostController,
    showExplanation: (NumberSystem, NumberSystem) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    NavHost(
        navController = navController,
        startDestination = Screen.Converter.route,
    ) {
        composable(Screen.Converter.route) { ConverterScreen(converterViewModel, contentPadding) }
        composable(Screen.Calculator.route) { CalculatorScreen(calculatorViewModel, contentPadding) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .displayCutoutPadding()
            .padding(
                start = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection),
                bottom = maxOf(
                    WindowInsets.ime
                        .asPaddingValues()
                        .calculateBottomPadding(),
                    WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding(),
                    contentPadding.calculateBottomPadding(),
                ),
            ),
        contentAlignment = Alignment.BottomEnd,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val clearFabIsVisible by remember(currentDestination) {
            derivedStateOf {
                return@derivedStateOf when (currentDestination?.route) {
                    Screen.Converter.route -> converterViewModel.hasData.value
                    Screen.Calculator.route -> calculatorViewModel.hasData.value
                    else -> false
                }
            }
        }
        val explanationFabIsVisible by remember(currentDestination) {
            derivedStateOf {
                return@derivedStateOf when (currentDestination?.route) {
                    Screen.Converter.route -> converterViewModel.hasData.value
                    else -> false
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            AnimatedVisibility(
                visible = clearFabIsVisible,
                enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeIn() + expandIn(expandFrom = Alignment.TopStart),
                exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
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
                    modifier = Modifier.padding(vertical = 8.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                ) {
                    Icon(painter = painterResource(R.drawable.close), contentDescription = null)
                }
            }

            AnimatedVisibility(
                visible = explanationFabIsVisible,
                enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeIn() + expandIn(expandFrom = Alignment.TopStart),
                exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) +
                        fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
            ) {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(id = R.string.explanation)) },
                    icon = { Icon(painter = painterResource(R.drawable.explanation), contentDescription = stringResource(id = R.string.explanation)) },
                    onClick = {
                        if (navController.currentDestination?.route == Screen.Converter.route) {
                            showExplanation(converterViewModel.numberSystem10.value, converterViewModel.numberSystem2.value)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewMainScreen() {
    NumberSystemsTheme {
        Surface {
            MainScreen(
                bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
                showExplanation = { _, _ -> },
            )
        }
    }
}
