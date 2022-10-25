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
fun MainRoute(
    converterViewModel: ConverterViewModel = viewModel(),
    calculatorViewModel: CalculatorViewModel = viewModel(),
    explanationViewModel: ExplanationViewModel = viewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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

    BackHandler(bottomSheetState.targetValue != ModalBottomSheetValue.Hidden) {
        converterViewModel.hideExplanation()
    }

    MainScreen(
        converterUiState = converterViewModel.converterUiState.value,
        calculatorUiState = calculatorViewModel.calculatorUiState.value,
        explanationUiState = explanationViewModel.explanationUiState.value,
        bottomSheetState = bottomSheetState,
        showExplanation = { nsFrom, nsTo ->
            converterViewModel.showExplanation()
            explanationViewModel.acceptValues(nsFrom, nsTo)
        },
        onConverterNumberSystemChanged = converterViewModel::convertFrom,
        onConverterRadixChanged = converterViewModel::updateCustomRadix,
        onCalculatorNumberSystemChanged = calculatorViewModel::convertFrom,
        onCalculatorRadixChanged = calculatorViewModel::updateRadix,
        onCalculatorArithmeticChange = calculatorViewModel::selectArithmetic,
        onClearClicked = converterViewModel::clear,
        onExplanationRadixChanged = explanationViewModel::updateRadix,
        onExplanationRadixSwapClicked = explanationViewModel::swapRadixes,
    )
}
