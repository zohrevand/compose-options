# Compose Options

A Jetpack Compose library for building **interactive option selectors** with a draggable thumb and track.  
It’s useful when you want a slider-like UI but with **discrete labeled options** instead of continuous values.  

## Demo
<img src="https://raw.githubusercontent.com/zohrevand/compose-options/refs/heads/main/images/options_sample.gif" width="300"/>

## Features

- Select options via drag or tap  
- Fully customizable colors, shapes, and typography  
- Compose-first, built with idiomatic patterns (`rememberOptionsRowState`)  
- State-driven API for clean architecture  
- Easy to integrate into existing Compose layouts  

## Usage

Here’s a quick example of how to use the `OptionsRow` composable in your project.

### Step 1: Define your options

Create a list of **`Option`** data classes, each containing the text for the track and the thumb.

```kotlin
val options = listOf(
    Option(trackText = "Small"),
    Option(trackText = "Medium"),
    Option(trackText = "Large")
)
```

### Step 2: Create and remember the state

Use **`rememberOptionsRowState`** to manage the selected option's state. This function returns an `OptionsRowState` object that holds the current selection and handles changes.

```kotlin
val optionsState = rememberOptionsRowState(
    options = options,
    selectedOptionIndex = 1, // Start with "Medium" selected
    onOptionIndexChange = { newIndex ->
        // Handle option change
    }
)
```

### Step 3: Add the composable to your layout

Pass the **`optionsState`** to the **`OptionsRow`** composable.

```kotlin
@Composable
fun MyScreen() {
    val options = listOf(
        Option(trackText = "Option 1", thumbText = "O1"),
        Option(trackText = "Option 2", thumbText = "O2"),
        Option(trackText = "Option 3", thumbText = "O3")
    )

    val optionsState = rememberOptionsRowState(options)

    OptionsRow(state = optionsState)
}
```

## Customization

You can customize the appearance of the `OptionsRow` using its parameters.

### `OptionsRow` Parameters

  * `state`: The `OptionsRowState` that controls the component.
  * `modifier`: Standard `Modifier` for layout customization.
  * `trackTextStyle`: `TextStyle` for the text on the track.
  * `thumbTextStyle`: `TextStyle` for the text on the draggable thumb.
  * `colors`: The color scheme for the row, defined by `OptionsColors`.
  * `activeTrackVisible`: Set to `true` to show the active track color.
  * `inactiveTrackVisible`: Set to `true` to show the inactive track color.
  * `draggable`: Set to `false` to disable dragging the thumb.
  * `containerHeight`: The height of the entire row.
  * `shape`: The shape of the row container.
  * `contentPadding`: Padding around the contents of the row.

### Custom Colors

Use the **`OptionsDefaults.colors`** to create a custom color scheme.

```kotlin
OptionsRow(
    state = optionsState,
    colors = OptionsDefaults.colors(
        containerColor = Color.LightGray,
        thumbColor = Color.Blue,
        trackTextColor = Color.DarkGray,
        thumbTextColor = Color.White
    )
)
```

## License

```
Copyright 2024 Hasan Zohrevand

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
