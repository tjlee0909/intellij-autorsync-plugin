package autorsync;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.Project;

public class RsyncAppComponent implements ApplicationComponent
{
	private RsyncProjectManagerListener _projectManagerListener;

	public void initComponent()
	{
		ProjectManager.getInstance().addProjectManagerListener(this.getProjectManagerListener());
	}

	public String getComponentName()
	{
		return "BoxRsyncPluginComponent";
	}

	public void disposeComponent()
	{
		ProjectManager.getInstance().removeProjectManagerListener(this.getProjectManagerListener());
	}

	private ProjectManagerListener getProjectManagerListener()
	{
		if (this._projectManagerListener == null)
			this._projectManagerListener = new RsyncProjectManagerListener();

		return this._projectManagerListener;
	}

	public void toggleRsyncEnabled(Project project)
	{
		// Toggle persisted state in settings
		Boolean newRsyncEnabledStatus = !Settings.isRsyncEnabled(project);
		Settings.setRsyncEnabled(newRsyncEnabledStatus, project);

		// Toggle actual behavior in Project manager listener
		if (newRsyncEnabledStatus)
			this._projectManagerListener.EnableRsync(project);
		else
			this._projectManagerListener.DisableRsync(project);
	}
}
