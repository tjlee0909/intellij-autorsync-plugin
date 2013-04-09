package autorsync;

/**
 * Contains some global state of the whole application. Kind of a hack, but can't think of a better way
 * to solve tools window problem with auto-rsync.
 */
public abstract class State
{
	public static Boolean insideSync = false;
	public static String previousActiveWindowID = null;
}
