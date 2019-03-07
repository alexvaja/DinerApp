package dinerapp.model;

import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import dinerapp.model.dto.StatisticDTO;

@SessionScope
public class StatisticsViewModel {

	private List<StatisticDTO> statistics;

	public StatisticsViewModel() {
		super();
	}

	public StatisticsViewModel(List<StatisticDTO> statistics) {
		super();
		this.statistics = statistics;
	}

	public List<StatisticDTO> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<StatisticDTO> statistics) {
		this.statistics = statistics;
	}

	@Override
	public String toString() {
		return "StatisticsViewModel [" + statistics + "]";
	}
}