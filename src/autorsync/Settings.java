package autorsync;

import com.intellij.openapi.project.Project;
import com.intellij.ide.util.PropertiesComponent;

public abstract class Settings
{
	public static final String RSYNC_TOOL_NAME = "rsync";
	private static final String ENABLED_PROPERTY_KEY = "AutoRsyncEnabled";
	private static final String ENABLED_VALUE = "rsync_enabled";
	private static final String DISABLED_VALUE = "rsync_disabled";

	public static Boolean isRsyncEnabled(Project project)
	{
		PropertiesComponent properties = PropertiesComponent.getInstance(project);
		String enabled_string = properties.getValue(Settings.ENABLED_PROPERTY_KEY,  Settings.DISABLED_VALUE);
		return enabled_string.equals(Settings.ENABLED_VALUE);
	}

	public static void setRsyncEnabled(Boolean enabled, Project project)
	{
		PropertiesComponent properties = PropertiesComponent.getInstance(project);
		properties.setValue(Settings.ENABLED_PROPERTY_KEY, enabled ? Settings.ENABLED_VALUE : Settings.DISABLED_VALUE);
	}
}
