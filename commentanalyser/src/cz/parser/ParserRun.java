package cz.parser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.fit.cvut.ddw.GateClient;

public class ParserRun {
	private List<Entity> ListOfEntities;
	
	public List<Entity> run(String key) {
		GateClient gate = new GateClient();
		String url = "http://www.dhgate.com/wholesale/search.do?act=search&sus=&searchkey=";
		ListOfEntities = new ArrayList<>();
		Parser p = new Parser();
		Document doc = p.getPage( url + key );
		Elements list = doc.select("div.listitem.clearfix");
		int i = 0;
		System.out.println("-*-*-");
		for (Element element : list) {
			System.out.println("----------    " + i);
			Entity e = new Entity();
			e.setLink(p.getLink(element));
			e.setImageLink(p.getImageLink(element));
			e.setFeedbackLink(p.getFeedbacklink(e.getLink()));
			List<Feedback> fList = p.getFeedbackList(e.getFeedbackLink());
			int positive = 0;
			int negative = 0;
			for( Feedback f : fList ){
				try {
					if(!gate.run(f.getFeedback())){
						f.setPositive(false);
						negative++;
					}else{
						positive++;
					}
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
			e.addFeedback(fList);
			if( negative >= positive ){
				e.setPositive(false);
			}
			ListOfEntities.add(e);
			if (i++ == 5) {
				break;
			}
		}
		for (int j = 0; j < 5; j++) {
			System.out.println(ListOfEntities.get(j).getLink());
			System.out.println(ListOfEntities.get(j).getImageLink());
			System.out.println(ListOfEntities.get(j).getFeedbackLink());
			System.out.println("------------------");
		}
		return ListOfEntities;
	}

}
