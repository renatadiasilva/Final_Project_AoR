package pt.uc.dei.aor.pf.admin;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.ChartSeries;

import pt.uc.dei.aor.pf.reports.ReportItem;

@Named
@SessionScoped
public class CreateGraphic2 implements Serializable {

	private static final long serialVersionUID = -2813659390065788411L;
	private LineChartModel lineModel2;

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void createLineModels(String title, List<ReportItem> report,
			String period, String category, Long minX) {
		lineModel2 = initCategoryModel(report);
		lineModel2.setTitle(title);
		lineModel2.setShowPointLabels(true);
		lineModel2.getAxes().put(AxisType.X, new CategoryAxis(period));
		Axis yAxis = lineModel2.getAxis(AxisType.Y);
		yAxis.setLabel(category);
		yAxis.setMin(0);
		yAxis.setMax(minX);
	}

	private LineChartModel initCategoryModel(List<ReportItem> report) {
		LineChartModel model = new LineChartModel();

		ChartSeries interviews = new ChartSeries();

		for (ReportItem item: report)
			interviews.set(item.getDateHeader(), item.getMeasure());
		model.addSeries(interviews);

		return model;
	}

}