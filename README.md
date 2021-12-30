# LogFX
A tool for rating the funds

jlink --no-header-files --no-man-pages --strip-debug --output javafxImage --module-path $PATH_TO_FX_MODS --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media

jpackage -n LogFX --app-version 1.0  --vendor zhenxing.lu -i target --description "A tool for viewing multiple logs from muliplte sites"  --copyright "Copyright 2021, All rights reserved"  --main-jar LogFx-1.0-SNAPSHOT.jar --main-class org.logfx.LogFx  --win-shortcut  --win-dir-chooser --win-menu  --runtime-image javafxImage --type msi  -d output  --verbose --icon target/classes/logfx.ico


