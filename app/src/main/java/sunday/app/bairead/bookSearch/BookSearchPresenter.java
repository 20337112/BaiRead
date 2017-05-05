package sunday.app.bairead.bookSearch;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Response;
import sunday.app.bairead.data.setting.BookInfo;
import sunday.app.bairead.download.OKhttpManager;
import sunday.app.bairead.parse.ParseBaiduSearch;
import sunday.app.bairead.utils.FileManager;
import sunday.app.bairead.utils.ThreadManager;

/**
 * Created by sunday on 2017/3/6.
 */

public class BookSearchPresenter {
    public static final String fileName = "searchHistory.txt";

    public interface IBookSearchListener{
        void historyAddFinish(String name);
        void historyLoadFinish(ArrayList<String> list);
        void bookSearchStart();
        void bookSearching(BookInfo bookInfo);
        void bookSearchFinish();
        void bookSearchError();

    }

    private Handler handler = new Handler();
    private IBookSearchListener bookSearchListener;
    private ArrayList<String> historyList;

    public BookSearchPresenter(IBookSearchListener bookSearchListener){
        this.bookSearchListener = bookSearchListener;
    }

    public void readSearchHistory(Context context) {
        File file = context.getCacheDir();
        if (!file.exists()) {
            file.mkdirs();
        }
        final String fullName = file.getAbsolutePath() + "/" + fileName;
        ThreadManager.getInstance().work(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> list = FileManager.readFileByLine(fullName);
                Collections.reverse(list);
                historyList = list;
                runMainUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookSearchListener.historyLoadFinish(historyList);
                    }
                });

            }
        });
    }

    public void addSearchHistory(Context context,final String name){
        if(historyList.contains(name)){
        }else {
            final String fullName = context.getCacheDir().getAbsolutePath() + "/" + fileName;
            ThreadManager.getInstance().work(new Runnable() {
                @Override
                public void run() {
                    FileManager.writeFileByLine(fullName, name);
                    runMainUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookSearchListener.historyAddFinish(name);
                        }
                    });

                }
            });
        }
    }

    public void clearHistory(Context context){
        File file = context.getCacheDir();
        final String fullName = file.getAbsolutePath() + "/" + fileName;
        if(file.exists()) {
            ThreadManager.getInstance().work(new Runnable() {
                @Override
                public void run() {
                    FileManager.deleteFile(fullName);
                }
            });
        }
    }


    private ArrayList<String> baiduSearchList;
    private ArrayList<BookInfo> searchBookInfoList;
    private AtomicInteger atomicInteger;

    private void checkStart(int count){
        atomicInteger = new AtomicInteger(count);
    }

    private void checkFinish(final BookInfo bookInfo){
        int i = atomicInteger.decrementAndGet();
        if( i <= 0){
            runMainUiThread(new Runnable() {
                @Override
                public void run() {
                    bookSearchListener.bookSearchFinish();
                }
            });

        }else if(bookInfo != null && bookInfo.bookChapter != null && bookInfo.bookChapter.getChapterCount() !=0){

            runMainUiThread(new Runnable() {
                @Override
                public void run() {
                    bookSearchListener.bookSearching(bookInfo);
                }
            });
        }
    }

    /**
     * 将百度搜索结果的网站长地址生成临时文件名称
     * */
    private static String getBaiduLinkCodeString(String link){
        String[] cs = link.split("\\.");
        String cs2 = cs[cs.length-1];
        return cs2.substring(cs2.length()-20,cs2.length()-1);
    }

    private void findRealLink(){
        searchBookInfoList = new ArrayList<>();
//        BookDownLoad bookDownLoad = new BookDownLoad();
//
//        checkStart(baiduSearchList.size());
//        for(final String string : baiduSearchList){
//            final String name = getBaiduLinkCodeString(string);
//            bookDownLoad.updateBookInfoAsync(new BookDownLoad.DownloadListener<BookInfo>() {
//                @Override
//                public String getFileName() {
//                    return FileManager.TEMP_DIR + "/" + name + ".html";
//                }
//
//                @Override
//                public String getLink() {
//                    return string;
//                }
//
//                @Override
//                public long getId() {
//                    return 0;
//                }
//
//                @Override
//                public void onStart() {
//                    FileManager.clearTempFolder();
//                }
//
//                @Override
//                public void onError(int errorCode) {
//                    Log.e("sunday","errorCode="+errorCode);
//                    checkFinish(null);
//                }
//
//                @Override
//                public void onResult(BookInfo result) {
//                    //searchBookInfoList.add(result);
//                    checkFinish(result);
//                }
//            });
//        }
    }

//    public void searchBookDebug(final String name){
//        bookSearchListener.bookSearchStart();
//        OKhttpManager.getInstance().connectUrl(new OKHttpListener() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                if(response != null && response.body() != null){
//                    try {
//                        FileManager.writeSearchFile(response.body().bytes());
//                        baiduSearchList = ParseXml.createParse(ParseBaiduSearch.class).from(FileManager.TEMP_BAIDU_SEARCH_FILE).parse();
//                        findRealLink();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }finally {
//                        response.body().close();
//                    }
//                }
//            }
//
//            @Override
//            public String getLink() {
//
//                return "http://www.baidu.com/s?q1="+name+"&q2=&q3=&q4=&gpc=stf&ft=&q5=1&q6=&tn=baiduadv";
//            }
//        });
//    }

    public void runMainUiThread(Runnable runnable){
        handler.post(runnable);
    }

}
