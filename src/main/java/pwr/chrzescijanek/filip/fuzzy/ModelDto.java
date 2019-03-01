package pwr.chrzescijanek.filip.fuzzy;

import java.util.List;
import java.util.Map;

public class ModelDto {

	private final Integer type;
	private final List<String> clazzValues;
	private final List<String> attributes;
	private final List<String> rules;
	private final Map<String, Double> means;
	private final Map<String, Double> variances;
	private final Map<String, Double> bottomValues;
	private final Map<String, Double> topValues;
	
	public ModelDto(Integer type, List<String> clazzValues, List<String> attributes, List<String> rules, Map<String, Double> means,
			Map<String, Double> variances, Map<String, Double> bottomValues, Map<String, Double> topValues) {
		this.type = type;
		this.clazzValues = clazzValues;
		this.attributes = attributes;
		this.rules = rules;
		this.means = means;
		this.variances = variances;
		this.bottomValues = bottomValues;
		this.topValues = topValues;
	}

	public Integer getType() {
		return type;
	}

	public List<String> getClazzValues() {
		return clazzValues;
	}
	
	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getRules() {
		return rules;
	}

	public Map<String, Double> getMeans() {
		return means;
	}

	public Map<String, Double> getVariances() {
		return variances;
	}

	public Map<String, Double> getBottomValues() {
		return bottomValues;
	}

	public Map<String, Double> getTopValues() {
		return topValues;
	}
	
}
