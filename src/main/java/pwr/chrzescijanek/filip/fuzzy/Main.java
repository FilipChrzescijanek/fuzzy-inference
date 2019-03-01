package pwr.chrzescijanek.filip.fuzzy;

import java.io.IOException;
import java.util.List;
import pwr.chrzescijanek.filip.fuzzyclassifier.Classifier;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.test.TestDataSet;
import pwr.chrzescijanek.filip.fuzzyclassifier.data.test.TestRecord;

/**
 * Main application class.
 */
public class Main {

	/**
	 * Starts the application.
	 *
	 * @param args
	 *            launch arguments
	 * @throws IOException
	 */
	public static void main(final String... args) throws IOException {
		ModelDto model = Utils.loadModel("model-path");
		Classifier classifier = Utils.getClassifier(model);
		List<TestRecord> records = Utils.readCsv("csv-path", model.getAttributes());
		classifier.test(new TestDataSet(model.getAttributes(), records));	
		records.forEach(System.out::println);
	}

}
