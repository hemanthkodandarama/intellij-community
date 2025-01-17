// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.openapi.fileTypes;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeCoreBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public final class NativeFileType implements INativeFileType {
  public static final NativeFileType INSTANCE = new NativeFileType();

  private NativeFileType() { }

  @Override
  @NotNull
  public String getName() {
    return "Native";
  }

  @Override
  @NotNull
  public String getDescription() {
    return IdeCoreBundle.message("filetype.native.description");
  }

  @Nls
  @Override
  public @NotNull String getDisplayName() {
    return IdeCoreBundle.message("filetype.native.display.name");
  }

  @Override
  @NotNull
  public String getDefaultExtension() {
    return "";
  }

  @Override
  public Icon getIcon() {
    return AllIcons.FileTypes.Custom;
  }

  @Override
  public boolean isBinary() {
    return true;
  }

  @Override
  public boolean openFileInAssociatedApplication(final Project project, @NotNull final VirtualFile file) {
    return openAssociatedApplication(file);
  }

  @Override
  public boolean useNativeIcon() {
    return true;
  }

  public static boolean openAssociatedApplication(@NotNull final VirtualFile file) {
    final List<String> commands = new ArrayList<>();
    if (SystemInfo.isWindows) {
      commands.add("rundll32.exe");
      commands.add("url.dll,FileProtocolHandler");
    }
    else if (SystemInfo.isMac) {
      commands.add("/usr/bin/open");
    }
    else if (SystemInfo.hasXdgOpen()) {
      commands.add("xdg-open");
    }
    else {
      return false;
    }
    commands.add(file.getPath());

    try {
      new GeneralCommandLine(commands).createProcess();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
}
