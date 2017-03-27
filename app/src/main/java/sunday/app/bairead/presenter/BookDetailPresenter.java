package sunday.app.bairead.presenter;

import android.content.Context;

import sunday.app.bairead.database.BaiReadApplication;
import sunday.app.bairead.database.BookInfo;
import sunday.app.bairead.database.BookModel;
import sunday.app.bairead.download.BookChapterCache;

/**
 * Created by sunday on 2017/3/7.
 */

public class BookDetailPresenter {

    private Context context;
    public interface IBookDetailListener{

    }

    private  IBookDetailListener bookDetailListener;
    public BookDetailPresenter(Context c,IBookDetailListener bookDetailListener){
        context = c;
        this.bookDetailListener = bookDetailListener;
    }

    public void readBook(BookInfo bookInfo){
        BookcasePresenter.readBook(context,bookInfo);
    }

    public static void addToBookCase(Context context,BookInfo bookInfo){
        BaiReadApplication application  = (BaiReadApplication) context.getApplicationContext();
        BookModel bookModel = application.getBookModel();
        bookModel.addBook(bookInfo);
    }

    public boolean isBookCase(BookInfo bookInfo){
        BaiReadApplication application  = (BaiReadApplication) context.getApplicationContext();
        BookModel bookModel = application.getBookModel();
        return bookModel.isBookCase(bookInfo);
    }


    public void cacheBook(BookInfo bookInfo){
        BookChapterCache.getInstance().downloadAllChpater(bookInfo);
    }
}
