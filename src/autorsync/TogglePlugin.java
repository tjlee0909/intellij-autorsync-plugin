package autorsync;

import autorsync.RsyncAppComponent;
import autorsync.Settings;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;

public class TogglePlugin extends AnAction
{
	private RsyncAppComponent _rsyncAppComponent;

    public void actionPerformed(AnActionEvent e)
    {
	    this.getRsyncAppComponent(e).toggleRsyncEnabled(e.getProject());
	    this.update(e);
    }

    @Override
    public void update(AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        presentation.setText(Settings.isRsyncEnabled(e.getProject()) ? "Disable Box Files synchronization" : "Enable Box Files synchronization");
        super.update(e);
    }

	private RsyncAppComponent getRsyncAppComponent(AnActionEvent event)
	{
		if (this._rsyncAppComponent == null && event != null)
			this._rsyncAppComponent = ApplicationManager.getApplication().getComponent(RsyncAppComponent.class);

		return this._rsyncAppComponent;
	}
}