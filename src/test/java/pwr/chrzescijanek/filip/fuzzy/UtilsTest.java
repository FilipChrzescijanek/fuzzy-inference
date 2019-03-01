package pwr.chrzescijanek.filip.fuzzy;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import pwr.chrzescijanek.filip.fuzzyclassifier.Classifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.test.TestDataSet;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.test.TestRecord;

public class UtilsTest {

	private static final double DELTA = Math.pow(10, -6);

	@Test
	public void csvParsingTest() throws IOException {
		List<TestRecord> records = Utils.readCsv("src/test/resources/example.csv", Arrays.asList("top", "kek"));
		assertEquals(2, records.size());
		assertEquals(0.0, records.get(0).getAttributes().get("top"), DELTA);
		assertEquals(0.0, records.get(0).getAttributes().get("kek"), DELTA);
		assertEquals(10.0, records.get(1).getAttributes().get("top"), DELTA);
		assertEquals(10.0, records.get(1).getAttributes().get("kek"), DELTA);
	}
	
	@Test
	public void type1ModelParsingTest() throws IOException {
		ModelDto model = Utils.loadModel("src/test/resources/example-type-1.fuzzymodel");
		
		assertCommonModelData(model);

		assertEquals(1, model.getType().intValue());
		
		Map<String, Double> bottomValues = new HashMap<>();
		Map<String, Double> topValues = new HashMap<>();
		
		bottomValues.put("LOW", 0.0);
		bottomValues.put("HIGH", 40.0);
		topValues.put("LOW", 0.0);
		topValues.put("HIGH", 40.0);

		assertEquals(bottomValues, model.getBottomValues());
		assertEquals(topValues, model.getTopValues());
	}
	
	@Test
	public void type2ModelParsingTest() throws IOException {
		ModelDto model = Utils.loadModel("src/test/resources/example-type-2.fuzzymodel");
		
		assertCommonModelData(model);
		
		assertEquals(2, model.getType().intValue());
		
		Map<String, Double> bottomValues = new HashMap<>();
		Map<String, Double> topValues = new HashMap<>();
		
		bottomValues.put("LOW", 0.0);
		bottomValues.put("HIGH", 30.0);
		topValues.put("LOW", 10.0);
		topValues.put("HIGH", 40.0);
		
		assertEquals(bottomValues, model.getBottomValues());
		assertEquals(topValues, model.getTopValues());
	}

	private void assertCommonModelData(ModelDto model) {
		assertEquals(Arrays.asList("LOW", "HIGH"), model.getClazzValues());
		assertEquals(Arrays.asList("top", "kek"), model.getAttributes());
		assertEquals(Arrays.asList("LOW = (top_LOW & kek_LOW)", 
				"HIGH = (top_HIGH & kek_HIGH)"), model.getRules());

		Map<String, Double> means = new HashMap<>();
		Map<String, Double> variances = new HashMap<>();
		
		means.put("top", 5.0);
		means.put("kek", 5.0);
		variances.put("top", 1.0);
		variances.put("kek", 1.0);
		
		assertEquals(means, model.getMeans());
		assertEquals(variances, model.getVariances());
	}
	
	@Test
	public void type1ClassifierTest() throws IOException {
		ModelDto model = Utils.loadModel("src/test/resources/example-type-1.fuzzymodel");
		testClassifier(model);
	}
	
	@Test
	public void type2ClassifierTest() throws IOException {
		ModelDto model = Utils.loadModel("src/test/resources/example-type-2.fuzzymodel");
		testClassifier(model);
	}

	private void testClassifier(ModelDto model) throws IOException {
		Classifier classifier = Utils.getClassifier(model);
		List<TestRecord> records = Utils.readCsv("src/test/resources/example.csv", model.getAttributes());
		classifier.test(new TestDataSet(model.getAttributes(), records));	
		records.forEach(System.out::println);
	}
	
}
