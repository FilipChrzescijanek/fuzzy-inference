package pwr.chrzescijanek.filip.fuzzy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import com.google.gson.Gson;
import com.opencsv.CSVReaderHeaderAware;

import pwr.chrzescijanek.filip.fuzzyclassifier.Classifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.raw.Stats;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.test.TestRecord;
import pwr.chrzescijanek.filip.fuzzyclassifier.model.Rule;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.one.BasicTypeOneDefuzzifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.one.CustomTypeOneDefuzzifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.one.SimpleTypeOneClassifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.one.TypeOneModel;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.two.BasicTypeTwoDefuzzifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.two.CustomTypeTwoDefuzzifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.two.SimpleTypeTwoClassifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.type.two.TypeTwoModel;

/**
 * Provides utility methods for handling controllers.
 */
public final class Utils {

	private Utils() {
	}

	public static ModelDto loadModel(String filePath) throws IOException {
		return loadModel(new File(filePath));
	}

	public static ModelDto loadModel(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String json = br.lines().collect(Collectors.joining());
			return new Gson().fromJson(json, ModelDto.class);
		}
	}

	public static Classifier getClassifier(ModelDto model) {
		List<Rule> rules = getRules(model.getRules());
		Map<String, Double> bottomValues = model.getBottomValues();
		Map<String, Double> topValues = model.getTopValues();

		return model.getType() == 1 ? new SimpleTypeOneClassifier(
				new TypeOneModel(rules, model.getClazzValues(), new Stats(model.getMeans(), model.getVariances())),
				bottomValues != null ? new CustomTypeOneDefuzzifier(bottomValues)
						: new BasicTypeOneDefuzzifier(model.getClazzValues()))
				: new SimpleTypeTwoClassifier(
						new TypeTwoModel(rules, model.getClazzValues(),
								new Stats(model.getMeans(), model.getVariances())),
						bottomValues != null ? new CustomTypeTwoDefuzzifier(bottomValues, topValues)
								: new BasicTypeTwoDefuzzifier(model.getClazzValues()));
	}

	private static List<Rule> getRules(List<String> inputs) {
		List<Rule> rules = new ArrayList<>();
		for (String input : inputs) {
			String[] parts = input.split("\\s*=\\s*");
			String clazz = parts[0];
			String expression = parts[1];
			Expression<String> expr = RuleSet.simplify(ExprParser.parse(expression));
			rules.add(new Rule(expr, clazz));
		}
		return rules;
	}

	public static List<TestRecord> readCsv(String filePath, List<String> attributes) throws IOException {
		try (CSVReaderHeaderAware csv = new CSVReaderHeaderAware(new BufferedReader(new FileReader(new File(filePath))))) {
			List<TestRecord> records = new ArrayList<>();
			Map<String, String> values = null;
			while ((values = csv.readMap()) != null) {
				records.add(getTestRecord(attributes, values));
			}
			return records;
		}
	}

	private static TestRecord getTestRecord(List<String> attributes, Map<String, String> values) {
		return new TestRecord(attributes.parallelStream()
				.collect(Collectors.toMap(Function.identity(), a -> Double.parseDouble(values.get(a)))));
	}

}
