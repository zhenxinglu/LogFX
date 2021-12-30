#!/usr/bin/env bash

#This script build the LogFX installer for Windows. To build the installer for other Linux/Mac,
#this script may need some tuning.

javaFXImageDir=javafxImage
javaFXModules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media

if [ -z "$PATH_TO_FX_MODS" ]
then
  echo "Environment variable \$PATH_TO_FX_MODS is not set"
fi

if [ ! -d $javaFXImageDir ]
then
  #the cutom JRE is not built yet, just build it
  jlink --no-header-files --no-man-pages --strip-debug --output $javaFXImageDir --module-path $PATH_TO_FX_MODS --add-modules $javaFXModules
fi

#mvn package

appInfo="-n LogFX --app-version 1.0  --vendor zhenxing.lu"
description="A tool for viewing multiple logs from multiple sites"
copyright="Copyright 2021, All rights reserved"
iconPath=target/classes/logfx.ico
mainClass=org.logfx.LogFx
customWindowOption="--win-shortcut  --win-dir-chooser --win-menu"
jpackage $appInfo -i target --description "$description" --copyright "$copyright"  --main-jar LogFx-1.0-SNAPSHOT.jar --main-class $mainClass  $customWindowOption --runtime-image $javaFXImageDir --type msi  -d output  --verbose --icon $iconPath
