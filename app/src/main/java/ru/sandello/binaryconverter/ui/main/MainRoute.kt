package ru.sandello.binaryconverter.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.explanation.ExplanationViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainRoute(
    converterViewModel: ConverterViewModel = viewModel(),
    calculatorViewModel: CalculatorViewModel = viewModel(),
    explanationViewModel: ExplanationViewModel = viewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

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

    MainScreen(
        converterUiState = converterViewModel.converterUiState,
        calculatorUiState = calculatorViewModel.calculatorUiState,
        explanationUiState = explanationViewModel.explanationUiState,
        bottomSheetState = bottomSheetState,
        showExplanation = { nsFrom, nsTo ->
            converterViewModel.showExplanation()
            explanationViewModel.acceptValues(nsFrom, nsTo)
        },
        onConverterNumberSystemChanged = converterViewModel::convertFrom,
        onConverterRadixChanged = converterViewModel::updateCustomRadix,
        onConverterClearClicked = converterViewModel::clear,
        onCalculatorNumberSystemChanged = calculatorViewModel::convertFrom,
        onCalculatorRadixChanged = calculatorViewModel::updateRadix,
        onCalculatorArithmeticChange = calculatorViewModel::selectArithmetic,
        onCalculatorClearClicked = calculatorViewModel::clear,
        onExplanationRadixChanged = explanationViewModel::updateRadix,
    )
}
