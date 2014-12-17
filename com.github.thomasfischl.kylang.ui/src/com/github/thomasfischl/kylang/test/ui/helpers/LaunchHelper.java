package com.github.thomasfischl.kylang.test.ui.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class LaunchHelper {

  public static void moveFile(String target, File source) {
    File temp = new File(target);

    if (temp.exists()) {
      temp.delete();
    }

    source.renameTo(temp);
  }

  public static void ReadLogFromProcess(Process p, String path, String filename) {
    if (p != null) {
      try {
        String errorline;
        String inputline;
        String logfilename = path + "_" + filename + ".log";

        InputStream inputlog = p.getInputStream();
        InputStream errorlog = p.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputlog));
        BufferedReader errorreader = new BufferedReader(new InputStreamReader(errorlog));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfilename, false), "UTF-8"));

        while ((errorline = errorreader.readLine()) != null) {
          bw.write(errorline);
          bw.newLine();
        }

        errorreader.close();
        bw.newLine();
        bw.newLine();

        while ((inputline = reader.readLine()) != null) {
          bw.write(inputline);
          bw.newLine();
        }

        bw.flush();
        reader.close();
        bw.flush();
        bw.close();
        p.waitFor();
        p.destroy();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void WriteLogFromProcess(Process p, IResource resource) {
    ConsoleDisplayManager cdm = UiHelpers.getConsoleDisplayManager();
    cdm.clear();
    if (p != null) {
      try {
        String errorline;
        String inputline;

        InputStream inputlog = p.getInputStream();
        InputStream errorlog = p.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputlog));
        BufferedReader errorreader = new BufferedReader(new InputStreamReader(errorlog));

        while ((errorline = errorreader.readLine()) != null) {
          if (errorline.contains("UNIT TEST FAILURE")) {
            UiHelpers.reportError(resource, 1, 1, "[run] " + errorline);
          } else {
            cdm.println(errorline);
          }
        }

        errorreader.close();

        while ((inputline = reader.readLine()) != null) {
          if (inputline.contains("UNIT TEST FAILURE")) {
            UiHelpers.reportError(resource, 1, 1, "[run] " + inputline);
          } else {
            cdm.println(inputline);
          }
        }

        reader.close();
        p.waitFor();
        p.destroy();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void ParseLogFromProcess(Process p, String filename, IResource resource) {
    if (p != null && resource != null) {
      try {
        String errorline;
        String inputline;

        InputStream inputlog = p.getInputStream();
        InputStream errorlog = p.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputlog));
        BufferedReader errorreader = new BufferedReader(new InputStreamReader(errorlog));

        while ((errorline = errorreader.readLine()) != null) {
          if (errorline.contains("Error")) {
            if (getLineNumber(errorline) > 0) {
              UiHelpers.reportError(resource, getLineNumber(errorline), getCharacterIndex(errorline), "[compile] " + errorline);
            }
          } else if (errorline.contains("Note")) {
            if (getLineNumber(errorline) > 0) {
              UiHelpers.reportWarning(resource, getLineNumber(errorline), getCharacterIndex(errorline), "[compile] " + errorline);
            }
          }
        }

        errorreader.close();

        while ((inputline = reader.readLine()) != null) {
          if (inputline.contains("Error")) {
            if (getLineNumber(inputline) > 0) {
              UiHelpers.reportError(resource, getLineNumber(inputline), getCharacterIndex(inputline), "[compile] " + inputline);
            }
          } else if (inputline.contains("Note")) {
            if (getLineNumber(inputline) > 0) {
              UiHelpers.reportWarning(resource, getLineNumber(inputline), getCharacterIndex(inputline), "[compile] " + inputline);
            }
          }
        }

        reader.close();
        p.waitFor();
        p.destroy();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static IResource GetResourceFromFile(IProject project, String filename, boolean isTest) {

    try {
      if (project != null) {
        if (!isTest) {
          for (IResource resource : project.getFolder("src").members()) {
            if (resource instanceof IFile && (resource.getName().equals(filename))) {
              return resource;
            }
          }
        } else {
          for (IResource resource : project.getFolder("src-gen").members()) {
            if (resource instanceof IFile && (resource.getName().contains(filename))) {
              return resource;
            }
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static int getLineNumber(String line) {
    if (line != null && line.indexOf('(') > -1) {
      line = line.substring(line.indexOf('(') + 1);
      String lineNumber = line.substring(0, line.indexOf(','));
      return Integer.parseInt(lineNumber);
    }
    return 0;
  }

  private static int getCharacterIndex(String line) {
    if (line != null && line.indexOf(',') > -1) {
      line = line.substring(line.indexOf(',') + 1);
      String charNumber = line.substring(0, line.indexOf(')'));
      return Integer.parseInt(charNumber);
    }
    return 0;
  }
}
