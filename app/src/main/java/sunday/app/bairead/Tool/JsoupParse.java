package sunday.app.bairead.Tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sunday.app.bairead.DataBase.SearchBook;

/**
 * Created by sunday on 2016/9/13.
 */


public  class JsoupParse {
    public static final String TAG = "snuday";
    public static final String UTF8 = "UTF-8";
    public static final String GB2312 = "gb2312";

    protected ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    protected Document mDocument;

    public  Document loadCurrentHtml(String filename) {
        try {
            File input = new File(filename);
            mDocument = Jsoup.parse(input, GB2312);
            return mDocument;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> loadSearchHtml(String fileName){
        return new ArrayList<>();
    }



//    public Document loadCurrentHtml(String web,String format) {
//        try {
//            File input = new File(FileManager.FileName);
//            mDocument = Jsoup.parse(input, GB2312, web);
//            return mDocument;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
