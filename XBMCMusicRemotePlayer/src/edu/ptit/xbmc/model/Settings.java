package edu.ptit.xbmc.model;

import java.io.Serializable;

public class Settings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8140754327381945252L;
	private String piUrl;
	private int piPort;
	private boolean shuffled, muted;
	private int repeat, volume;

	public int getRepeat() {
		return repeat;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public static int REPEAT_ONE = 1;
	public static int REPEAT_NONE = 0;
	public static int REPEAT_ALL = 3;

	public Settings() {
	}

	public String getPiUrl() {
		return piUrl;
	}

	public void setPiUrl(String piUrl) {
		this.piUrl = piUrl;
	}

	public int getPiPort() {
		return piPort;
	}

	public void setPiPort(int piPort) {
		this.piPort = piPort;
	}

	public boolean isShuffled() {
		return shuffled;
	}

	public void setShuffled(boolean shuffled) {
		this.shuffled = shuffled;
	}

	

	public void setRepeate(String repeat) {
		if (repeat.equalsIgnoreCase("None"))
			this.repeat = REPEAT_NONE;
		if (repeat.equalsIgnoreCase("One"))
			this.repeat = REPEAT_ONE;
		if (repeat.equalsIgnoreCase("All"))
			this.repeat = REPEAT_ALL;
	}

}
