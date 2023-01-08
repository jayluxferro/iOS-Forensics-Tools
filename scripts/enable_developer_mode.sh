#!/bin/bash
# Enable developer mode on macOS

echo ""
echo "Enabling developer mode on mac..."
DevToolsSecurity -enable
echo "Mac Developer mode done!"
echo ""
echo "Restart USB connection to fix random connection on/off bug...."
killall -STOP -c usbd
echo "Killed USB connection. Restarting..."
echo "Done."
echo ""
exit 1
