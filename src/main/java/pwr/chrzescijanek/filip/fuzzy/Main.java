package pwr.chrzescijanek.filip.fuzzy;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addRequiredOption( "d", "data", true, "path to CSV file with test data" );
		options.addRequiredOption( "m", "model", true, "path to FUZZYMODEL file with fuzzy logic model data" );

		try {
		    CommandLine line = parser.parse( options, args );
			
			ModelDto model = Utils.loadModel(line.getOptionValue("model"));
			Classifier classifier = Utils.getClassifier(model);
			List<TestRecord> records = Utils.readCsv(line.getOptionValue("data"), model.getAttributes());
			classifier.test(new TestDataSet(model.getAttributes(), records));	
			records.forEach(System.out::println);
		} catch( ParseException exp ) {
		    System.out.println( "Exception: " + exp.getMessage() );
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp("fuzzy-inference", options);
		}
	}

}
