import 'package:flutter/material.dart';
import 'package:android_autostart/android_autostart.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  final _scaffoldMessengerKey = GlobalKey<ScaffoldMessengerState>();

  void _showSnackBar(bool isAvailable) {
    _scaffoldMessengerKey.currentState?.showSnackBar(
      SnackBar(content: Text("Auto start setting is available: $isAvailable")),
    );
  }

  void _checkIfAutoStartSettingIsAvailable() async {
    AndroidAutostart.autoStartSettingIsAvailable.then(_showSnackBar);
  }

  void _navigateToAutoStartSettingScreen() {
    AndroidAutostart.navigateAutoStartSetting();
  }

  void _openXiaomiDevicesAutoStartManagementScreen() {
    AndroidAutostart.customSetComponent(
      manufacturer: "xiaomi",
      pkg: "com.miui.securitycenter",
      cls: "com.miui.permcenter.autostart.AutoStartManagementActivity",
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      scaffoldMessengerKey: _scaffoldMessengerKey,
      home: Scaffold(
        appBar: AppBar(title: const Text('Enable AutoStart Example App')),
        body: Center(
          child: Column(
            children: [
              ElevatedButton(
                onPressed: _checkIfAutoStartSettingIsAvailable,
                child: const Text('Check if AutoStart is available'),
              ),
              ElevatedButton(
                onPressed: _navigateToAutoStartSettingScreen,
                child: const Text("Navigate AutoStart Setting"),
              ),
              ElevatedButton(
                onPressed: _openXiaomiDevicesAutoStartManagementScreen,
                child: const Text("Custom Set Component"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
