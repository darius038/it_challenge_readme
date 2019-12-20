import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Setup_readme extends java.lang.Object{

    static List<File> listFileTree(File dir) {
        List<File> fileTree = new LinkedList();
        if(dir==null||dir.listFiles()==null){
            return fileTree;
        }
        for (File entry : dir.listFiles()) {
            if (entry.isFile()) fileTree.add(entry);
            else fileTree.addAll(listFileTree(entry));
        }
        return fileTree;
    }

    static void readComments(String path) throws IOException {

        File root = new File(path);
        File[] list = root.listFiles();

        System.out.println(Arrays.toString(list));

        Pattern pattern = Pattern.compile("(\\/\\/.*?(\\r?\\n|$))|(\\/\\*(?:[\\s\\S]*?)\\*\\/)" +
                "|(\"(?:\\\\[^\\n]|[^\"\"\\n])*\")|(@(?:\"\"[^\"\"]*\"\")+)", Pattern.MULTILINE | Pattern.DOTALL);

        if (list != null) {  // In case of access error, list is null
            for (File f : list) {
                if (f.isFile()) {

                    System.out.println(f.getAbsoluteFile());

                    FileInputStream input = new FileInputStream(f.getAbsoluteFile());
                    FileChannel channel = input.getChannel();

                    // Create a read-only CharBuffer on the file
                    ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
                    CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);

                    Matcher matcher = pattern.matcher(cbuf);

                    while (matcher.find()) {
                        String match = matcher.group();
                        if (match.startsWith("/*") || match.startsWith("//")){
                        System.out.println(match);}
                    }

                }
            }
        }
    }


    public static void main(String[] args) throws IOException {

        String rootFolder = "c:\\JAVA_idea\\ba_it_challange\\ReadmeBA";

//        File root = new File(rootFolder);
//
//        List<File> filesList = Setup_readme.listFileTree(root);
//
//        System.out.println("List of all files under " + rootFolder);
//        System.out.println("------------------------------------");
//        filesList.forEach(System.out::println);

        Setup_readme.readComments(rootFolder); // this will take a while to run!
    }


}
