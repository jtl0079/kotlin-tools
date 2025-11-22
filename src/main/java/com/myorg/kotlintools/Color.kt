package com.myorg.kotlintools

import androidx.compose.ui.graphics.Color

fun generateDistinctColors(
    n: Int,
    saturation: Float = 0.7f,
    lightness: Float = 0.55f
): List<Color> {
    return List(n) { index ->
        val hue = (index * 360f / n) % 360f  // 每个颜色的色相均匀分布
        Color.hsl(
            hue = hue,
            saturation = saturation, // 色彩饱和度
            lightness = lightness  // 亮度
        )
    }
}