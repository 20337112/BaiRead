package sunday.app.bairead.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sunday.app.bairead.database.BookInfo;
import sunday.app.bairead.R;
import sunday.app.bairead.tool.Temp;
import sunday.app.bairead.presenter.BookDetailPresenter;
import sunday.app.bairead.view.BookStatusView;
import sunday.app.bairead.view.BookTypeView;

/**
 * Created by sunday on 2017/3/7.
 */

public class BookDetailActivity extends BaseActivity implements BookDetailPresenter.IBookDetailListener{
    private BookInfo bookInfo;

    private TextView nameTView;
    private TextView authorTView;
    private TextView sourceTView;
    private TextView chapterLatestTView;
    private TextView chapterTimeTView;
    private TextView mDescriptionTView;
    private BookTypeView bookTypeView;
    private BookStatusView bookStatusView;

    private Button mButtonReadView;
    private Button mButtonCaseView;
    private Button mButtonCahceView;


    private BookDetailPresenter bookDetailPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);
        bookInfo = Temp.getInstance().getBookInfo();
        //Temp.getInstance().clearBookInfo();
        bookDetailPresenter = new BookDetailPresenter(this,this);
        setTitle("图书简介");
        setupView();
        initView();

    }
    private void setupView(){
        nameTView = (TextView)findViewById(R.id.book_detail_activity_name);
        authorTView = (TextView)findViewById(R.id.book_detail_activity_author);
        authorTView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        sourceTView = (TextView)findViewById(R.id.book_detail_activity_source);
        chapterLatestTView = (TextView)findViewById(R.id.book_detail_activity_chapter_latest);
        chapterTimeTView = (TextView)findViewById(R.id.book_detail_activity_chapter_time);

        bookTypeView = (BookTypeView) findViewById(R.id.book_detail_activity_type);
        bookStatusView = (BookStatusView) findViewById(R.id.book_detail_activity_status);
        mDescriptionTView = (TextView) findViewById(R.id.book_detail_activity_description);

        mButtonReadView = (Button) findViewById(R.id.book_detail_activity_button_read);
        mButtonCaseView = (Button) findViewById(R.id.book_detail_activity_button_case);
        mButtonCahceView = (Button) findViewById(R.id.book_detail_activity_button_cache);
        mButtonReadView.setOnClickListener(onClickListener);
        mButtonCaseView.setOnClickListener(onClickListener);
        mButtonCahceView.setOnClickListener(onClickListener);
        //authorTView.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.book_detail_activity_button_read:
                    bookDetailPresenter.readBook(bookInfo);
                    break;
                case R.id.book_detail_activity_button_case:
                    bookDetailPresenter.addToBookCase(bookInfo);
                    break;
                case R.id.book_detail_activity_button_cache:
                    bookDetailPresenter.cacheBook(bookInfo);
                    break;
                //case R.id.book_detail_activity_author:
                default:
                    break;
            }
        }
    };

    private void initView(){
        nameTView.setText(bookInfo.bookDetail.getName());
        authorTView.setText(bookInfo.bookDetail.getAuthor());
        chapterLatestTView.setText(bookInfo.bookDetail.getChapterLatest());
        chapterTimeTView.setText(bookInfo.bookDetail.getUpdateTime());
        mDescriptionTView.setText(bookInfo.bookDetail.getDescription());

        if(bookDetailPresenter.isBookCase(bookInfo)) {
            mButtonCaseView.setEnabled(false);
        }

        int type = bookInfo.bookDetail.getType();
        bookTypeView.setType(type);
        boolean status = bookInfo.bookDetail.isStatus();
        bookStatusView.setStatus(status);
    }
}
