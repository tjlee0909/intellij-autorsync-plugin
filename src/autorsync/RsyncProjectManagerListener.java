package autorsync;

import com.intellij.ide.FrameStateListener;
import com.intellij.ide.FrameStateManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.tools.Tool;
import com.intellij.tools.ToolManager;
import java.util.HashMap;

public class RsyncProjectManagerListener implements ProjectManagerListener
{
	private HashMap<Project, FrameStateListener> _projectFrameStateListenerMap = new HashMap<Project, FrameStateListener>();
	private Tool _rsyncTool;

	public void projectOpened(Project project)
	{
		if (Settings.isRsyncEnabled(project))
			this.EnableRsync(project);

		ToolWindowManagerEx.getInstanceEx(project).addToolWindowManagerListener(new RsyncToolWindowManagerListener(project));
	}

	public void projectClosed(Project project)
	{
		this.DisableRsync(project);
	}

	public boolean canCloseProject(Project project) { return true; }

	public void projectClosing(Project project) { /* No op */ }

	public void DisableRsync(Project project)
	{
		if (this._projectFrameStateListenerMap.containsKey(project))
		{
			FrameStateManager.getInstance().removeListener(this._projectFrameStateListenerMap.get(project));
			this._projectFrameStateListenerMap.remove(project);
		}
	}

	public void EnableRsync(Project project)
	{
		if (!this._projectFrameStateListenerMap.containsKey(project))
		{
			FrameStateListener listener = this.getFrameStateListener(project);
			FrameStateManager.getInstance().addListener(listener);
			this._projectFrameStateListenerMap.put(project, listener);
		}
	}

	/**
	* Build a frame state listener
	*
	* @param project
	* @return FrameStateListener
	*/
	private FrameStateListener getFrameStateListener(Project project)
	{
		return new RsyncFrameStateListener(this.getRsyncTool(), project);
	}

	/**
	* Get the rsync tool that is configured in Intellij. If the developer hasn't configured it yet,
	* then will return null.
	*
	* @return Tool|null
	*/
	private Tool getRsyncTool()
	{
		if (this._rsyncTool == null)
		{
			for (Tool tool : ToolManager.getInstance().getTools())
			{
				if (tool.getName().equals(Settings.RSYNC_TOOL_NAME))
				{
					this._rsyncTool = tool;
					break;
				}
			}
		}

		return this._rsyncTool;
	}
}
