package fiskkit.url2csv;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import com.fiskkit.Fetcher;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Main 
{
	
	
			
	
	
	public static final void main(String[] args) throws Exception
	{
		
		if (args.length != 2)
		{
			usage();
		}
		
		String url = args[0];
		String csvFile = args[1];
		
		
		List<String> paragraphList = Fetcher.pullAndExtract(url);
		
		StringBuilder stringBuilder = new StringBuilder("");
		for (String paragraph: paragraphList)
		{
			stringBuilder.append(paragraph);
			stringBuilder.append('\n');
		}
		String article = stringBuilder.toString();
		
		
		InputStream modelStream = new Main().getClass().getResourceAsStream("en-sent.bin");
		SentenceModel model = new SentenceModel(modelStream);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		
		String sentences[] = sdetector.sentDetect(article);
		
		PrintStream ps = new PrintStream(csvFile);
		 
		for (String sentence: sentences)
		{
			StringBuilder line = new StringBuilder("\"");
			for (int c = 0; c < sentence.length(); c ++)
			{
				char character = sentence.charAt(c);
				if (character == '\"')
				{
					line.append('\"');
				}
				line.append(character);
				
			}
			line.append("\"");
			ps.println(line);
			
		}
		ps.close();
		
		
		
	}
	
	
	public static void usage()
	{
		System.out.println("Usage: java fiskkit.article2csv.Main url csv-file-name");
		System.out.println("CLASSPATH should contain Fetcher.jar and opennlp-tools-1.6.0.jar");
		System.out.println("Example: java fiskkit.article2csv.Main " +
				"http://robertreich.org/post/133414962975 /tmp/sentences.csv");
		
		System.exit(1);
	}
	
	
	
}
