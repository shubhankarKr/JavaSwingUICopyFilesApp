import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class CopyFileWithThread extends Thread{
	static int count=0;
	static int totalFile=0;
	static int written=0;
	ArrayList<File> source;
	File to;
	ArrayList<String> fileExtentions;
	public CopyFileWithThread(ArrayList<File> source, File to) {
		super();
		this.source = source;
		this.to = to;
	}
	
	public static void copyDirectory(ArrayList<File> sourceLocation, File targetLocationForFile)
			throws IOException {
		if (!targetLocationForFile.exists()) {
			targetLocationForFile.mkdir();
		}
		for (int i = 0; i < sourceLocation.size(); i++) {
			InputStream in =null;
			OutputStream out=null;
				try {
					File newFile = new File(targetLocationForFile, sourceLocation.get(i).getName());
					in = new FileInputStream(sourceLocation.get(i));
					out = new FileOutputStream(newFile);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = in.read(bytes)) > 0) {
						out.write(bytes, 0, length);
					}
					written++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					in.close();
					out.close();
				}
		}
	}

	@Override
	public void run() {
		try {
			count++;
			System.out.println("Started Thread "+count+ " with Record "+source.size());
			copyDirectory(source,to);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
	}
}
