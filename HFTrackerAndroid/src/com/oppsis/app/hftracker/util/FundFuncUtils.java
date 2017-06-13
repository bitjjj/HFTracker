package com.oppsis.app.hftracker.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.model.FundChartParcelable;
import com.oppsis.app.hftracker.model.FundHolding;

public class FundFuncUtils {

	public static String getActivityDesc(int action,float activity){
		String activityStr = "--";
		switch (action) {
			case FundHolding.ACTION_ADD:
				activityStr = String.format(Constants.FUND_DETAIL_STOCK_ADD, activity + "");
				break;
			case FundHolding.ACTION_CLEAR:
				activityStr = Constants.FUND_DETAIL_STOCK_CLEAR;
				break;
			case FundHolding.ACTION_DOWN:
				activityStr = String.format(Constants.FUND_DETAIL_STOCK_DOWN, activity + "");
				break;
			case FundHolding.ACTION_UNCHANGED:
				activityStr = "--";
				break;
			case FundHolding.ACTION_UP:
				activityStr = String.format(Constants.FUND_DETAIL_STOCK_UP, activity + "");
				break;
			default:
				activityStr = activity + Constants.PERCENTAGE_MARK;
				break;
		}
		return activityStr;
	}
	
	//TODO:[Next Release]activity = 0 if standing for newly buy in?
	public static String getActivityDesc(float activity){
		String result = "";
		if(activity > 0){
			result = getActivityDesc(FundHolding.ACTION_UP,activity);
		}
		else if(activity < 0){
			result = getActivityDesc(FundHolding.ACTION_DOWN,activity);
		}
		else{
			result = getActivityDesc(FundHolding.ACTION_UNCHANGED,activity);
		}
		return result;
	}
	
	public static  boolean isSameManagerList(List<String> list1,List<String> list2){
		return list1.size() == list2.size() && list1.containsAll(list2) && list2.containsAll(list1);
	}
	
	/**
	 * 
	 * @param result
	 * @param clazz
	 * @return Entry<DateList,ResultList>
	 */
	public static <T extends FundChartParcelable> Map<String, ArrayList> alignFundChartObjects(List<T> result,
			Comparator<FundChartParcelable> comparator,Class<T> clazz){
		Set<String> fullSorterSet = new HashSet<String>();
		Map<String, List<T>> dataMap = new HashMap<String, List<T>>();
		
		for(int i=0; i<result.size(); i++){
			T holding = result.get(i);
			if(!dataMap.containsKey(holding.getIdentifier())){
				dataMap.put(holding.getIdentifier(), new ArrayList<T>());
			}
			
			dataMap.get(holding.getIdentifier()).add(holding);
			fullSorterSet.add(holding.getComparatorField());
		}
		
		for(Map.Entry<String, List<T>> entry:dataMap.entrySet()){
			if(entry.getValue().size() < fullSorterSet.size()){
				Set<String> existedDateSet = new HashSet<String>();
				for(int i=0; i<entry.getValue().size(); i++){
					existedDateSet.add(entry.getValue().get(i).getComparatorField());
				}
				
				Set<String> diffSorterSet = new HashSet<String>(fullSorterSet);
				diffSorterSet.removeAll(existedDateSet);
				
				Iterator<String> dateIter = diffSorterSet.iterator();
				while (dateIter.hasNext()) {
					String date = dateIter.next();
					T emptyData = null;
					try {
						emptyData = clazz.newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
					emptyData.initValue();
					emptyData.setIdentifier(entry.getKey());
					emptyData.setComparatorField(date);
					entry.getValue().add(emptyData);
				}
			}
		}
		
		final ArrayList<String> sorterList = new ArrayList<String>(fullSorterSet);
		Collections.sort(sorterList, new StringComparator());//desc
		
		final ArrayList<List<T>> dataLists = new ArrayList<List<T>>();
		for(Map.Entry<String, List<T>> entry:dataMap.entrySet()){
			List<T> holdingList = entry.getValue();
			Collections.sort(holdingList,comparator);
			dataLists.add(holdingList);
		}
		
		final ArrayList<String> identifierList = new ArrayList<String>();
		for(int i=0; i<dataLists.size(); i++){
			identifierList.add(dataLists.get(i).get(0).getIdentifier());
		}
		
		//Map<ArrayList<String>, ArrayList<List<T>>> resultMap = new HashMap<ArrayList<String>, ArrayList<List<T>>>();
		//resultMap.put(sorterList, dataLists);
		//return resultMap.entrySet().iterator().next();
		
		Map<String, ArrayList> resultMap = new HashMap<String, ArrayList>();
		resultMap.put(Constants.SORTER_LIST, sorterList);
		resultMap.put(Constants.IDENTIFIER_LIST, identifierList);
		resultMap.put(Constants.RESULT_LIST, dataLists);
		
		return resultMap;
	}
	
	public static List<String> getStockTickerList(List<FundAnalysis> result){
		List<String> stockList = new ArrayList<String>();
		for(FundAnalysis fundAnalysis:result){
			if(fundAnalysis.getTicker() != null && !"".equals(fundAnalysis.getTicker())){
				stockList.add(fundAnalysis.getTicker());
			}
		}
		return stockList;
	}
	
	public static void addHoldingResultsChildren(List<FundAnalysis> result,HashMap<String, List<Map.Entry<String, Float>>> holdingResults) {
		for(FundAnalysis fundAnalysis:result){
			SortedSet<Map.Entry<String, Float>> childSet = fundAnalysis.getHoldingDetails();
			holdingResults.put(fundAnalysis.getId(), new ArrayList<Map.Entry<String,Float>>(childSet));
		}
	}
	
}
