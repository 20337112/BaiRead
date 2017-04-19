package sunday.app.bairead.bookRead;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import sunday.app.bairead.data.setting.BookChapter;
import sunday.app.bairead.R;
import sunday.app.bairead.data.setting.BookMarkInfo;
import sunday.app.bairead.utils.PreferenceSetting;
import sunday.app.bairead.base.BaseActivity;
import sunday.app.bairead.view.BookTextView;


/**
 * Created by sunday on 2017/2/23.
 */

public class BookReadSettingPanelView extends RelativeLayout {

    RelativeLayout settingTopPanel;
    LinearLayout settingBottomPanel;
    private boolean chapterOrder;
    private ChapterPanel chapterPanel;
    private BookMarkPanel bookMarkPanel;
    private BookReadSettingTextSizePanel bookReadSettingTextSizePanel;
    private Button chapterOrderButtonView;
    private ChapterAdapter chapterAdapter;
    private MarkAdapter markAdapter;
    private BookReadPresenter bookReadPresenter;
    private OnClickListener sizeOnReduceClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String key = (String) v.getTag();
            BookTextView.ReadSize readSize = BookReadPresenter.getReadSize(getContext());
            if (key.equals(PreferenceSetting.KEY_TEXT_SIZE)) {
                if(readSize.textSize > 30) {
                    readSize.textSize -= 4;
                }
            } else if (key.equals(PreferenceSetting.KEY_LINE_SIZE)) {
                if(readSize.lineSize > 6) {
                    readSize.lineSize -= 6;
                }
            } else if (key.equals(PreferenceSetting.KEY_MARGIN_SIZE)) {
                if(readSize.marginSize > 6) {
                    readSize.marginSize -= 6;
                }
            }
            bookReadPresenter.setReadSize(readSize);
        }
    };
    private OnClickListener sizeOnAddClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String key = (String) v.getTag();
            BookTextView.ReadSize readSize = BookReadPresenter.getReadSize(getContext());
            if (key.equals(PreferenceSetting.KEY_TEXT_SIZE)) {
                readSize.textSize += 4;
            } else if (key.equals(PreferenceSetting.KEY_LINE_SIZE)) {
                readSize.lineSize += 6;
            } else if (key.equals(PreferenceSetting.KEY_MARGIN_SIZE)) {
                readSize.marginSize += 6;
            }
            bookReadPresenter.setReadSize(readSize);
        }
    };
    private OnClickListener buttonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            BookReadActivity bookReadActivity = (BookReadActivity) getContext();
            switch (v.getId()) {
                case R.id.book_read_setting_panel_chapter_menu:
                    showChapterList();
                    break;
                case R.id.book_read_setting_panel_book_mark:
                    showMarkPanel();
                    break;
                case R.id.book_read_setting_panel_text_font:
                    showBookTextSizePanel();
                    break;
                case R.id.book_read_setting_panel_more:
                    //break;

//                case R.id.book_read_setting_panel_source:
                    break;
//                    Toast.makeText(getContext(), "开发中", Toast.LENGTH_SHORT).show();
                case R.id.book_read_setting_panel_mark_add:

                    if(bookReadPresenter.addBookMark()){
                        bookReadActivity.showToast(R.string.mark_success);
                    }else{
                        bookReadActivity.showToast(R.string.case_add_tips);
                    }

                    break;
                case R.id.book_read_setting_top_panel_add_case:
                    if( bookReadPresenter.addBookCase()){
                        bookReadActivity.showToast(R.string.case_add_success);
                    }else{
                        bookReadActivity.showToast(R.string.case_add_failed);
                    }
                    break;
                case R.id.book_read_setting_top_panel_cache_book:
                    if(bookReadPresenter.cacheBook()){
                        bookReadActivity.showToast(R.string.cache_book);
                    }else{
                        bookReadActivity.showToast(R.string.case_add_tips);
                    }
                default:
                    break;
            }
        }
    };

    public BookReadSettingPanelView(Context context) {
        super(context);
    }

    public BookReadSettingPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookReadSettingPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        settingTopPanel = (RelativeLayout) findViewById(R.id.book_read_setting_panel_top_panel);
        settingBottomPanel = (LinearLayout) findViewById(R.id.book_read_setting_panel_bottom_panel);
        setOnClick(settingTopPanel, buttonOnClickListener);
        setOnClick(settingBottomPanel, buttonOnClickListener);
    }

    public void setReadPresenter(BookReadPresenter bookReadPresenter) {
        this.bookReadPresenter = bookReadPresenter;
        chapterOrder = bookReadPresenter.getChapterOrder();
        //TextView titleView = (TextView) settingTopPanel.findViewById(R.id.book_read_setting_panel_title);
        //titleView.setText(bookReadPresenter.getBookName());
    }

    private void setOnClick(ViewGroup viewGroup, OnClickListener onClickListener) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = viewGroup.getChildAt(i);
            v.setOnClickListener(onClickListener);
        }

    }

    public void reverseChapterList() {
        Collections.reverse(chapterAdapter.getChapterArrayList());
        chapterAdapter.notifyDataSetChanged();
    }


    public void setOrderText(boolean order) {
        chapterOrderButtonView.setText(order ? R.string.order_default : R.string.order_revert);
    }

    private void showChapterList() {
        chapterPanel = (ChapterPanel) LayoutInflater.from(getContext()).inflate(R.layout.book_read_setting_chapter_panel, null, false);

        TextView bookNameView = (TextView) chapterPanel.findViewById(R.id.book_read_setting_panel_chapter_list_title);
        String bookName = BookChapterCacheNew.getInstance().getBookName();
        bookNameView.setText(bookName);

        //if(chapterAdapter == null){
        chapterAdapter = new ChapterAdapter();
        chapterAdapter.setChapterList(BookChapterCacheNew.getInstance().getChapterArrayList());
        if (!chapterOrder) {
            reverseChapterList();
        }
        //}
        ListView chapterListView = (ListView) chapterPanel.findViewById(R.id.book_read_setting_panel_chapter_list);
        chapterListView.setAdapter(chapterAdapter);
        int index = BookChapterCacheNew.getInstance().getChapterIndex() - 5;
        index = index < 0 ? 0 : index;
        index = chapterOrder ? index : chapterAdapter.getCount() - index - 1;
        chapterListView.setSelection(index);
        chapterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int chapterIndex = chapterOrder ? position : chapterAdapter.getCount() - position - 1;
                BookChapterCacheNew.getInstance().setChapterIndex(chapterIndex);
                hide();
            }
        });
        chapterOrderButtonView = (Button) chapterPanel.findViewById(R.id.book_read_setting_panel_chapter_list_order_button);
        setOrderText(chapterOrder);
        chapterOrderButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chapterOrder = !chapterOrder;
                bookReadPresenter.setChapterOrder(chapterOrder);
                setOrderText(chapterOrder);
                reverseChapterList();
            }
        });


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(chapterPanel, layoutParams);
    }


    private void showMarkPanel(){

        if(bookReadPresenter.getBookMarkList().size() == 0){
            BookReadActivity bookReadActivity = (BookReadActivity) getContext();
            bookReadActivity.showTipsDialog(R.string.mark_null);
        }else {
            bookMarkPanel = (BookMarkPanel) LayoutInflater.from(getContext()).inflate(R.layout.book_read_setting_mark_panel, null, false);
            TextView titleView = (TextView) bookMarkPanel.findViewById(R.id.book_read_setting_panel_mark_title);
            titleView.setText(BookChapterCacheNew.getInstance().getBookName());
            Button markDeleteButton = (Button) bookMarkPanel.findViewById(R.id.book_read_setting_panel_mark_delete_button);
            markDeleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookReadActivity bookReadActivity = (BookReadActivity) getContext();
                    bookReadActivity.showConfirmDialog(R.string.mark_clear, new BaseActivity.DialogListenerIm() {
                        @Override
                        public void onConfirm() {
                            bookReadPresenter.deleteBookMark();
                        }
                    });
                }
            });
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ListView bookListView = (ListView) bookMarkPanel.findViewById(R.id.book_read_setting_panel_mark_list);
            ArrayList<BookMarkInfo> list = bookReadPresenter.getBookMarkList();
            markAdapter = new MarkAdapter(list);
            bookListView.setAdapter(markAdapter);
            bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BookMarkInfo bookMarkInfo = (BookMarkInfo) markAdapter.getItem(position);
                    BookChapterCacheNew.getInstance().setChapterIndex(bookMarkInfo.chapterIndex);
                    hide();
                }
            });
            bookListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final BookMarkInfo bookMarkInfo = (BookMarkInfo) markAdapter.getItem(position);
                    BookReadActivity bookReadActivity = (BookReadActivity) getContext();
                    bookReadActivity.showConfirmDialog(R.string.mark_delete, new BaseActivity.DialogListenerIm() {
                        @Override
                        public void onConfirm() {
                            markAdapter.removeItem(bookMarkInfo);
                            bookReadPresenter.deleteBookMark(bookMarkInfo);
                        }
                    });

                    return true;
                }
            });
            addView(bookMarkPanel, layoutParams);
        }
    }


    private void setupTypeView(View parent,int imgLeftId,int imgRightId,int titleId, String key) {
        TextView titleView = (TextView) parent.findViewById(R.id.book_read_setting_size_line_title);
        ImageView reduceButton = (ImageView) parent.findViewById(R.id.book_read_setting_size_line_button_reduce);
        reduceButton.setImageResource(imgLeftId);
        ImageView addButton = (ImageView) parent.findViewById(R.id.book_read_setting_size_line_button_add);
        addButton.setImageResource(imgRightId);
        titleView.setText(titleId);
        reduceButton.setTag(key);
        addButton.setTag(key);
        reduceButton.setOnClickListener(sizeOnReduceClickListener);
        addButton.setOnClickListener(sizeOnAddClickListener);
    }



    static class PreferView{
        int imgLeftId;
        int imgRightId;
        int titleId;
        String prefKey;
        public PreferView(int imgLeftId,int imgRightId,int titleId,String key){
            this.imgLeftId = imgLeftId;
            this.imgRightId = imgRightId;
            this.titleId = titleId;
            this.prefKey = key;
        }
    }

    private void showBookTextSizePanel() {
        bookReadSettingTextSizePanel = (BookReadSettingTextSizePanel) LayoutInflater.from(getContext()).inflate(R.layout.book_read_setting_size_panel, null, false);
        PreferView[] preferViews = new PreferView[]{
                new PreferView(R.drawable.ic_font_reduce,R.drawable.ic_font_add,R.string.book_read_preference_title_font,PreferenceSetting.KEY_TEXT_SIZE),
                new PreferView(R.drawable.ic_line_reduce,R.drawable.ic_line_add,R.string.book_read_preference_title_line,PreferenceSetting.KEY_LINE_SIZE),
                new PreferView(R.drawable.ic_margin_reduce,R.drawable.ic_margin_add,R.string.book_read_preference_title_margin,PreferenceSetting.KEY_MARGIN_SIZE),
        };



        int childCount = bookReadSettingTextSizePanel.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = bookReadSettingTextSizePanel.getChildAt(i);
            setupTypeView(view, preferViews[i].imgLeftId,preferViews[i].imgRightId,preferViews[i].titleId,preferViews[i].prefKey);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(bookReadSettingTextSizePanel, layoutParams);

    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        if (chapterPanel != null) {
            removeView(chapterPanel);
            chapterPanel = null;
            chapterAdapter = null;
        } else if (bookReadSettingTextSizePanel != null) {
            removeView(bookReadSettingTextSizePanel);
            bookReadSettingTextSizePanel = null;
        }else if (bookMarkPanel != null) {
            removeView(bookMarkPanel);
            bookMarkPanel = null;
        }
        setVisibility(GONE);
    }

    public boolean isShow() {
        return getVisibility() == VISIBLE;
    }

    class ViewHolder {
        TextView textView;
        TextView cacheShowView;
    }

    private class ChapterAdapter extends BaseAdapter {

        //private BookInfo bookInfo;
        private ArrayList<BookChapter.Chapter> chapterArrayList;

        public void setChapterList(ArrayList<BookChapter.Chapter> list) {
            //this.bookInfo = bookInfo;
            chapterArrayList = (ArrayList<BookChapter.Chapter>) list.clone();
        }

        public ArrayList<BookChapter.Chapter> getChapterArrayList() {
            return chapterArrayList;
        }

        @Override
        public int getCount() {
            return chapterArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return chapterArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chapter_list_item, null, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.chapter_list_item_text);
                viewHolder.cacheShowView = (TextView) convertView.findViewById(R.id.chapter_list_item_cache_text);
                convertView.setTag(viewHolder);
            }

            BookChapter.Chapter chapter = chapterArrayList.get(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.textView.setText(chapter.getTitle());

            boolean isCache = BookChapterCacheNew.getInstance().isChapterExists(chapter);
            viewHolder.cacheShowView.setVisibility(isCache ? VISIBLE : INVISIBLE);

            return convertView;
        }
    }

    public static class MarkViewHolder{
        TextView titleView;
        TextView textView;
    }

    public class MarkAdapter extends BaseAdapter{

        private ArrayList<BookMarkInfo> list;
        public MarkAdapter(ArrayList<BookMarkInfo> markItemInfoArrayList){
            list = markItemInfoArrayList;
        }

        public void removeItem(BookMarkInfo bookMarkInfo){
            list.remove(bookMarkInfo);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.mark_list_item, null, false);
                MarkViewHolder markViewHolder = new MarkViewHolder();
                markViewHolder.titleView = (TextView) convertView.findViewById(R.id.mark_list_item_title);
                markViewHolder.textView = (TextView) convertView.findViewById(R.id.mark_list_item_text);
                convertView.setTag(markViewHolder);
            }

            MarkViewHolder markViewHolder = (MarkViewHolder) convertView.getTag();
            markViewHolder.titleView.setText(list.get(position).title);
            markViewHolder.textView.setText(list.get(position).text);
            return convertView;
        }
    }

}
