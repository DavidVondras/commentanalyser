package cz.fit.cvut.ddw;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.DocumentContent;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




public class GateClient {
	String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
    private static SerialAnalyserController annotationPipeline = null;
    private static boolean isGateInitilised = false;
    
    public boolean run( String feedback) throws IOException{
        
        if(!isGateInitilised){
            initialiseGate();            
        }        

        try {
        	int positive = 0;
        	int negative = 0;
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");

            ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
            ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
            ProcessingResource gazetteer = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer");
            File japeOrigFile = new File("C:\\Users\\David\\Documents\\fit\\ddw\\pravidla2.jape");
            java.net.URI japeURI = japeOrigFile.toURI();
            
            FeatureMap transducerFeatureMap = Factory.newFeatureMap();
            try {
                transducerFeatureMap.put("grammarURL", japeURI.toURL());
                transducerFeatureMap.put("encoding", "UTF-8");
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL of JAPE grammar");
                System.out.println(e.toString());
            }
            ProcessingResource japeTransducerPR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerFeatureMap);
            
            annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");

            annotationPipeline.add(documentResetPR);
            annotationPipeline.add(gazetteer);
            annotationPipeline.add(japeTransducerPR);
            
            Document document = Factory.newDocument(feedback);
            Corpus corpus = Factory.newCorpus("");
            corpus.add(document);
            annotationPipeline.setCorpus(corpus);
            annotationPipeline.execute();

            
            for(int i=0; i< corpus.size(); i++){
                Document doc = corpus.get(i);
                AnnotationSet as_default = doc.getAnnotations();

                FeatureMap futureMap = null;
                AnnotationSet annSetTokens = as_default.get("Positive",futureMap);
                System.out.println("Number of Positive annotations: " + annSetTokens.size());
                positive = annSetTokens.size();
                futureMap = null;
                annSetTokens = as_default.get("Negative",futureMap);
                System.out.println("Number of Negative annotations: " + annSetTokens.size());
                negative = annSetTokens.size();
                
                
            }
            return (positive > negative)?true:false;
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private void initialiseGate() {
        
        try {
            File gateHomeFile = new File("C:\\Program Files\\GATE_Developer_8.1");
            Gate.setGateHome(gateHomeFile);
            File pluginsHome = new File("C:\\Program Files\\GATE_Developer_8.1\\plugins");
            Gate.setPluginsHome(pluginsHome);            
            Gate.init();
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new URL("file:///C:/Program Files/GATE_Developer_8.1/plugins/ANNIE/");
            register.registerDirectories(annieHome);
            isGateInitilised = true;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
