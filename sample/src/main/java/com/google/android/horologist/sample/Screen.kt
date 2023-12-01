/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.horologist.sample

sealed class Screen(
    val route: String,
) {
    object Menu : Screen("menu")
    object FillMaxRectangle : Screen("fmr")
    object ScrollAway : Screen("scrollAway")
    object ScrollAwaySLC : Screen("scrollAwaySLC")
    object ScrollAwayColumn : Screen("scrollAwayColumn")
    object Volume : Screen("volume")
    object DatePicker : Screen("datePicker")
    object TimePicker : Screen("timePicker")
    object TimeWithSecondsPicker : Screen("timeWithSecondsPicker")
    object TimeWithoutSecondsPicker : Screen("timeWithoutSecondsPicker")
    object Network : Screen("network")


    object MaterialButtons : Screen("materialButtons")
    object MaterialChips : Screen("materialChips")
    object MaterialChipIconWithProgress : Screen("materialChipIconWithProgress")
    object MaterialCompactChips : Screen("materialCompactChips")
    object MaterialConfirmation : Screen("materialConfirmationScreen")
    object MaterialIcon : Screen("materialIcon")
    object MaterialOutlinedChip : Screen("materialOutlinedChip")
    object MaterialOutlinedCompactChip : Screen("materialOutlinedCompactChip")
    object MaterialSplitToggleChip : Screen("materialSplitToggleChip")
    object MaterialStepper : Screen("materialStepper")
    object MaterialTitle : Screen("materialTitle")
    object MaterialToggleButton : Screen("materialToggleButton")

    object DataLayerNodes : Screen("nodes")

    object SectionedListMenuScreen : Screen("sectionedListMenuScreen")
    object SectionedListStatelessScreen : Screen("sectionedListStatelessScreen")
    object SectionedListStatefulScreen : Screen("sectionedListStatefulScreen")
    object SectionedListExpandableScreen : Screen("sectionedListExpandableScreen")

    object RotaryMenuScreen : Screen("rotaryMenuScreen")

    object RotaryScrollScreen : Screen("rotaryScrollScreen")
    object RotaryScrollReversedScreen : Screen("rotaryScrollReversedScreen")
    object RotaryScrollWithFlingScreen : Screen("rotaryScrollWithFlingScreen")
    object RotarySnapListScreen : Screen("rotarySnapListScreen")

    object Paging : Screen("paging")
    object PagingItem : Screen("pagingItem?id={id}")

    object PagerScreen : Screen("pagerScreen")
}
