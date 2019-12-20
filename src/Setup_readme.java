import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Setup_readme extends java.lang.Object{

    static List<File> listFile(File dir) {

        List<File> fileTree = new LinkedList();

        // In case of access error, list is null
        if(dir==null||dir.listFiles()==null)
            return fileTree;

        for (File entry : dir.listFiles()) {
            if (entry.isFile()) fileTree.add(entry);
            else fileTree.addAll(listFile(entry));
        }
        return fileTree;
    }

    private static void readComments(String path) throws IOException {

        File root = new File(path);

        List<File> list = listFile(root);

        BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"));

        Pattern pattern = Pattern.compile("(\\/\\/.*?(\\r?\\n|$))|(\\/\\*(?:[\\s\\S]*?)\\*\\/)" +
                "|(\"(?:\\\\[^\\n]|[^\"\"\\n])*\")|(@(?:\"\"[^\"\"]*\"\")+)", Pattern.MULTILINE | Pattern.DOTALL);

        if (list != null) {  // In case of access error, list is null

            for (File f : list) {
                if (f.isFile()) {

                    FileInputStream input = new FileInputStream(f.getAbsoluteFile());

                    // read file with FileChannel
                    FileChannel channel = input.getChannel();

                    // Create a read-only CharBuffer on the file
                    ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
                    CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);

                    Matcher matcher = pattern.matcher(cbuf);

                    List<String> comments = new LinkedList();

                    //find only comments
                    while (matcher.find()) {
                        String match = matcher.group();
                        if (match.startsWith("/*") || match.startsWith("//")){
                            comments.add(match);
                        }
                    }
                    //write comments to file with writer
                    if (comments.size()!=0) {
                        writer.write("======="+f.getName()+"=======");
                        writer.newLine();
                        Iterator<String> iterator = comments.iterator();
                        Integer num = 1; //numbering comments

                        while(iterator.hasNext()){
                            writer.write(num+". "+iterator.next());
                            writer.newLine();
                            num++;
                        }
                        writer.newLine();
                    }
                }
            }
            writer.close();
        }
    }


    public static void main(String[] args) throws IOException {

        String rootFolder = "c:\\JAVA_idea\\it_challenge_readme\\ReadmeBA"; //root folder for comments reading
        readComments(rootFolder);
    }

}
