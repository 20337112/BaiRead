package sunday.app.bairead.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sunday.app.bairead.database.BookInfo;
import sunday.app.bairead.R;
import sunday.app.bairead.tool.Temp;
import sunday.app.bairead.presenter.BookSearchPresenter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sunday on 2017/3/6.
 */

public class BookSearchActivity extends BaseActivity implements BookSearchPresenter.IBookSearchListener{


    private BookSearchPresenter bookSearchPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_activity);
        setupView();
        bookSearchPresenter = new BookSearchPresenter(this);
    }

    private EditText mBookTextEditText;
    private ListView historyListView;
    private TextView historyTextView;
    private Button historyDeleteButton;
    private ListView bookListView;
    private HistoryAdapter historyAdapter;
    private BookAdapter bookAdapter;
    private TextView mToastView;


    private class HistoryAdapter extends BaseAdapter{

        private ArrayList<String> list;


        public HistoryAdapter(ArrayList<String> historyList){
            this.list = historyList;
        }

        public void addItem(String name){
            list.add(0,name);
            notifyDataSetChanged();
        }

        public void clear(){
            list.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.history_list_item,null,false);
                convertView.setMinimumHeight(200);
            }

            ((TextView)convertView).setText(list.get(position));
            return convertView;
        }
    }


    class ViewHolder {
        private TextView nameTView;
        private TextView authorTView;
        private TextView sourceTView;
        private TextView chapterLatestTView;
        private TextView chapterTimeTView;

        ViewHolder(View parent) {
            nameTView = (TextView) parent.findViewById(R.id.search_fragment_list_item_name);
            authorTView = (TextView) parent.findViewById(R.id.search_fragment_list_item_author);
            sourceTView = (TextView) parent.findViewById(R.id.search_fragment_list_item_source);
            chapterLatestTView = (TextView) parent.findViewById(R.id.search_fragment_list_item_chapter_latest);
            chapterTimeTView = (TextView) parent.findViewById(R.id.search_fragment_list_item_chapter_time);
        }

        void setValue(BookInfo bookInfo){
            nameTView.setText(bookInfo.bookDetail.getName());
            authorTView.setText(bookInfo.bookDetail.getAuthor());
            //sourceTView.setText(sourceTView.getText()+info.get);
            chapterLatestTView.setText(bookInfo.bookDetail.getChapterLatest());
            chapterTimeTView.setText(bookInfo.bookDetail.getUpdateTime());
        }

    }

    private class BookAdapter extends BaseAdapter{
        private ArrayList<BookInfo> list;
        public BookAdapter(ArrayList<BookInfo> bookInfoArrayList){
            list = bookInfoArrayList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.search_list_item,null,false);
                ViewHolder viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.setValue(list.get(position));
            return convertView;
        }
    }

    private void setupView(){
        mBookTextEditText = (EditText) findViewById(R.id.book_search_edit_text);
        Button ButtonSearch = (Button) findViewById(R.id.book_search_button_search);
        ButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBookTextEditText.getText().toString();
                mBookTextEditText.setText("");
                mBookTextEditText.clearFocus();
                bookSearchPresenter.addSearchHistory(getBaseContext(),name);
                bookSearchPresenter.searchBook(name);
            }
        });
        historyTextView = (TextView) findViewById(R.id.book_search_history);
        historyDeleteButton = (Button) findViewById(R.id.book_search_history_delete_button);
        historyDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyAdapter.clear();
                bookSearchPresenter.clearHistory(getBaseContext());
            }
        });
        historyListView = (ListView) findViewById(R.id.book_search_history_list_view);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) historyAdapter.getItem(position);
                bookSearchPresenter.searchBook(name);
            }
        });
        bookListView = (ListView) findViewById(R.id.book_search_book_list_view);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookInfo bookInfo = (BookInfo) bookAdapter.getItem(position);
                Temp.getInstance().setBookInfo(bookInfo);
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), BookDetailActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mToastView = (TextView) findViewById(R.id.book_search_book_dont_find);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookSearchPresenter.readSearchHistory(this);
    }

    @Override
    public void historyAddFinish(String name) {
        historyAdapter.addItem(name);

    }

    @Override
    public void historyLoadFinish(ArrayList<String> historyList) {
        historyAdapter = new HistoryAdapter(historyList);
        historyListView.setAdapter(historyAdapter);
    }

    @Override
    public void bookSearchStart(ArrayList<BookInfo> bookInfoArrayList) {
        showHistoryPanel(false);
        showBookListPanel(true);
        bookAdapter = new BookAdapter(bookInfoArrayList);
        bookListView.setAdapter(bookAdapter);
    }

    @Override
    public void bookSearchFinish() {
        bookAdapter.notifyDataSetChanged();
    }

    public void showHistoryPanel(boolean show){
        int visibility = show ? View.VISIBLE : View.GONE;
        mToastView.setVisibility(visibility);
        historyListView.setVisibility(visibility);
        historyTextView.setVisibility(visibility);
        historyDeleteButton.setVisibility(visibility);

    }

    public void showBookListPanel(boolean show){
        int visibility = show ? View.VISIBLE : View.GONE;
        bookListView.setVisibility(visibility);
    }


}