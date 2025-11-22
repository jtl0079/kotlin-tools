package com.myorg.kotlintools.composable


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale


data class RawData(val name: String, val value: Double)


/**
 * 绘制一个带有动画效果的多段圆形百分比图（饼形环图），
 * 并在下方显示每个数据项的名称、颜色及数值比例。
 *
 * ## 功能说明
 * - 根据传入的数据列表 `dataInput` 自动计算各项比例并生成对应的彩色分段。
 * - 使用 `animateFloatAsState` 实现从 0 到 100% 的绘制动画。
 * - 图形部分采用圆环（非填充饼图），可通过 `strokeWidth` 调整厚度。
 * - 支持自定义图形中心显示的文字内容 `wordsInsidePanel`。
 * - 下方使用 `LazyColumn` 展示每项的颜色标识、名称、数值与百分比。
 *
 * ## 参数说明
 * @param modifier 外部传入的 Compose Modifier，用于控制整体的布局行为。
 * @param dataInput 一个 `List<RawData>`，包含每段的数据名称与数值。
 * @param panelSize 饼图区域的大小（宽高相同）。默认 200.dp。
 * @param strokeWidth 圆环的厚度（Stroke 宽度）。默认 25.dp。
 * @param wordsInsidePanel 显示在圆环中央的文字，例如总百分比或说明文字。
 *
 * ## RawData 数据结构示例
 * data class RawData(
 *     val name: String,
 *     val value: Double
 * )
 *
 * ## 使用场景
 * - 分类占比图（如统计支出、时间分布、用户结构等）
 * - 仪表盘 Dashboard
 * - 数据分析结果可视化
 *
 * ## 注意事项
 * - 建议传入不可变 List，而非 MutableList，以避免不必要的重组（Recomposition）。
 * - 当 `dataInput` 变化（数量变化）时，颜色将重新生成。
 */

@Composable
fun PieChart(
    modifier: Modifier,
    dataInput: List<RawData>,    // 传入一组数据
    panelSize: Dp = 200.dp,
    strokeWidth: Dp = 25.dp,
    wordsInsidePanel: String = "Words",
) {
    // 计算每个值的比例, 总和
    val totalValue: Double = dataInput.sumOf { it.value }
    val eachValueRatio: List<Double> = dataInput.map { it.value / totalValue }
    val colors: List<Color> = remember(dataInput.size) { generateDistinctColors(dataInput.size) }

    // 动画：从 0 → 1
    val animatedProgress = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 720)
    )

    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // ================================================
        //                   圆形饼图
        // ================================================
        Box(
            modifier = Modifier
                .size(panelSize),
            contentAlignment = Alignment.Center
        )
        {
            Canvas(modifier = Modifier.size(panelSize)) {
                val strokePx = strokeWidth.toPx()
                val strokeOuterPx = strokePx + 8

                val stroke: Stroke = Stroke(width = strokePx, cap = StrokeCap.Butt)
                val strokeBorder: Stroke = Stroke(width = strokeOuterPx, cap = StrokeCap.Butt)
                val radius = panelSize.toPx() / 2 - strokeOuterPx / 2

                val arcTopLeft = Offset(
                    x = size.width / 2 - radius,
                    y = size.height / 2 - radius
                )
                val arcSize = Size(radius * 2, radius * 2)
                var startAngle = -90f   // 从上方开始

                // ① 底圆环 --------------------------------------------------------
                drawArc(
                    color = Color.Black,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = strokeBorder,
                    topLeft = arcTopLeft,
                    size = arcSize
                )

                // ② 多段进度环 -----------------------------------------------------
                dataInput.forEachIndexed { index, item ->
                    val percentage = (item.value / totalValue).toFloat()
                    val sweepAngle = percentage * 360f * animatedProgress.value

                    drawArc(
                        color = colors[index],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = stroke,
                        topLeft = arcTopLeft,
                        size = arcSize
                    )
                    startAngle += sweepAngle
                }
            }
            // 文本显示
            Text(
                text = wordsInsidePanel,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        // ================================================
        //                    数据列表
        // ================================================
        // 每行显示 [颜色块代表][name][" : "][data]
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            itemsIndexed(dataInput) { index, item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(color = colors[index])
                    )
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = " : ")
                    Text(
                        text = "${item.value} (${String.format(Locale.US, "%.2f%%", eachValueRatio[index] * 100)})",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun qqq(
    modifier: Modifier,
    mutableList: MutableList<RawData>
) {

    // 每行显示 [String][" : "][Double]
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(mutableList) { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = " : ",
                    modifier = Modifier.weight(0.8f)
                )
                Text(
                    text = item.value.toString(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ExpandableSection() {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // 标题 + 箭头
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "More Info",
                modifier = Modifier.weight(1f)
            )

            // 旋转动画
            val rotation by animateFloatAsState(
                targetValue = if (expanded) 180f else 0f
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }

        // 折叠 / 展开区域
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("This is the expanded content.")
                Text("You can put anything here.")
            }
        }
    }
}
