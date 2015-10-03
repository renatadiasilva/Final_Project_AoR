package pt.uc.dei.aor.pf.admin;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;

import pt.uc.dei.aor.pf.reports.ReportItem;

@Named
@SessionScoped
public class CreateGraphic implements Serializable {

	private static final long serialVersionUID = -2813659390065788411L;
	private LineChartModel lineModel;
	private BarChartModel barModel;
	 
	 
	public void createLineModel(String title, List<ReportItem> report,
			String period, String category, Long maxY) {
		lineModel = initCategoryModel(report);
		lineModel.setTitle(title);
		lineModel.getAxes().put(AxisType.X, new CategoryAxis(period));

		Axis yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel(category);
		yAxis.setMin(0);
		yAxis.setMax(maxY+5);
	}

	private LineChartModel initCategoryModel(List<ReportItem> report) {
		LineChartModel model = new LineChartModel();

		ChartSeries category = new ChartSeries();

		for (ReportItem item: report)
			category.set(item.getDateHeader(), item.getMeasure());
		model.addSeries(category);

		return model;
	}

    public void createBarModel(String title, List<ReportItem> report,
			String period, String category, Long maxY) {
        barModel = initBarModel(report);
        barModel.setTitle(title);
         
		barModel.getAxes().put(AxisType.X, new CategoryAxis(period));
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel(category);
        yAxis.setMin(1);
        yAxis.setMax(maxY+5);
    }
     
    private BarChartModel initBarModel(List<ReportItem> report) {
        BarChartModel model = new BarChartModel();
 
        ChartSeries category = new ChartSeries();
		
        for (ReportItem item: report)
			category.set(item.getDateHeader(), item.getMeasure());
        model.addSeries(category);
        
        return model;
    }
     
    public BarChartModel getBarModel() {
        return barModel;
    }

    public LineChartModel getLineModel() {
    	return lineModel;
    }

}