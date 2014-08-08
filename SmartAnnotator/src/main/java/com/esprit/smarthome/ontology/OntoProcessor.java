package com.esprit.smarthome.ontology;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

@Stateless
public class OntoProcessor implements IOntoProcessor {
	final static String NS = "http://elite.polito.it/ontologies/dogont.owl#";
	final static String RDFPREFIX = "PREFIX rdf: <" + RDF.getURI() + ">";
	final static String RDFSPREFIX = "PREFIX rdfs: <" + RDFS.getURI() + ">";
	final static String OWLPREFIX = "PREFIX owl: <" + OWL.getURI() + ">";
	final static String XSDPREFIX = "PREFIX xsd: <" + XSD.getURI() + ">";
	final static String BASEPREFIX = "PREFIX dog: <http://elite.polito.it/ontologies/dogont.owl#> ";
	String[] stopwords = { "Class", "NamedIndividual", "Resource", "Thing",
			"Controllable", "BuildingThing", "Nothing" };
	private OntModel model;
	static final Log log = LogFactory.getLog(OntoProcessor.class);
	List<String> myAnnotations;
	List<String> allAnnotations;

	public void init() {
		myAnnotations = new ArrayList<String>();
		allAnnotations = new ArrayList<String>();
		try {
			model = ModelFactory
					.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

			InputStream is = OntoProcessor.class.getClassLoader()
					.getResourceAsStream("myont.owl");
			model.read(is, NS);
		} catch (Exception e) {
			log.error("Fichier d'ontologie non trouvé");
			;
		}

	}

	public List<String> ontoQuery(String queryParam) {
		init();
		String queryString = "select DISTINCT ?bth where {dog:" + queryParam
				+ " rdf:type ?bth. " + " FILTER (!isBlank(?bth))}";
		String query = RDFPREFIX + RDFSPREFIX + XSDPREFIX + OWLPREFIX
				+ BASEPREFIX + queryString;
		Query queryObject = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(queryObject, model);

		for (ResultSet rs = qe.execSelect(); rs.hasNext();) {
			QuerySolution binding = rs.nextSolution();
			myAnnotations.add(binding.get("?bth").toString().split("#")[1]);
		}
		qe.close();
		myAnnotations.removeAll(Arrays.asList(stopwords));
		log.info("My Annotations: " + myAnnotations);
		return myAnnotations;

	}

	public List<String> getAllAnnotations() {
		init();
		String queryString = "select DISTINCT ?bth where {?bth rdfs:subClassOf+ dog:Controllable.  FILTER (!isBlank(?bth))}";
		String query = RDFPREFIX + RDFSPREFIX + XSDPREFIX + OWLPREFIX
				+ BASEPREFIX + queryString;
		Query queryObject = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(queryObject, model);

		for (ResultSet rs = qe.execSelect(); rs.hasNext();) {
			QuerySolution binding = rs.nextSolution();
			String a = binding.get("?bth").toString().split("#")[1];
			allAnnotations.add(a);
		}
		allAnnotations.removeAll(Arrays.asList(stopwords));
		return allAnnotations;

	}

	public void addIndividual(String classe, String instanceName) {
		OntClass c = model.getOntClass(NS + classe);
		System.out.println("la classe est" + c);
		Individual ind = model.createIndividual(NS + instanceName, c);
		try {
			PrintStream p = new PrintStream("C:\\sem\\test\\ont.owl");
			model.writeAll(p, "RDF/XML", null);
			p.close();
		} catch (FileNotFoundException e) {
			log.error("Fichier d'ontologie non trouvé");
			;
		}
		;
	}
}
