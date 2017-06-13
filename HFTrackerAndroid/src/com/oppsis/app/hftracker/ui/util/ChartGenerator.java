package com.oppsis.app.hftracker.ui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;
import com.github.mikephil.charting.utils.YLabels.YLabelPosition;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.model.FundIndustry;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.SPXData;
import com.oppsis.app.hftracker.util.Constants;

public class ChartGenerator {

	private static ArrayList<Integer> colorList = new ArrayList<Integer>();
	static{
		for (int c : ColorTemplate.PASTEL_COLORS)colorList.add(c);
        
        for (int c : ColorTemplate.COLORFUL_COLORS)colorList.add(c);
        
        for (int c : ColorTemplate.VORDIPLOM_COLORS)colorList.add(c);
        
        for (int c : ColorTemplate.JOYFUL_COLORS)colorList.add(c);
        
        colorList.add(ColorTemplate.getHoloBlue());
	}
	
	static class HoldingValueFormatter implements ValueFormatter{
		@Override
		public String getFormattedValue(float value) {
			return Constants.US_DECIMAL_FORMAT.format(value);
		}
		
	}
	
	static class PortValueFormatter implements ValueFormatter{
		@Override
		public String getFormattedValue(float value) {
			return String.format(Locale.getDefault(),"%.02f", value);
		}
		
	}
	
