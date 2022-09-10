package ru.sandello.binaryconverter.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.explanation.ExplanationViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainRoute() {
    val converterViewModel: ConverterViewModel = viewModel()
    val calculatorViewModel: CalculatorViewModel = viewModel()
    val explanationViewModel: ExplanationViewModel = viewModel()

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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

    MainScreen(
        converterViewModel = converterViewModel,
        calculatorViewModel = calculatorViewModel,
        explanationViewModel = explanationViewModel,
        bottomSheetState = bottomSheetState,
    )
}
