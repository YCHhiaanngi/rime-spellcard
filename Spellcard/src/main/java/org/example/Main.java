package org.example;

import com.opencsv.CSVWriter;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static final int TOUHOU_NUM = 19;//东方作品总数
    public static void main(String[] args) throws IOException, InterruptedException {
        output();
        writeCSV();
//        getRequire();

    }

    public static void writeCSV() throws IOException {//写入.csv词典文件
        File file = new File("output/spellcard.csv");
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
        String[] records = "词组.拼音".split("\\.");
        csvWriter.writeNext(records);
        for (int i = 6; i <= TOUHOU_NUM; i++) {
            File input = new File("output/th"+i+".txt");
            BufferedReader br = new BufferedReader(new FileReader(input));
            while(true){
                String result = br.readLine();
                if(result == null){
                    break;
                }
                csvWriter.writeNext(result.split("\\,"));
            }
        }
        csvWriter.close();
    }

    public static void output() throws IOException {//将getSpellcards()方法的结果写入txt文档
        for (int i = 6; i <= TOUHOU_NUM; i++) {
            File file = new File("output/th"+i+".txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int j = 0; j < getSpellcards(i).size(); j++) {
//                System.out.println(getSpellcards(i).get(j));
                bw.write((String) getSpellcards(i).get(j));
            }
            bw.close();
            System.out.println("th"+i+"符卡列表已经整理完毕");
        }
    }
    public static void getRequire() throws IOException, InterruptedException {//爬取html文件
        for (int i = 6; i <= TOUHOU_NUM; i++) {//整数作
            String num;
            if(i<10){
                num = "0"+ i;
            }else{
                num = String.valueOf(i);
            }
            String url = "https://www.thpatch.net/wiki/Th"+num+"/Spell_cards";
            File spellcard = new File("html/th"+num+".html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(spellcard));
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            bw.write(document.html());
            bw.close();
            Thread.sleep(10);//下手轻点，别把网站干冒烟了
            System.out.println("th"+num+"的HTML文件已经获取完毕");
        }
    }
    public static ArrayList getSpellcards(int th) throws IOException {//从html文件中获取符卡和使用者并输出
        String num;
        ArrayList output = new ArrayList<String>();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        if(th<10){
            num = "0"+ th;
        }else{
            num = String.valueOf(th);
        }
        String url = "html/th"+num+".html";
        File file = new File(url);
        Document document = Jsoup.parse(file,"utf-8");
        Elements trElements = document.select("tr:has(th[style=text-align:right;], td, td span[lang=ja])");

        for (Element tr : trElements) {
            Element td1 = tr.selectFirst("td");
            Element td2 = tr.selectFirst("td span[lang=ja]");

            if (td1!= null && td2!= null) {
                output.add(td1.text()+","+td2.text()+"\n");
            }
        }
        return output;
    }
}