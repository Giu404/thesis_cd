How to use

Variante 1:
- Es wird ein Objekt der ConfluenceConnector Klasse erstellt

  	ConfluenceConnector con = new ConfluenceConnector();

- Es wird die Methode startConnector ausgeführt

  	con.startConnector();

- Nun werden die gebrauchten Studiengang-Maps angefordert

  	con.getStudyMapByTitle("Software Engineering");
  	con.getStudyMapByID("3221163");

Variante 2:
- Es wird ein Objekt der ConfluenceConnector Klasse erstellt

	ConfluenceConnector con = new ConfluenceConnector();

- Erstellen einer ArrayList des Typs String und hinzufügen der Werte

	ArrayList<String> params = new ArrayList<String>();
	params.add("Abschluss"); 
	params.add("Studienform");
	...

- Es wird die Methode startConnector mit ArrayList als Parameter ausgeführt

	con.startConnector(params);

- Nun kann man auf die einzelnen Studiengang-Maps zugreifen

	con.getStudyMapByTitle("Software Engineering");
  	con.getStudyMapByID("3221163");