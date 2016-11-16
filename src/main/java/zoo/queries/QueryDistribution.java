package zoo.queries;

import zoo.reader.JSONReader;
import zoo.reader.QueryStructure;

public class QueryDistribution extends Thread {
	private static final JSONReader jsonReader = new JSONReader();
	private String queryFile;
	
	public QueryDistribution(String queryFile) {
		this.queryFile = queryFile;
	}

	public void run () {
		QueryStructure queryDescription = jsonReader.readJson(queryFile);
		System.out.println("Deploying query " + queryDescription.getName());
		System.out.println("Query " + queryDescription.toString() + " needs to be distributed");
	}
}
