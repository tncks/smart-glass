#!/bin/sh

APK_DIR=/workspace/smart-glass/app/build/outputs/apk/debug
cd ${APK_DIR}

SHARED_DIR=~/share
mkdir $SHARED_DIR


srcfile="/workspace/smart-glass/app/build/outputs/apk/debug"
dstfile="~/."

mv -r  $srcfile $dstfile


message="auto-commit f"
GIT=`which git`
REPO_DIR=~/debug
cd ${REPO_DIR}

${GIT} init
${GIT} branch -m main

${GIT} add --all .
${GIT} commit -m "$message"

gitPush=$(${GIT} push -u https://tncks@bitbucket.org/secondsmy/abcd.git main 2>&1)
echo "$gitPush"



