package com.github.thomasfischl.kylang.test.ui.launchers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class KyLangTab extends AbstractLaunchConfigurationTab {

  private String projectName;

  private Text fProjectText;
  private Button fProjectBrowse;

  private Text fProgramText;
  private Button fProgramBrowse;

  @Override
  public void createControl(Composite parent) {
    Font font = parent.getFont();

    Composite comp = new Composite(parent, SWT.NONE);
    setControl(comp);
    GridLayout topLayout = new GridLayout();
    topLayout.verticalSpacing = 0;
    topLayout.numColumns = 3;
    comp.setLayout(topLayout);
    comp.setFont(font);

    createVerticalSpacer(comp, 3);

    Label projectLabel = new Label(comp, SWT.NONE);
    projectLabel.setText("&Rayden Project:");
    GridData gd = new GridData(GridData.BEGINNING);
    projectLabel.setLayoutData(gd);
    projectLabel.setFont(font);

    fProjectText = new Text(comp, SWT.SINGLE | SWT.BORDER);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    fProjectText.setLayoutData(gd);
    fProjectText.setFont(font);
    fProjectText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        updateLaunchConfigurationDialog();
      }
    });

    fProjectBrowse = createPushButton(comp, "&Browse...", null); //$NON-NLS-1$
    fProjectBrowse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        browsePasProjects();
      }
    });

    Label programLabel = new Label(comp, SWT.NONE);
    programLabel.setText("&Rayden Test:");
    gd = new GridData(GridData.BEGINNING);
    programLabel.setLayoutData(gd);
    programLabel.setFont(font);

    fProgramText = new Text(comp, SWT.SINGLE | SWT.BORDER);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    fProgramText.setLayoutData(gd);
    fProgramText.setFont(font);
    fProgramText.addModifyListener(new ModifyListener() {
      @Override
      public void modifyText(ModifyEvent e) {
        updateLaunchConfigurationDialog();
      }
    });

    fProgramBrowse = createPushButton(comp, "&Browse...", null); //$NON-NLS-1$
    fProgramBrowse.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        browsePasFiles();
      }
    });
  }

  /**
   * Open a resource chooser to select a Pascal Project
   */
  protected void browsePasProjects() {
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new LabelProvider() {
      @Override
      public String getText(Object element) {
        IProject project = (IProject) element;
        return project.getName();
      }
    });
    List<IProject> elements = new ArrayList<IProject>();
    for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
      if (project.isAccessible()) {
        elements.add(project);
      } // if
    } // for
    dialog.setElements(elements.toArray());
    dialog.setTitle("Rayden Project");
    if (dialog.open() == Window.OK) {
      Object[] result = dialog.getResult();
      if (result.length == 1) {
        IProject proj = (IProject) result[0];
        fProjectText.setText(proj.getLocation().toString());
        projectName = proj.getName();
        updateLaunchConfigurationDialog();
      } // if
    } // if
  }

  /**
   * Open a resource chooser to select a KyLang test file
   */
  protected void browsePasFiles() {
    ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new LabelProvider() {
      @Override
      public String getText(Object element) {
        IFile file = (IFile) element;
        return file.getName();
      }
    });
    List<IFile> elements = new ArrayList<IFile>();
    try {
      for (IResource resource : ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getFolder("src").members()) {
        if (resource instanceof IFile && (resource.getName().endsWith(".kytest"))) {
          elements.add((IFile) resource);
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }

    dialog.setElements(elements.toArray());
    dialog.setTitle("Rayden Test");
    if (dialog.open() == Window.OK) {
      Object[] result = dialog.getResult();
      if (result.length == 1) {
        IFile file = (IFile) result[0];
        fProgramText.setText(file.getName());
        updateLaunchConfigurationDialog();
      } // if
    } // if
  }

  @Override
  public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    try {
      fProjectText.setText(configuration.getAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_PATH, new String()));
      projectName = configuration.getAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_NAME, new String());
      fProgramText.setText(configuration.getAttribute(KyLangLaunchConfiguration.TEST_FILE, new String()));
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_PATH, fProjectText.getText());
    configuration.setAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_NAME, projectName);
    configuration.setAttribute(KyLangLaunchConfiguration.TEST_FILE, fProgramText.getText());
  }

  @Override
  public String getName() {
    return "Main";
  }

}
