package sunday.app.bairead.download;

import okhttp3.Response;
import sunday.app.bairead.database.BookInfo;
import sunday.app.bairead.parse.ParseChapter;
import sunday.app.bairead.parse.ParseChapterText;
import sunday.app.bairead.parse.ParseDetail;
import sunday.app.bairead.parse.ParseXml;
import sunday.app.bairead.tool.FileManager;

/**
 * Created by Administrator on 2017/3/5.
 */

public class BookDownLoad {

    public interface DownloadListener {
        void onError();
        void onNewChapter(BookInfo bookInfo);
    }

    private DownloadListener downloadListener;

    public BookDownLoad(DownloadListener downloadListener){
        this.downloadListener = downloadListener;
    }

    public void  updateNewChapter(final BookInfo bookInfo){
        final String bookName = bookInfo.bookDetail.getName();
        final String link = bookInfo.bookChapter.getChapterLink();
        final long id = bookInfo.bookDetail.getId();
        OKhttpManager.getInstance().connectUrl(new BookDownLoadListener(bookName, BookDownLoadListener.Type.ParseDownLoad){

            @Override
            public String getLink() {
                return link;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(BookInfo bookInfo) {
                if(downloadListener != null){
                    if(bookInfo != null) {
                        bookInfo.bookDetail.setId(id);
                    }
                    downloadListener.onNewChapter(bookInfo);
                }
            }

            @Override
            public void onError() {
                downloadListener.onError();
            }
        });
    }

    public BookInfo updateBookInfo(BookInfo bookInfo,String fileName){
        Response response = OKhttpManager.getInstance().connectUrl(bookInfo.bookChapter.getChapterLink());
        if(response != null && response.body() != null){
            try {
                String name = fileName == null ?  TEMP_CHAPTER_NAME : fileName;
                FileManager.writeByte(name, response.body().bytes());
                BookInfo  newBookInfo = new BookInfo();
                newBookInfo.bookDetail = ParseXml.createParse(ParseDetail.class).from(fileName).parse();
                newBookInfo.bookChapter = ParseXml.createParse(ParseChapter.class).from(fileName).parse();
                return newBookInfo;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                response.body().close();
            }
        }
        downloadListener.onError();
        return null;
    }

    static final String TEMP_TEXT_NAME = FileManager.PATH + "/" +"tempChapterText.html";
    static final String TEMP_CHAPTER_NAME = FileManager.PATH + "/" +"tempChapter.html";

    public String updateBookChapterText(String url,String fileName) {
        Response response = OKhttpManager.getInstance().connectUrl(url);
        if (response != null && response.body() != null) {
            try {
//                if(fileName != null) {
//                    FileManager.writeByte(fileName, response.body().bytes());
//                }else{
//                    //写入临时文件
//                    FileManager.writeByte(TEMP_FILE_NAME, response.body().bytes());
//                }
                String name = fileName == null ?  TEMP_TEXT_NAME : fileName;
                FileManager.writeByte(name, response.body().bytes());
                return ParseXml.createParse(ParseChapterText.class).from(name).parse();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }finally {
                response.body().close();
            }
        }

        return null;
    }



}
