package com.myorg.kotlintools

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseApp

/**
 * 创建多个独立的 FirebaseApp 实例。
 *
 * Firebase 默认只允许单一实例（default app），并且单个 FirebaseApp 内只能维护一个
 * FirebaseAuth 的用户登录状态。这个函数的用途是创建多个命名的 FirebaseApp 实例，
 * 让每个实例能独立拥有自己的 Auth、Firestore、Database Session。
 *
 * 功能:
 * - 基于当前默认 FirebaseApp 的配置（FirebaseOptions）复制多个实例。
 * - 每个实例使用不同的 name（"0"、"1"、"2"...），互不冲突。
 * - 自动检查是否已经存在相同名称的 FirebaseApp，如已存在则直接复用。
 * - 若初始化失败会抛出 IllegalStateException，以便开发者即时发现问题。
 *
 * 应用场景:
 * - 需要在同一设备同时登录多个 Firebase 用户（多账号并行）。
 * - 做多用户测试（Stress test / Online user simulation）。
 * - 多会话架构（Multi-session Firebase clients）。
 *
 * @param context Android Context
 * @param n 创建的 FirebaseApp 数量
 * @return Map<String, FirebaseApp>，键为 app 名称，值为对应实例
 */

fun createMultipleFirebaseApp(
    context: Context ,
    n: Int
): Map<String, FirebaseApp> {
    val originalOptions = FirebaseApp.getInstance().options
    val result = mutableMapOf<String, FirebaseApp>()

    val existingApps = FirebaseApp.getApps(context)

    for (i in 0 until n) {
        val appName = "$i"

        // 防止重复创建
        val existing = existingApps.find { it.name == appName }
        if (existing != null) {
            result[appName] = existing
            continue
        }

        val app = FirebaseApp.initializeApp(context, originalOptions, appName)
            ?: throw IllegalStateException("Failed to initialize FirebaseApp: $appName")

        result[appName] = app
    }

    return result
}