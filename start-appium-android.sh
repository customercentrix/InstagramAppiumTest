#!/bin/bash

function die {
    echo $1
    exit 1
}

pkg_root_dir=`find $PWD | grep "/Config$" | head -n 1 | xargs dirname`
app_filename="/Users/sonny/loadstorm/workspaces/dev/InstagramAppiumTest/lib/instagram.apk"
ls -1 $app_filename || die "Did not find app in $pkg_root_dir"

appium --pre-launch --app-pkg com.instagram.android  --app-activity .activity.MainTabActivity --platform-name Android --app $app_filename

