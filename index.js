import { NativeModules, PermissionsAndroid, Platform } from 'react-native';
const { Torch } = NativeModules || {};

async function ensureCameraPermissionAndroid() {
  if (Platform.OS !== 'android') return true;
  const has = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.CAMERA);
  if (has) return true;
  const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
  return result === PermissionsAndroid.RESULTS.GRANTED;
}

async function switchState(newState) {
  if (!Torch || !Torch.switchState) {
    throw new Error('Native module Torch is not linked or not available.');
  }

  if (Platform.OS === 'android') {
    const granted = await ensureCameraPermissionAndroid();
    if (!granted) throw new Error('Camera permission denied');
  }

  // native method returns a Promise
  return Torch.switchState(newState);
}

async function hasTorch() {
  if (!Torch || !Torch.hasTorch) return false;
  try {
    return await Torch.hasTorch();
  } catch (e) {
    return false;
  }
}

export default {
  ...Torch,
  switchState,
  hasTorch
};