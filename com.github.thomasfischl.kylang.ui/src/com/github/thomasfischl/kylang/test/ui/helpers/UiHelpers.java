package com.github.thomasfischl.kylang.test.ui.helpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class UiHelpers {
  private static ConsoleDisplayManager consoleDisplayManager;
  private static List<IMarker> markers;

  private static void addMarker(IMarker m) {
    if (markers == null) {
      markers = new ArrayList<IMarker>();
    }

    markers.add(m);
  }

  public static void removeMarkers() {
    if (markers != null) {
      for (IMarker m : markers) {
        try {
          m.delete();
        } catch (CoreException e) {
          e.printStackTrace();
        }
      }

      markers.clear();
    }
  }

  public static void reportError(IResource resource, int line, int charstart, String msg) throws CoreException {
    IMarker m = resource.createMarker(IMarker.PROBLEM);
    m.setAttribute(IMarker.LINE_NUMBER, line);
    m.setAttribute(IMarker.CHAR_START, charstart);
    m.setAttribute(IMarker.MESSAGE, msg);
    m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
    m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    addMarker(m);
  }

  public static void reportWarning(IResource resource, int line, int charstart, String msg) throws CoreException {
    IMarker m = resource.createMarker(IMarker.PROBLEM);
    m.setAttribute(IMarker.LINE_NUMBER, line);
    m.setAttribute(IMarker.CHAR_START, charstart);
    m.setAttribute(IMarker.MESSAGE, msg);
    m.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
    m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
    addMarker(m);
  }

  public static ConsoleDisplayManager getConsoleDisplayManager() {
    if (consoleDisplayManager == null) {
      consoleDisplayManager = new ConsoleDisplayManager("Punit Console View");
    }

    return consoleDisplayManager;
  }
}
