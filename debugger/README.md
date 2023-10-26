## Setting up iOS Debugger

1. Locate the debugger binary

```
/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/DeviceSupport/[ios_version]/DeveloperDiskImage
```

2. Mount the `DeveloperDiskImage`

3. Sign the debugserver (in `/usr/bin/`) with the entitlement

```
codesign -s - --entitlements 123.plist -f debugserver
```

4. Transfer signed binary to the rooted device

```
scp -P 2222 -r debugserver  root@localhost:/usr/bin/debugserver
```

5. Start debugserver and attach it to a running process

```
debugserver *:3456 -a xxx
```

6. Connect with lldb

```
lldb 
process connect connect://localhost:3456
```
