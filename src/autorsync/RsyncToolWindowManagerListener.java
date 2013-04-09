package autorsync;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import org.jetbrains.annotations.NotNull;

public class RsyncToolWindowManagerListener implements ToolWindowManagerListener
{
	private static final String RUN_TOOL_WINDOW_ID = "Run";
	private static final Runnable NO_OP = new Runnable() { public void run() { } };

	private Project _project;

	public RsyncToolWindowManagerListener(Project project)
	{
		this._project = project;
	}

	public void stateChanged()
	{
		final ToolWindowManagerEx toolWindowManager = ToolWindowManagerEx.getInstanceEx(this._project);
		String activeToolWindowId = toolWindowManager.getActiveToolWindowId();

		// The user is focused on a window
		if (activeToolWindowId != null)
		{
			// Currently syncing with the "Run" tool window being active, and the previously selected window was not a non-"Run" window
			if (State.insideSync &&
					activeToolWindowId.equals(RUN_TOOL_WINDOW_ID) &&
					(State.previousActiveWindowID == null || !State.previousActiveWindowID.equals(RUN_TOOL_WINDOW_ID)))
			{
					toolWindowManager.invokeLater(new Runnable()
					{
						public void run()
						{
							toolWindowManager.getToolWindow(RUN_TOOL_WINDOW_ID).hide(NO_OP);
						}
					});
			}

			if (!State.insideSync) State.previousActiveWindowID = activeToolWindowId;
		}
		// Not syncing, and the user has a focus on a window
		else if (!State.insideSync)
		{
			boolean isToolWindowVisible = false;

			for (String id : toolWindowManager.getToolWindowIds())
			{
				ToolWindow toolWindow = toolWindowManager.getToolWindow(id);

				if (toolWindow.isVisible() && toolWindow.getContentUiType().getName().equals("tabs") && toolWindow.getAnchor().isHorizontal())
				{
					isToolWindowVisible = true;
					break;
				}
			}

			if (!isToolWindowVisible) State.previousActiveWindowID = null;
		}
	}

	public void toolWindowRegistered(@NotNull String s) { }
}
