package com.github.thomasfischl.kylang.test.ui.launchers;

import java.io.InputStreamReader;

import javax.script.ScriptException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import com.github.thomasfischl.kylang.runtime.KyLangReporter;
import com.github.thomasfischl.kylang.runtime.KyLangScriptEngine;
import com.github.thomasfischl.kylang.runtime.KyLangScriptEngineFactory;
import com.github.thomasfischl.kylang.test.ui.helpers.ConsoleDisplayManager;
import com.github.thomasfischl.kylang.test.ui.helpers.LaunchHelper;
import com.github.thomasfischl.kylang.test.ui.helpers.UiHelpers;

public class KyLangLaunchConfigurationDelegate extends LaunchConfigurationDelegate {

  @Override
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
    // clear problem view
    UiHelpers.removeMarkers();

    // boolean isTest = false;
    // String commandline = "";
    // String srcpath = "/src/";
    String projectname = configuration.getAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_NAME, new String());
    // String projectpath = configuration.getAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_PATH, new String());
    String programpath = configuration.getAttribute(KyLangLaunchConfiguration.TEST_FILE, new String());
    // String programname = programpath.substring(0, programpath.lastIndexOf("."));

    IResource resource = LaunchHelper.GetResourceFromFile(ResourcesPlugin.getWorkspace().getRoot().getProject(projectname), programpath, false);
    if (resource instanceof IFile) {
      try {
        KyLangScriptEngineFactory factory = new KyLangScriptEngineFactory();
        KyLangScriptEngine engine = (KyLangScriptEngine) factory.getScriptEngine();

        engine.setReporter(new KyLangReporterExtension());

        engine.eval(new InputStreamReader(((IFile) resource).getContents()));
      } catch (ScriptException e) {
        e.printStackTrace();
        UiHelpers.reportError(ResourcesPlugin.getWorkspace().getRoot(), 0, 0, "Script Error: " + e.getMessage());
      }
    } else {
      UiHelpers.reportError(ResourcesPlugin.getWorkspace().getRoot(), 0, 0, "File '" + programpath + "' does not exists!");
    }
  }

  private final class KyLangReporterExtension extends KyLangReporter {

    private ConsoleDisplayManager cdm;

    public KyLangReporterExtension() {
      cdm = UiHelpers.getConsoleDisplayManager();
      cdm.clear();
    }

    @Override
    protected void report(String msg) {
      cdm.println(formatMessae(msg));
    }
  }
}
