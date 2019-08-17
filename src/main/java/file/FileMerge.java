package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileMerge {

    /**
     * 将目录下的多个文件合并为一个文件
     * @param inputDir
     * @param outputFile
     * @return
     */
    public static boolean mergeFiles(String inputDir, String outputFile){
        try {
            Path path = Paths.get(inputDir);
            DirectoryStream<Path> paths = Files.newDirectoryStream(path);
            System.out.println(Files.size(path));

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            String line = null;
            for (Path p : paths) {
                System.out.println("process file "+p.toString());
                BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(p.toString()));
                while((line=bufferedReader.readLine()) != null)
                    bufferedWriter.write(line+"\n");
                bufferedWriter.flush();
                bufferedReader.close();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean mergeLinesByID(String inputFilePath, String outputFilePath){
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
            String line = null;
            String[] tokens = null;
            HashMap<String,List<String>> newsThemeMap = new HashMap<>();
            while((line = br.readLine()) != null){
                tokens = line.trim().split(",");
                if(tokens.length != 3){
                    System.out.println("Error Line : "+line.trim());
                    continue;
                }
                String key = tokens[0].toUpperCase();
                String[] tmp = key.split("_");
                key = tmp[0]+"\t"+tmp[tmp.length-1];

                String theme = tokens[1];
                if(newsThemeMap.containsKey(key)) {
                    newsThemeMap.get(key).add(theme);
                }
                else{
                    List<String> themeList = new ArrayList<>();
                    themeList.add(theme);
                    newsThemeMap.put(key, themeList);
                }
            }

            StringBuffer sb = new StringBuffer();
            for(String key : newsThemeMap.keySet()){
                for(String theme : newsThemeMap.get(key))
                    sb.append(theme+"\t");
                bw.write(key+"\t"+sb.toString().trim().replace("\t","，")+"\n");
                sb = new StringBuffer();
            }
            bw.flush();
            bw.close();
            br.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean mergeFiles(String inputFile1, String inputFile2, String outputFile){
        try{
            BufferedReader br1 = new BufferedReader(new FileReader(inputFile1));
            BufferedReader br2 = new BufferedReader(new FileReader(inputFile2));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

            String line = null;
            String[] tokens = null;
            HashMap<String, String> resultMap = new HashMap<>();
            HashMap<String, String> titleMap = new HashMap<>();
            while((line = br1.readLine()) != null){
                tokens = line.trim().split("\t");
                if(tokens.length < 3){
                    System.out.println("Error Line : "+line.trim());
                    continue;
                }
                String key = tokens[0];
                String title = tokens[1];
                String value = tokens[2];
                for(int i=3; i<tokens.length; i++)
                    value += "\t"+tokens[i];

                resultMap.put(key,value);
                if(title!=null && !title.isEmpty())
                    titleMap.put(key,title);
            }

            while((line = br2.readLine()) != null){
                tokens = line.trim().split("\t");
                if(tokens.length < 3){
                    System.out.println("Error Line : "+line.trim());
                    continue;
                }
                String key = tokens[0];
                String title = tokens[1];
                String value = tokens[2];
                for(int i=3; i<tokens.length; i++)
                    value += "\t"+tokens[i];

                if(title!=null && !title.isEmpty())
                    titleMap.put(key,title);

                if(resultMap.containsKey(key)) {
                    bw.write(key + "\t" + title + "\t" +(resultMap.get(key)+"\t"+value).trim().replaceAll("\t+","\t") + "\n");
                    resultMap.remove(key);
                }
                else
                    System.out.println("not found key "+line.trim());
            }

            for(String key : resultMap.keySet()){
                bw.write(key + "\t" + titleMap.get(key) + "\t" + resultMap.get(key).trim().replaceAll("\t+","\t") + "\t-\n");
            }
            bw.flush();
            bw.close();
            br1.close();
            br2.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void mergeFields(String inputFilePath, String outputFilePath){
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
            String line = null;
            String[] tokens = null;
            while((line = br.readLine()) != null){
                tokens = line.trim().split("\t");
                if(tokens.length != 9){
                    System.out.println("Error Line = "+line.trim());
                    continue;
                }
                ArrayList<String> list = new ArrayList<>();
                for(int i=3; i<=7; i++){
                    if(!tokens[i].equalsIgnoreCase("-") && !list.contains(tokens[i]))
                        list.add(tokens[i]);
                }
                StringBuffer sb = new StringBuffer();
                for(String tag : list)
                    sb.append(tag+"\t");
                bw.write(tokens[0]+"\t"+tokens[1]+"\t"+tokens[2]+"\t"+sb.toString().trim().replace("\t","，")+"\t"+tokens[8]+"\n");
            }
            bw.flush();
            bw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
