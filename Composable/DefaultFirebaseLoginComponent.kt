package com.example.miniproject.kotlinTools.Composable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon


// ---------------------------
// UI State
// ---------------------------
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val errorMessage: String? = null,
    val userId: String? = null
)


// ---------------------------
// ViewModel
// ---------------------------
class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()  // 默认 FirebaseApp

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    /** 当 Email 改变时更新 UI 状态，并清除错误 */
    fun onEmailChanged(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                errorMessage = null       // 用户有输入则清除错误
            )
        }
    }

    /** 当 Password 改变时更新 UI 状态，并清除错误 */
    fun onPasswordChanged(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword,
                errorMessage = null      // 清除错误
            )
        }
    }

    /** 切换密码可见性 */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /** 执行 Firebase 登录 */
    fun login(
        errorMassageOfEmptyFieldsOfEmailAndPassword: String = "Email or Password can be null"
    ) {
        val email = _uiState.value.email
        val password = _uiState.value.password.trim()

        // 先检查是否为空
        if (email.isEmpty() || password.isEmpty()) {
            _uiState.update {
                it.copy(
                    errorMessage = errorMassageOfEmptyFieldsOfEmailAndPassword,
                    isLoading = false
                )
            }
            return
        }

        // 更新 loading
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userId = result.user?.uid,
                        errorMessage = null
                    )
                }
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
    }
}

/**
 * 一个基于 Firebase Authentication（Email + Password）的可复用登录组件，
 * 自动绑定默认 FirebaseApp，通过 ViewModel 管理输入状态、验证与登录流程。
 *
 * ## 功能说明
 * - 自动使用默认的 FirebaseAuth（FirebaseAuth.getInstance()）。
 * - 内置 Email 与 Password 输入框（均为 singleLine）。
 * - 密码输入框支持可见性切换（显示/隐藏密码）。
 * - 若 Email 或 Password 为空，会立即提示错误并阻止登录。
 * - 用户修改输入内容时会自动清除之前的错误提示。
 * - 登录过程中显示加载状态（Login 按钮禁用，文字显示 “Logging in...”）。
 * - 登录成功后自动触发 `onLoginSuccess(uid)` 回调，并弹出 Toast。
 * - 使用 StateFlow 管理 UI 状态，实现响应式自动更新。
 * - 完整 MVVM 架构：UI 无业务逻辑，适合复用。
 *
 * ## 参数说明
 * @param modifier 外部传入的 Compose Modifier，用于控制组件布局（宽度、内边距等）。
 * @param viewModel 可选，供依赖注入（DI）或测试时使用。默认自动创建 LoginViewModel。
 * @param onLoginSuccess 登录成功后的回调，会返回 Firebase User 的 UID。
 *
 * ## LoginViewModel 行为说明
 * - `onEmailChanged()`：更新 email 输入，并清空错误消息。
 * - `onPasswordChanged()`：更新 password 输入，并清空错误消息。
 * - `togglePasswordVisibility()`：切换 password 是否可见。
 * - `login()`：执行登录流程（含空值检查、loading 状态、错误处理）。
 *
 * ## 使用方式示例
 * FirebaseDefaultLoginComponent(
 *     onLoginSuccess = { uid ->
 *         // 登录成功后的操作，例如跳转到主界面
 *     }
 * )
 *
 * ## 使用场景
 * - 需要快速集成 Email+Password 登录的 App
 * - 使用 Firebase Authentication 作为用户系统的项目
 * - 希望复用一个通用、干净、无业务耦合的登录组件
 * - 想通过 MVVM 保持 UI 与业务逻辑分离的 Compose 架构
 *
 * ## 注意事项
 * - 组件使用默认 FirebaseApp，如需多 FirebaseApp 必须自定义 ViewModel。
 * - 若需扩展注册、忘记密码等功能，可在 ViewModel 中扩展方法。
 * - 推荐搭配 Navigation 使用，通过 onLoginSuccess 触发跳转。
 */

@Composable
fun FirebaseDefaultLoginComponent(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    // 监听登录成功
    LaunchedEffect(uiState.userId) {
        uiState.userId?.let { uid ->
            Toast.makeText(context, "Login success: $uid", Toast.LENGTH_SHORT).show()
            onLoginSuccess(uid)
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Email 输入栏
        TextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Password 输入栏 + 眼睛按钮
        TextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),

            trailingIcon = {
                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (uiState.isPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
        )

        // 错误显示
        uiState.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        // Login 按钮
        Button(
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.login() }
        ) {
            Text(if (uiState.isLoading) "Logging in..." else "Login")
        }
    }
}
