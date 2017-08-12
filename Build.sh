 #!/bin/bash
 #
 # Copyright © 2017, Ayush Rathore "AyushR1" <ayushrathore12501@gmail.com>
 #
 # If Anyone Use this Scrips Maintain Proper Credits
 #
 # This software is licensed under the terms of the GNU General Public
 # License version 2, as published by the Free Software Foundation, and
 # may be copied, distributed, and modified under those terms.
 #
 # This program is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 # GNU General Public License for more details.
 #
 # Please maintain this if you use this script or any part of it

ROOT=$PWD
BUILD='/home/ayushr1/AR_Beast/KernelAdiutor-Mod/app/build/outputs/apk'
OUT='/home/ayushr1/AR_Beast/out'
APK='/home/ayushr1/AR_Beast/KernelAdiutor-Mod/app/build/outputs/apk/app-debug.apk'



blue='\033[0;34m'
yellow='\033[0;33m'
red='\033[0;31m'

echo -e "$blue***********************************************"
echo "          Building KA-Mod         "
echo -e "***********************************************"

./gradlew clean build
if ! [ -a $APK ];
then
echo -e "$red Fix the errors!"
exit 1
fi
echo "**** Done. ****"



