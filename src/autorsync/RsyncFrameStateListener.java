package autorsync;

import com.intellij.ide.FrameStateListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.tools.Tool;
import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NonNls;

public class RsyncFrameStateListener implements FrameStateListener
{
	private static final Runnable NO_OP = new Runnable() { public void run() { } };

	private Tool _rsyncTool;
	private Project _project;

	/**
	* @param rsyncTool - the external rsync tool that should be installed in this IDE
	* @param project - the current project
	*/
	public RsyncFrameStateListener(Tool rsyncTool, Project project)
	{
		this._rsyncTool = rsyncTool;
		this._project = project;
	}

	public void onFrameActivated()
	{
		if (this._rsyncTool == null || this._project == null) return;

		final Project proj = this._project;
		State.insideSync = true;

		this._rsyncTool.execute(
			new DataContext()
			{
				public Object getData(@NonNls String s)
				{
					return proj;
				}
			}
		);

		this.openPreviouslyOpenedToolWindow();
	}

	/**
	* There is a bug where auto-rsync will always force the "Run" tool window on the user. We want to
	* subdue this window when the user was focused on a different tool window, and open that window
	* back up immediately.
	*/
	private void openPreviouslyOpenedToolWindow()
	{
		final ToolWindowManagerEx toolWindowManager = ToolWindowManagerEx.getInstanceEx(this._project);

		toolWindowManager.invokeLater(new Runnable() {
			public void run()
			{
				String prevActiveWindow = State.previousActiveWindowID;

				if (prevActiveWindow != null && !prevActiveWindow.equals(toolWindowManager.getActiveToolWindowId()))
				{
					ToolWindow toolWindow = toolWindowManager.getToolWindow(prevActiveWindow);
					toolWindow.show(NO_OP);
				}

				State.insideSync = false;
			}
		});
	}

	public void onFrameDeactivated() { }
}
