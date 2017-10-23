import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {

	private String path;
	private boolean appendToFile = false;
	
	public WriteFile(String filePath) {
		path = filePath;
	}
	
	public WriteFile(String filePath, boolean appendValue) {
		path = filePath;
		appendToFile = appendValue;
	}
	
	public static void writeFile( String path, String content) {
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
}
