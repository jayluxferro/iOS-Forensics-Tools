#!/bin/bash

# script to install all necessary tools

# init
echo '[-] Installing necessary tools'

# xcode command-line utils
xcode-select --install 2> /dev/null

# homebrew installation
if [ ! "$(which brew)" ]; then
  echo '[-] Installing homebrew'
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

# install extra tools
brew install esolitos/ipa/sshpass
brew install libimobiledevice sshpass ideviceinstaller libusb libtool automake libirecovery

# done
echo '[-] Installation done.'