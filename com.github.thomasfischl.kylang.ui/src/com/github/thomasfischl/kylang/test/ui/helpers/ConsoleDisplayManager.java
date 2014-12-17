package com.github.thomasfischl.kylang.test.ui.helpers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class ConsoleDisplayManager {
  private static ConsoleDisplayManager fDefault = null;
  private String fTitle = null;
  private MessageConsole fMessageConsole = null;

  public static final int MSG_INFORMATION = 1;
  public static final int MSG_ERROR = 2;
  public static final int MSG_WARNING = 3;

  public ConsoleDisplayManager(String messageTitle) {
    fDefault = this;
    fTitle = messageTitle;
  }

  public static ConsoleDisplayManager getDefault() {
    return fDefault;
  }

  public void println(String msg) {
    if (msg == null)
      return;

    /*
     * if console-view in Java-perspective is not active, then show it and then display the message in the console attached to it
     */
    if (!displayConsoleView()) {
      /*
       * If an exception occurs while displaying in the console, then just diplay atleast the same in a message-box
       */
      MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", msg);
      return;
    }

    /* display message on console */
    getNewMessageConsoleStream().println(msg);
  }

  public void clear() {
    getMessageConsole().clearConsole();
  }

  private boolean displayConsoleView() {
    try {
      IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      if (activeWorkbenchWindow != null) {
        IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
        if (activePage != null)
          activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW, null, IWorkbenchPage.VIEW_VISIBLE);
      }

    } catch (PartInitException partEx) {
      return false;
    }

    return true;
  }

  private MessageConsoleStream getNewMessageConsoleStream() {
    MessageConsoleStream msgConsoleStream = getMessageConsole().newMessageStream();
    return msgConsoleStream;
  }

  private MessageConsole getMessageConsole() {
    if (fMessageConsole == null)
      createMessageConsoleStream(fTitle);

    return fMessageConsole;
  }

  private void createMessageConsoleStream(String title) {
    fMessageConsole = new MessageConsole(title, null);
    ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { fMessageConsole });
  }
}
