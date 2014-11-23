import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.util.FileManager;

public class Main {
	
	public static void main(String args[]) throws Exception
	{
		
		try {
			sparqlUpdateFromFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sparqlTest();
		sparqlTest2();
		
	}
	
	static void sparqlTest()
	{
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		Model model = FileManager.get().loadModel("C:/Users/Shwevy/Desktop/university.owl");
		
		String queryString =
				"PREFIX uni: <http://people.brunel.ac.uk/~csstnns/university.owl#>" +
				"SELECT * {" +
				"?x uni:studies uni:M201" +
				"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		qexec.close();

	}
	
	static void sparqlTest2()
	{
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		Model model = FileManager.get().loadModel("C:/Users/Shwevy/Desktop/university.owl");
		
		String queryString =
				"PREFIX uni: <http://people.brunel.ac.uk/~csstnns/university.owl#>" +
				"SELECT * {" +
				"?x uni:teaches uni:M201" +
				"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		ResultSetFormatter.out(System.out, results, query);

		qexec.close();

	}
	
	static void sparqlUpdateFromFile() throws Exception
	{
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		Model model = FileManager.get().loadModel("C:/Users/Shwevy/Desktop/university.owl", "RDF/XML");
		// Prepare your update...
		
		File queryStringFile = new File("C:/Users/Shwevy/Desktop/sparqlExample.txt");
	    String queryString = extractText(queryStringFile);
		GraphStore graphStore = GraphStoreFactory.create(model) ;
		
		UpdateAction.parseExecute(queryString, graphStore) ;

		
		// Save the updated model 
		model.write(new FileOutputStream("C:/Users/Shwevy/Desktop/university.owl"), "RDF/XML");
	
	}
	
	static void sparqlUpdateFromString() throws FileNotFoundException
	{
		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		Model model = FileManager.get().loadModel("C:/Users/Shwevy/Desktop/university.owl", "RDF/XML");
		// Prepare your update...
		String queryString =
				"PREFIX uni: <http://people.brunel.ac.uk/~csstnns/university.owl#>" +
					"PREFIX extra: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
					"PREFIX extra2: <http://www.w3.org/2002/07/owl#>" +
					"INSERT DATA {uni:Student4 extra:type extra2:NamedIndividual;" + 
					"uni:studies uni:M201."+
					"}";
		GraphStore graphStore = GraphStoreFactory.create(model) ;
		
		UpdateAction.parseExecute(queryString, graphStore) ;

		
		// Save the updated model 
		model.write(new FileOutputStream("C:/Users/Shwevy/Desktop/university.owl"), "RDF/XML");
	
	}

//Taken from http://ocw.uc3m.es/ OpenCourseWare
private static String extractText(File f) throws Exception {

       StringBuilder contents = new StringBuilder();

       try {
           BufferedReader input = new BufferedReader(new FileReader(f));
           try {
               String line = null;
               while ((line = input.readLine()) != null) {
                   contents.append(line);
                   contents.append(System.getProperty("line.separator"));
               }
           } finally {
               input.close();
           }
       } catch (IOException ex) {
           ex.printStackTrace();
       }

       return contents.toString().trim();
   }
}
