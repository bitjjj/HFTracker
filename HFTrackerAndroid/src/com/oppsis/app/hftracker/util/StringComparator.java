package com.oppsis.app.hftracker.util;

import java.util.Comparator;

public class StringComparator implements Comparator<String>{

	private boolean isAsc = true;
	
	public StringComparator(){}
	
	public StringComparator(boolean isAsc){
		this.isAsc = isAsc;
	}
	
	@Override
	public int compare(String lhs, String rhs) {
		return isAsc ? lhs.compareTo(rhs) : - lhs.compareTo(rhs);
	}

}
