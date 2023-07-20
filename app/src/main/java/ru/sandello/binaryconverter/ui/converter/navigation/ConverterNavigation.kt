/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.sandello.binaryconverter.ui.converter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.converter.ConverterRoute
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel

const val converterRoute = "converter"

fun NavController.navigateToConverter(navOptions: NavOptions? = null) {
    this.navigate(converterRoute, navOptions)
}

fun NavGraphBuilder.converterScreen(viewModel: ConverterViewModel) {
    composable(
        route = converterRoute,
    ) {
        ConverterRoute(viewModel = viewModel)
    }
}
