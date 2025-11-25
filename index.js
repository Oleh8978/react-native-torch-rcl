import { NativeModules, PermissionsAndroid, Platform } from 'react-native';
const TorchNative = NativeModules.Torch || NativeModules.RCTTorch || NativeModules.ReactNativeTorch || NativeModules.reactNativeTorch || NativeModules.rctorch || NativeModules['react-native-torch'] || NativeModules['react-native-torch-rcl'];

async function requestCameraPermission() {
  if (Platform.OS !== 'android') return true;
  const granted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
  return granted === PermissionsAndroid.RESULTS.GRANTED;
}

async function switchState(newState) {
  if (!TorchNative || !TorchNative.switchState) {
    throw new Error('Native module Torch is not available.');
  }
  return TorchNative.switchState(newState);
}

async function hasTorch() {
  if (!TorchNative || !TorchNative.hasTorch) return false;
  try {
    return await TorchNative.hasTorch();
  } catch (e) {
    return false;
  }
}

async function turnOn() { return switchState(true); }
async function turnOff() { return switchState(false); }

export default {
  requestCameraPermission,
  switchState,
  hasTorch,
  turnOn,
  turnOff
};
export { requestCameraPermission, switchState, hasTorch, turnOn, turnOff };
