import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** AndroidAutostartPlugin */
class AndroidAutostartPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "android_autostart")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "customSetComponent" -> {
                val manufacturer = call.argument<String>("manufacturer")
                val pkg = call.argument<String>("pkg")
                val cls = call.argument<String>("cls")
                
                if (manufacturer != null && pkg != null && cls != null) {
                    customSetComponent(manufacturer, pkg, cls, result)
                } else {
                    result.error("INVALID_ARGUMENTS", "Missing required arguments", null)
                }
            }
            "navigateAutoStartSetting" -> {
                navigateAutoStartSetting(result)
            }
            "autoStartSettingIsAvailable" -> {
                autoStartSettingIsAvailable(result)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun customSetComponent(manufacturer: String, pkg: String, cls: String, @NonNull result: Result) {
        val systemManufacturer = android.os.Build.MANUFACTURER
        try {
            val intent = Intent()

            if (manufacturer.equals(systemManufacturer, ignoreCase = true)) {
                intent.component = ComponentName(pkg, cls)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            result.success(true)
        } catch (e: Exception) {
            result.success(false)
        }
    }

    private fun navigateAutoStartSetting(@NonNull result: Result) {
        val manufacturer = android.os.Build.MANUFACTURER
        try {
            val intent = Intent()
            
            when {
                "xiaomi".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                }
                "oppo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                }
                "vivo".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                }
                "Letv".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")
                }
                "Honor".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
                }
                "samsung".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")
                }
                "oneplus".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")
                }
                "nokia".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.evenwell.powersaving.g3", "com.evenwell.powersaving.g3.exception.PowerSaverExceptionActivity")
                }
                "asus".equals(manufacturer, ignoreCase = true) -> {
                    intent.component = ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivy")
                }
                else -> {
                    return
                }
            }
            
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            result.success(true)
        } catch (e: Exception) {
            result.success(false)
        }
    }

    private fun autoStartSettingIsAvailable(@NonNull result: Result) {
        val manufacturer = android.os.Build.MANUFACTURER
        val supportedManufacturers = listOf(
            "xiaomi", "oppo", "vivo", "Letv", "Honor", 
            "samsung", "oneplus", "nokia", "asus"
        )
        
        result.success(
            supportedManufacturers.any { 
                it.equals(manufacturer, ignoreCase = true) 
            }
        )
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
