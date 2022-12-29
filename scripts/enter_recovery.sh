#!/bin/bash

# put device into recovery mode
idevicepair pair
uuid=$(ideviceinfo | grep "UniqueDeviceID" | cut -f 2 -d " ")
echo "Detected Device with UUID => $uuid"
echo "[-] Putting device into recovery mode"
ideviceenterrecovery -d $uuid