	public static void createHoldingsValueLineChart(Context context,LineChart chart,List<FundDateList> result){
		//chart.setStartAtZero(true);
		chart.setValueFormatter(new HoldingValueFormatter());
        // disable the drawing of values into the chart
		chart.setDrawYValues(false);
		chart.setDrawBorder(false);
		chart.setDrawLegend(false);		
		chart.setDescription("");

        // enable value highlighting
		chart.setHighlightEnabled(true);
        // enable touch gestures
		chart.setTouchEnabled(true);

        // enable scaling and dragging
		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
		chart.setPinchZoom(false);

		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.RIGHT_INSIDE);
        y.setTextColor(Color.WHITE);
        y.setFormatter(new HoldingValueFormatter());
        y.setLabelCount(3);
        
        
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = result.size() - 1; i >=0 ; i--) {
            xVals.add(result.get(i).getDateStr().substring(2));//using yy-mm-dd for saving space
        }

        ArrayList<Entry> valsEntry = new ArrayList<Entry>();
        for (int i = result.size() - 1,j=0; i >=0 ; i--,j++) {
            float val = Float.valueOf(result.get(i).getPortfolioValue().replaceAll(Constants.REGEX_SPECIAL_CHARS, "")) / 1000;
            valsEntry.add(new Entry(val, j));
        }
        
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(valsEntry, "Holdings Value DataSet");
        set1.setDrawCubic(true);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false); 

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);

        // set data
        chart.setData(data);

        chart.animateXY(2000, 2000);
        chart.invalidate();
	}
	
	public static void createIndustryPieChart(final Context context,PieChart chart,List<FundIndustry> result){
		

		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setHoleColor(context.getResources().getColor(R.color.pie_chart_holo));
        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(65f);

        chart.setDescription("");

        chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(true);
        chart.setRotationAngle(0);
        chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.setCenterText(result.get(0).getDateStr());
        chart.setCenterTextSize(18f);
        
        Paint centerPaint = chart.getPaint(PieChart.PAINT_CENTER_TEXT);
        centerPaint.setColor(Color.WHITE);
        chart.setPaint(centerPaint, PieChart.PAINT_CENTER_TEXT);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < result.size(); i++) {
            yVals1.add(new Entry(result.get(i).getNum(), i));
        }

        final ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < result.size(); i++)
            xVals.add(FundLocalInfoManager.getI18nIndustryName(context,result.get(i).getName()));

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);

        set1.setColors(colorList);

        PieData data = new PieData(xVals, set1);
        chart.setData(data);

        chart.highlightValues(null);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){

			@Override
			public void onValueSelected(Entry e, int dataSetIndex) {
				if (e == null)
		            return;
		        
		        Toast.makeText(context, xVals.get(e.getXIndex()), Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected() {}
        	
        });

        chart.invalidate();

        chart.animateXY(1500, 1500);
        //chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
		
		
	}
	
	public static void createHoldingHistoryLineChart(final Context context,LineChart chart,final List<FundHolding> result){
		
		chart.setValueFormatter(new PortValueFormatter());
		chart.setStartAtZero(false);
		chart.setDrawYValues(true);
		chart.setDrawBorder(false);
		chart.setDrawLegend(false);	
		chart.setDescription("");

		chart.setHighlightEnabled(true);

		chart.setTouchEnabled(true);

		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		chart.setPinchZoom(true);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.RIGHT);
        y.setFormatter(new PortValueFormatter());
        y.setTextColor(Color.WHITE);

        // add data
        final ArrayList<String> xVals = new ArrayList<String>();
        for (int i = result.size() - 1; i >= 0; i--) {
            xVals.add(result.get(i).getDateStr().substring(2));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = result.size() - 1,j = 0; i >= 0; i--,j++) {
            yVals.add(new Entry(result.get(i).getPort(), j));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setLineWidth(1.5f);
        set1.setCircleSize(4f);

        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        chart.setData(data);
        
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){

			@Override
			public void onValueSelected(Entry e, int dataSetIndex) {
				if (e == null)
		            return;
		        
				int size = result.size(),index = size - e.getXIndex() -1;
				String tip = context.getString(R.string.chart_mutual_security_history_tip, 
						result.get(index).getDateStr(),
						Constants.US_DECIMAL_FORMAT.format(result.get(index).getShares()),
						e.getVal());
		        Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
				
			}

			@Override
			public void onNothingSelected() {}
        	
        });


        chart.invalidate();
	}
	
	public static void createHoldingComparatorPortLineChart(final Context context,LineChart chart,final ArrayList<String> xVals,final ArrayList<List<FundHolding>> holdings){
		chart.setValueFormatter(new PortValueFormatter());
		chart.setStartAtZero(false);
		chart.setDrawYValues(true);
		chart.setDrawBorder(false);
		chart.setDrawLegend(true);	
		chart.setDescription(context.getString(R.string.chart_mutual_security_comparator_port_desc));

		chart.setHighlightEnabled(true);

		chart.setTouchEnabled(true);

		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		chart.setPinchZoom(true);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.RIGHT);
        y.setFormatter(new PortValueFormatter());
        y.setTextColor(Color.WHITE);

       

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int z = 0; z < holdings.size(); z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < holdings.get(z).size(); i++) {
                values.add(new Entry(holdings.get(z).get(i).getPort(), i));
            }

            LineDataSet d = new LineDataSet(values, FundObject.getManagerName(holdings.get(z).get(0).getFund_name()));
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);

            int color = colorList.get(z % colorList.size());
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }
        
        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        
        chart.animateXY(1500, 1500);
        chart.invalidate();
        
        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
	
	}
	
	
	public static void createHoldingComparatorSharesLineChart(final Context context,LineChart chart,final ArrayList<String> xVals,final ArrayList<List<FundHolding>> holdings){
		chart.setValueFormatter(new HoldingValueFormatter());
		chart.setStartAtZero(false);
		chart.setDrawYValues(false);
		chart.setDrawBorder(false);
		chart.setDrawLegend(true);	
		chart.setDescription(context.getString(R.string.chart_mutual_security_comparator_shares_desc));

		chart.setHighlightEnabled(true);

		chart.setTouchEnabled(true);

		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		chart.setPinchZoom(true);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_accent_1));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.RIGHT);
        y.setFormatter(new HoldingValueFormatter());
        y.setTextColor(Color.WHITE);

       

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int z = 0; z < holdings.size(); z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < holdings.get(z).size(); i++) {
                values.add(new Entry(holdings.get(z).get(i).getShares(), i));
            }

            LineDataSet d = new LineDataSet(values, FundObject.getManagerName(holdings.get(z).get(0).getFund_name()));
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);

            int color = colorList.get(z % colorList.size());
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }
        
        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        
        chart.animateXY(1500, 1500);
        chart.invalidate();
        
        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
	}
	
	public static void createAnlysisPortLineChart(final Context context,LineChart chart,final ArrayList<String> xVals,final ArrayList<List<FundDateList>> holdings){
		chart.setValueFormatter(new HoldingValueFormatter());
		chart.setStartAtZero(false);
		chart.setDrawYValues(false);
		chart.setDrawBorder(false);
		chart.setDrawLegend(true);	
		chart.setDescription("");

		chart.setHighlightEnabled(true);

		chart.setTouchEnabled(true);

		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		chart.setPinchZoom(true);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        //chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.RIGHT_INSIDE);
        y.setTextColor(Color.WHITE);
        y.setFormatter(new HoldingValueFormatter());
        //y.setLabelCount(3);
       

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int i = 0; i < holdings.size(); i++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int j = 0; j < holdings.get(i).size(); j++) {
            	float val = Float.valueOf(holdings.get(i).get(j).getPortfolioValue().replaceAll(Constants.REGEX_SPECIAL_CHARS, "")) / 1000;
                values.add(new Entry(val, j));
            }
            
            LineDataSet d = new LineDataSet(values, FundLocalInfoManager.getI18nFundProperty(context, holdings.get(i).get(0).getFund_name(), FundObject.PROPERTY_MANAGER_NAME));
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);

            int color = colorList.get(i % colorList.size());
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }
        
        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        
        chart.animateXY(1500, 1500);
        chart.invalidate();
        
        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);

	}
	
	@Deprecated
	public static void createAnlysisIndustryBarChart(final Context context,BarChart chart,final ArrayList<String> fundNameList,final ArrayList<List<FundIndustry>> industrySetList){
		chart.setValueFormatter(new PortValueFormatter());

		chart.setDrawYValues(true);
		chart.setDrawLegend(true);	
		chart.setDescription("");

		chart.setDrawValuesForWholeStack(true);

		chart.set3DEnabled(false);
		chart.setPinchZoom(false);
		chart.setDrawBarShadow(false);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        //chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.TOP);
        x.setTextColor(Color.WHITE);
        x.setCenterXLabelText(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.LEFT);
        y.setTextColor(Color.WHITE);
        y.setFormatter(new PortValueFormatter());
        y.setLabelCount(3);
       

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < industrySetList.size(); i++) {
            List<FundIndustry> industryList = industrySetList.get(i);
            float[] values = new float[industryList.size()];
            for (int j = 0; j < industryList.size(); j++) {
            	values[j] = industryList.get(j).getNum();
			}
            
            yVals1.add(new BarEntry(values, i));
        }
        
        List<String> labels = new ArrayList<String>();//get industry names from list
        for(int i = 0; industrySetList.size() > 0 && i < industrySetList.get(0).size(); i++){
        	labels.add(industrySetList.get(0).get(i).getName());
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Industry Chart");
        set1.setColors(colorList);
        set1.setStackLabels(labels.toArray(new String[0]));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        ArrayList<String> xVals = new ArrayList<String>();
		for(int i=0; i<fundNameList.size();i++){
			xVals.add(FundObject.getManagerName(fundNameList.get(i)));
		}
        
        BarData data = new BarData(xVals, dataSets);

        
        chart.setData(data);
        chart.animateXY(1500, 1500);
        chart.invalidate();
        
        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_RIGHT);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
	}
	
	public static void createAnlysisIndustryGroupBarChart(final Context context,BarChart chart,final ArrayList<String> fundNameList,
			final ArrayList<String> industryNameList,final ArrayList<List<FundIndustry>> industrySetList){
		chart.setValueFormatter(new PortValueFormatter());

		chart.setDrawYValues(false);
		chart.setDrawLegend(true);	
		chart.setDescription("");

		chart.setDrawValuesForWholeStack(true);

		chart.set3DEnabled(false);
		chart.setPinchZoom(false);
		chart.setDrawBarShadow(false);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawHorizontalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.TOP);
        x.setTextColor(Color.WHITE);
        x.setSpaceBetweenLabels(2);

        //x.setCenterXLabelText(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.LEFT);
        y.setTextColor(Color.WHITE);
        y.setFormatter(new PortValueFormatter());
        y.setLabelCount(3);
        
        
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		for(int i=0; i<industrySetList.size(); i++){
			ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
			
			List<FundIndustry> industryList = industrySetList.get(i);
	        for (int j = 0; j < industryList.size(); j++) {
	        	yVals.add(new BarEntry(industryList.get(j).getNum(), j));
	        }
	        
	        BarDataSet set = new BarDataSet(yVals, FundLocalInfoManager.getI18nFundProperty(context, fundNameList.get(i), FundObject.PROPERTY_MANAGER_NAME));
	        set.setColor(colorList.get(i));
	        
	        dataSets.add(set);
		}


		ArrayList<String> i18nIndustryNameList = new ArrayList<String>();
		for(int i=0; i<industryNameList.size(); i++){
			if(context.getResources().getBoolean(R.bool.tablet_land)){
				i18nIndustryNameList.add(FundLocalInfoManager.getI18nIndustryName(context, industryNameList.get(i)));
			}
			else{
				i18nIndustryNameList.add(FundLocalInfoManager.getI18nIndustryAbbrName(context, industryNameList.get(i)));
			}
		}
        BarData data = new BarData(i18nIndustryNameList, dataSets);
        
        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(110f);
        
        chart.setData(data);
        chart.animateXY(1500, 1500);
        chart.invalidate();
        
        Legend l = chart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);

	}
	
	public static void createSPXLineChart(final Context context,LineChart chart,List<SPXData> result){
		chart.setValueFormatter(new PortValueFormatter());
		chart.setStartAtZero(false);
		chart.setDrawYValues(false);
		chart.setDrawBorder(false);
		chart.setDrawLegend(false);	
		chart.setDescription("");
		chart.setNoDataTextDescription("You need to provide data for the chart.");

		chart.setHighlightEnabled(true);

		chart.setTouchEnabled(true);

		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);

		chart.setPinchZoom(true);
		
		chart.setBackgroundColor(context.getResources().getColor(R.color.theme_primary_light));
		chart.setDrawGridBackground(false);
		chart.setDrawVerticalGrid(false);
		chart.setGridColor(Color.WHITE & 0x70FFFFFF);

        //chart.setHighlightIndicatorEnabled(false);
        
        XLabels x = chart.getXLabels();
        x.setPosition(XLabelPosition.BOTTOM);
        x.setTextColor(Color.WHITE);
        x.setAvoidFirstLastClipping(true);
        //x.setSpaceBetweenLabels(3);

        
        YLabels y = chart.getYLabels();
        y.setPosition(YLabelPosition.LEFT_INSIDE);
        y.setTextColor(Color.WHITE);
        y.setFormatter(new PortValueFormatter());
        //y.setLabelCount(3);
       

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = result.size() - 1; i >=0 ; i--) {
            xVals.add(result.get(i).getYear());
        }

        float avg = 0.0f;
        ArrayList<Entry> valsEntry = new ArrayList<Entry>();
        for (int i = result.size() - 1,j=0; i >=0 ; i--,j++) {
        	avg += result.get(i).getValue();
            valsEntry.add(new Entry(result.get(i).getValue(), j));
        }
        
        LineDataSet set1 = new LineDataSet(valsEntry, "SPX");
        set1.setLineWidth(1.5f);
        //set1.setColor(Color.GREEN);
        //set1.setCircleSize(3f);
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        
        LimitLine ll = new LimitLine(avg / result.size());
        ll.setLineWidth(4f);
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setDrawValue(true);
        ll.setLabelPosition(LimitLabelPosition.LEFT);

        data.addLimitLine(ll);
        
        chart.setData(data);
        
        chart.animateXY(1500, 1500);
        chart.invalidate();

	}
	
	@SuppressWarnings("rawtypes")
	public static void clearChart(Chart chart){
		chart.clear();
	}
	
}
