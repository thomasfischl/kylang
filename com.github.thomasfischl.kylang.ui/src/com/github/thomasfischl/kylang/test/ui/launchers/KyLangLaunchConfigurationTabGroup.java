package com.github.thomasfischl.kylang.test.ui.launchers;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class KyLangLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
  @Override
  public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
    ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new KyLangTab(),
    // new CommonTab()
    };

    setTabs(tabs);
  } // createTabs
} // PascalLaunchConfigurationTabGroup
