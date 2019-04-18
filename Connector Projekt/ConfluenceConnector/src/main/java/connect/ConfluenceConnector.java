package connect;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * @author Giuliano Gozza
 * 
 * Diese Klasse enthält die Hauptlogik des Confluence Connectors.
 * Es wird die Startmethode ausgeführt um die Datenabfrage zu initiieren.
 *
 */
public class ConfluenceConnector {
	private ArrayList<LinkedHashMap<String, String>> studyMaps;
	
	/**
	 * Diese Methode muss gestartet werden, damit der API-Aufruf erfolgen kann.
	 * Es ist die Variante ohne Parameter.
	 */
	public void startConnector() {
		HttpResponse<String> response = getResponse();
		JsonElement jelement = new JsonParser().parse(response.getBody());
		JsonObject jsonRoot = jelement.getAsJsonObject();
		
		/**
		 * Die Keys werden hier geholt
		 */
		JsonArray keyArray = jsonRoot.getAsJsonArray("renderedHeadings");
		
		/**
		 * Werte der Studiengänge werden geholt
		 * Überprüfe erste Stelle in jedem Objekt auf Leere
	     * Wenn dem nicht so ist füge die Details der einzelnen studyMaps hinzu
		 */
	    this.studyMaps = new ArrayList<LinkedHashMap<String,String>>();
	    int valueMapCounter = 0;
	    for (int i = 0; i < jsonRoot.getAsJsonArray("detailLines").size(); i++) {
			if(!((JsonObject) jsonRoot.getAsJsonArray("detailLines").get(i)).get("details").getAsJsonArray().get(0).getAsString().isEmpty()) {
				this.studyMaps.add(new LinkedHashMap<String,String>());
				this.studyMaps.get(valueMapCounter).put("ID", ((JsonObject)jsonRoot.getAsJsonArray("detailLines").get(i)).get("id").getAsString());
				this.studyMaps.get(valueMapCounter).put("Titel", ((JsonObject)jsonRoot.getAsJsonArray("detailLines").get(i)).get("title").getAsString());
				for(int j = 0; j <= 13; j++) {
					this.studyMaps.get(valueMapCounter).put(keyArray.get(j).getAsString(), ((JsonArray) ((JsonObject) jsonRoot.getAsJsonArray("detailLines").get(i)).get("details")).get(j).getAsString());
				}
				valueMapCounter += 1;
			}
		}
	}
	
	/**
	 * Die überladene Variante der startConnector Methode nimmt eine Liste
	 * mit den Parametern für den API-Aufruf
	 * 
	 * @param callParams Diese Liste enthält alle Parameter 
	 * die beim API-Aufruf verwendet werden sollen
	 */
	public void startConnector(ArrayList<String> callParams) {
		HttpResponse<String> response = getResponse(callParams);
		JsonElement jelement = new JsonParser().parse(response.getBody());
		JsonObject jsonRoot = jelement.getAsJsonObject();
		
		/*
		 * Die Keys werden hier geholt
		 */
		JsonArray keyArray = jsonRoot.getAsJsonArray("renderedHeadings");
		
		/*
		 * Werte der Studiengänge werden geholt
		 * Überprüfe erste Stelle in jedem Objekt auf Leere
	     * Wenn dem nicht so ist füge die Details der einzelnen studyMaps hinzu
		 */
	    this.studyMaps = new ArrayList<LinkedHashMap<String,String>>();
	    int valueMapCounter = 0;
	    for (int i = 0; i < jsonRoot.getAsJsonArray("detailLines").size(); i++) {
			if(!((JsonObject) jsonRoot.getAsJsonArray("detailLines").get(i)).get("details").getAsJsonArray().get(0).getAsString().isEmpty()) {
				this.studyMaps.add(new LinkedHashMap<String,String>());
				this.studyMaps.get(valueMapCounter).put("ID", ((JsonObject)jsonRoot.getAsJsonArray("detailLines").get(i)).get("id").getAsString());
				this.studyMaps.get(valueMapCounter).put("Titel", ((JsonObject)jsonRoot.getAsJsonArray("detailLines").get(i)).get("title").getAsString());
				for(int j = 0; j <= 13; j++) {
					this.studyMaps.get(valueMapCounter).put(keyArray.get(j).getAsString(), ((JsonArray) ((JsonObject) jsonRoot.getAsJsonArray("detailLines").get(i)).get("details")).get(j).getAsString());
				}
				valueMapCounter += 1;
			}
		}
	}
	
	/**
	 * Mit dieser Methode wird ein Studiengang mithilfe des Titels zurückgegeben
	 * 
	 * @param studyTitle enthält den Titel des gewünschten Studiengangs
	 * @return
	 */
	public LinkedHashMap<String,String> getStudyMapByTitle(String studyTitle){
		for (int i = 0; i < this.studyMaps.size(); i++) {
			if(this.studyMaps.get(i).get("Titel").contains(studyTitle)) {
				return this.studyMaps.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Mit dieser Methode wird ein Studiengang mithilfe der ID zurückgegeben
	 * 
	 * @param studyID enthält die ID des gewünschten Studiengangs
	 * @return
	 */
	public LinkedHashMap<String,String> getStudyMapByID(String studyID){
		for (int i = 0; i < this.studyMaps.size(); i++) {
			if(this.studyMaps.get(i).get("ID").equals(studyID)) {
				return this.studyMaps.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Die Variante von getResponse ohne Parameter
	 * Hier werden standardmäßig die 14 Parameter, 
	 * die Stand Jetzt benutzt werden übermittelt
	 * 
	 * @return
	 */
	private HttpResponse<String> getResponse(){
		HttpResponse<String> response;
		try {
			response = Unirest.get("http://confluence-student.it.hs-heilbronn.de/rest/"
					+ "masterdetail/1.0/detailssummary/lines?cql=type%3Dpage&spaceKey=32211166&"
					+ "headings=Abschluss%2CStudienform%2CStudiendauer%20(Semester)%2CECTS%2CLehrsprache%2C"
					+ "Start%20zum%2CCampus%2CBewerbungsschluss%2CBeschreibung%2C"
					+ "Zulassungsvorraussetzungen%20und%20Anforderungen%2CNumerus%20Clausus%2CInteressen%2C"
					+ "Berufsfelder%2CLink%20zum%20Webauftritt")
					.header("Authorization", "Basic Z2dvenphOkdnMjNubTAyeHkxUjc=")
					.header("cache-control", "no-cache")
					.asString();
			return response;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Die überladene Variante von getResponse mit einem Parameter
	 * 
	 * @param callParams Enthält die Parameter für den API-Aufruf
	 * @return
	 */
	private HttpResponse<String> getResponse(ArrayList<String> callParams){
		String firstPartURL = "http://confluence-student.it.hs-heilbronn.de/rest/"
				+ "masterdetail/1.0/detailssummary/lines?cql=type%3Dpage&spaceKey=32211166&"
				+ "headings=";
		String secondPartURL = "";
		
		for (int i = 0; i < callParams.size(); i++) {
			if(callParams.get(i).contains(" ")){
				callParams.set(i, callParams.get(i).replace(" ", "%20"));				
			}
			secondPartURL += callParams.get(i);
			secondPartURL += "%2C";
		}
		
		HttpResponse<String> response;
		try {
			response = Unirest.get(firstPartURL+secondPartURL)
					.header("Authorization", "Basic Z2dvenphOkdnMjNubTAyeHkxUjc=")
					.header("cache-control", "no-cache")
					.asString();
			return response;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}

}