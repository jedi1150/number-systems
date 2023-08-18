package ru.sandello.binaryconverter.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.R
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.explanation.ExplanationScreen
import ru.sandello.binaryconverter.ui.explanation.ExplanationViewModel
import ru.sandello.binaryconverter.ui.navigation.NumSysNavHost
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CALCULATOR
import ru.sandello.binaryconverter.ui.navigation.TopLevelDestination.CONVERTER

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NumberSystemsApp(
    windowSizeClass: WindowSizeClass,
    appState: NumSysAppState = rememberNumSysAppState(
        windowSizeClass = windowSizeClass,
    ),
) {
    val converterViewModel = hiltViewModel<ConverterViewModel>()
    val calculatorViewModel = hiltViewModel<CalculatorViewModel>()
    val explanationViewModel = hiltViewModel<ExplanationViewModel>()

    val converterUiState by converterViewModel.converterUiState.collectAsStateWithLifecycle()
    val calculatorUiState by calculatorViewModel.calculatorUiState.collectAsStateWithLifecycle()
    val explanationUiState by explanationViewModel.explanationUiState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope: CoroutineScope = rememberCoroutineScope()

    val skipPartiallyExpanded by remember { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    LaunchedEffect(converterViewModel.showExplanation.value) {
        if (converterViewModel.showExplanation.value) {
            scope.launch {
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

    BackHandler(bottomSheetState.targetValue != SheetValue.Hidden) {
        converterViewModel.hideExplanation()
    }

    Column {
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    NavigationBar {
                        appState.topLevelDestinations.forEach { destination ->
                            NavigationBarItem(
                                selected = appState.currentDestination.isTopLevelDestinationInHierarchy(destination),
                                onClick = {
                                    appState.navigateToTopLevelDestination(destination)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = destination.iconId),
                                        contentDescription = null,
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = destination.titleTextId),
                                    )
                                },
                            )
                        }
                    }
                }
            },
        ) { contentPadding ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                if (appState.shouldShowNavRail) {
                    NavigationRail(
                        modifier = Modifier,
                    ) {
                        appState.topLevelDestinations.forEach { destination ->
                            NavigationRailItem(
                                selected = appState.currentDestination.isTopLevelDestinationInHierarchy(destination),
                                onClick = {
                                    appState.navigateToTopLevelDestination(destination)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = destination.iconId),
                                        contentDescription = null,
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = destination.titleTextId),
                                    )
                                },
                            )
                        }
                    }
                }
                NumSysNavHost(
                    appState = appState,
                    converterViewModel = converterViewModel,
                    calculatorViewModel = calculatorViewModel,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.BottomEnd,
            ) {
                val currentTopLevelDestination = appState.currentTopLevelDestination
                val clearFabIsVisible by remember(currentTopLevelDestination, converterUiState, calculatorUiState) {
                    derivedStateOf {
                        return@derivedStateOf when (currentTopLevelDestination) {
                            CONVERTER -> converterUiState.hasData
                            CALCULATOR -> calculatorUiState.hasData
                            else -> false
                        }
                    }
                }
                val explanationFabIsVisible by remember(currentTopLevelDestination, converterUiState, calculatorUiState) {
                    derivedStateOf {
                        return@derivedStateOf when (currentTopLevelDestination) {
                            CONVERTER -> converterUiState.hasData
                            else -> false
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    AnimatedVisibility(
                        visible = clearFabIsVisible,
                        enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) + fadeIn() + expandIn(expandFrom = Alignment.TopStart),
                        exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) + fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                when (currentTopLevelDestination) {
                                    CONVERTER -> {
                                        converterViewModel.clear()
                                    }

                                    CALCULATOR -> {
                                        calculatorViewModel.clear()
                                    }

                                    else -> {}
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp),
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                        ) {
                            Icon(painter = painterResource(R.drawable.ic_close), contentDescription = null)
                        }
                    }

                    AnimatedVisibility(
                        visible = explanationFabIsVisible,
                        enter = scaleIn(transformOrigin = TransformOrigin(0f, 0f)) + fadeIn() + expandIn(expandFrom = Alignment.TopStart),
                        exit = scaleOut(transformOrigin = TransformOrigin(0f, 0f)) + fadeOut() + shrinkOut(shrinkTowards = Alignment.TopStart),
                    ) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(id = R.string.explanation)) },
                            icon = { Icon(painter = painterResource(R.drawable.ic_explanation), contentDescription = stringResource(id = R.string.explanation)) },
                            onClick = {
                                if (currentTopLevelDestination == CONVERTER) {
                                    converterViewModel.showExplanation()
                                    explanationViewModel.acceptValues(
                                        from = converterUiState.numberSystem10,
                                        to = converterUiState.numberSystem2,
                                    )
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp),
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                        )
                    }

                }
            }
        }
        if (bottomSheetState.targetValue != SheetValue.Hidden || bottomSheetState.currentValue != SheetValue.Hidden) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        bottomSheetState.hide()
                    }
                },
                modifier = Modifier,
                sheetState = bottomSheetState,
            ) {
                ExplanationScreen(
                    explanationUiState = explanationUiState,
                    onRadixChanged = explanationViewModel::updateRadix,
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) = this?.hierarchy?.any {
    it.route?.contains(destination.name, true) ?: false
} ?: false
