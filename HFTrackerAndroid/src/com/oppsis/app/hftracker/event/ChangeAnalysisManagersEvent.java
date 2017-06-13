package com.oppsis.app.hftracker.event;

import java.util.List;

public class ChangeAnalysisManagersEvent {
	
	private List<String> mSelectedManagers;
	
	public ChangeAnalysisManagersEvent(List<String> selectedManagers){
		this.mSelectedManagers = selectedManagers;
	}
	
	public List<String> getSelectedManagers() {
		return mSelectedManagers;
	}

	
}
