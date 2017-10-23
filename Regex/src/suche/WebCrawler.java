package suche;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class WebCrawler {
	
	 final int max = 5;
	 int urlCount = 0;
	
	static HashMap<String, Integer> map = new HashMap<>();
	
	public WebCrawler() {
		
		try {
			
			  //config das indizieren
			  String indexPath = "D:\\suchindex";
		      Directory dir = FSDirectory.open(Paths.get(indexPath));
		      Analyzer analyzer = new StandardAnalyzer();
		      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		      iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		      IndexWriter writer = new IndexWriter(dir, iwc);
		      
		      try {
					urlCount = 0;
					getUrl("http://www.spiegel.de/", writer);
					urlCount = 0;
					getUrl("http://www.heise.de/", writer);
					urlCount = 0;
					getUrl("http://www.golem.de/", writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					writer.close();
				}
		
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
		

	
	
	public  String getUrl(String weburl, IndexWriter writer) throws IOException{
		
		if( urlCount ++ > max) {return null; }
		
		if (map.get(weburl) != null) return null;
		
		map.put(weburl, new Integer(0));
		
		System.setProperty("http.proxyHost", "10.232.1.1");
		System.setProperty("http.proxyPort", "8877");
		
		System.setProperty("https.proxyHost", "10.232.1.1");
		System.setProperty("https.proxyPort", "8877");
		
		Document doc = Jsoup.connect(weburl)
				.userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
					.get();
		
		IndexFiles.indexDoc(writer, weburl, doc.text(), 0);
		indexWebDoc(doc.text(), weburl);
		
		ArrayList<String> list = getLinks(doc.html(), weburl);
		
		for (String u : list) getUrl( u, writer);
		
		return doc.text();
	}
	
	public void indexWebDoc(String txt, String url) {
		
		
		System.out.println(urlCount + " indexiere " + url + " mit Buchstaben: " + txt.length());
	}


public static ArrayList<String> getLinks(String html, String weburl) throws IOException{

		ArrayList<String> list = new ArrayList<String>();
		
		
		Document doc = Jsoup.parse(html);
		
		Elements links = doc.select("a[href]"); // a with href<br />
		for ( Element u : links ) {
		String s = u.attr("href");

		//System.out.println(s);
		if ( s.equals("/") ) continue;
		if ( s.equals("./") ) continue;
		if ( s.equals("#") ) continue; //sprung marke
		if ( s.equals("mailto") ) continue; //mailto links
		
		 // ist es ein absoluter link od. relativer
		if ( s.indexOf("http") < 0) {
			// mach elink absolut
			if ( weburl.endsWith("/") ) {
				if ( !  s.startsWith("/") ) list.add(weburl + s);
			} else list.add(weburl + "/" + s);
		}
		else list.add(s);
	}
		return list;
	}


	public static void main (String[] args) {
		
		new WebCrawler();
	
	}
}
