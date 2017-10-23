
public class Parser {

	enum isHtml {
		
		outSide, inSide
	}
	
	static isHtml status = isHtml.outSide;
	
	final static String teststring = "<html>Ich bin inne</html><h1 class='sollte unsichtbar sein'>das hier ist sichtbar</h1>Wieder inne";
	
	public Parser() {
		
	}
	
	static String parseHtml (String html) {
		
		StringBuilder sammel = new StringBuilder();
		
		for (int i = 0; i < html.length(); i++) {
			char c = html.charAt(i);
			
			
			if(c == '<') {	
				status = isHtml.inSide;
				
			} 
			
			
			if(status == isHtml.outSide) {
				sammel.append(c);
			}
			
			if (c == '>') {
				status = isHtml.outSide;
				
			}
		}
		
		
		
		return sammel.toString();
	}
	
	public static String ergebniss = "";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ergebniss = parseHtml(teststring);
		
		System.out.println(ergebniss);
		
	}

}
