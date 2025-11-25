# 📸 react-native-torch-rcl

**Modernized Torch / Flashlight API for React Native (Android + iOS)**  
A fully updated, maintained, and CameraX-powered version of the abandoned `react-native-torch` library.

---

## ✨ Features

- ⚡ **Modern Android support using CameraX**
- 📱 **iOS torch support via `AVCaptureDevice`**
- 🔧 **Clean Kotlin & Swift native modules**
- 🔗 **Compatible with React Native 0.72+ (including Fabric)**
- 🔥 **Simple API:** `Torch.switchState(true | false)`
- 🧩 **Drop-in replacement** for the original `react-native-torch`
- 🛠 Actively **maintained as `react-native-torch-rcl`**

---

## 📦 Installation

```sh
npm install react-native-torch-rcl
# or
yarn add react-native-torch-rcl
```

iOS Setup

```sh
cd ios && pod install

```
    
Usage
```sh
import Torch from 'react-native-torch-rcl';

// Turn ON
Torch.switchState(true);

// Turn OFF
Torch.switchState(false);

```