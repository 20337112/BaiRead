package sunday.app.bairead.data.setting;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by sunday on 2016/12/12.
 * 监听正版网站更新，
 * 搜索结果只显示一条有最新章节的源，加入书架后，自动切换源
 * 在书籍文件夹内生成一个txt文件用于保存每个章节的源和对应的章节目录，
 * 按行读取，第一行为第一章
 */

public class BookChapter extends BookBase {

    public static final String FileName = "chapter.html";

    /**
     * 章节目录页
     */
    private String chapterLink;

    /**
     * 章节总数
     */
    private int chapterCount;

    /**
     * 阅读章节
     */
    private int chapterIndex;
    /**
     * 阅读章节页
     * */
    private int chapterPage;

    /**
     * 当前阅读源
     * 0 = 其他盗版源
     * 1 = 当前盗版源
     * 99 = 正版源
     */
    private boolean current;


    private ArrayList<Chapter> mChapterList;


    private BookChapter(Builder builder) {
        this.chapterLink = builder.chapterLink;
        this.chapterPage = builder.chapterPage;
        this.chapterIndex = builder.chapterIndex;
        this.chapterCount = builder.chapterCount;
        this.current = builder.current;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int index) {
        chapterIndex = index;
    }

    public int getChapterPage(){
        return chapterPage;
    }

    public void setChapterPage(int page){
        chapterPage = page;
    }

    /**
    * 书架要显示总章节，但是为了加快速度，章节列表没有在启动时加载，
    * 所以count读写在数据库中，不是直接返回list的长度
    * */
    public int getChapterCount() {
        return chapterCount;
    }

    public String getChapterLink() {
        return chapterLink;
    }

    public boolean isCurrent() {
        return current;
    }

    public Chapter getChapter(int index) {
        return mChapterList.get(index);
    }

    public Chapter getCurrentChapter(){
        return mChapterList.get(chapterIndex);
    }

    public void setChapterList(ArrayList<Chapter> list) {
        mChapterList = list;
        chapterCount = list.size();
    }

    public ArrayList<Chapter> getChapterList() {
        return mChapterList;
    }

    public void onAddToDatabase(ContentValues values) {
        values.put(BookSetting.Chapter.LINK, chapterLink);
        values.put(BookSetting.Chapter.INDEX, chapterIndex);
        values.put(BookSetting.Chapter.PAGE, chapterPage);
        values.put(BookSetting.Chapter.COUNT, chapterCount);

        /*
         *current 为真表示使用的是当前来源
         */
        int source = current ? 1 : 0;
        values.put(BookSetting.Chapter.CURRENT, source);
    }

    public static class Builder {
        private String chapterLink;
        private int chapterCount;
        private int chapterIndex;
        private int chapterPage;
        private boolean current;

        public Builder() {

        }

        public Builder setChapterLink(String chapterLink) {
            this.chapterLink = chapterLink;
            return this;
        }

        public Builder setChapterCount(int chapterCount) {
            this.chapterCount = chapterCount;
            return this;
        }

        public Builder setChapterIndex(int chapterIndex) {
            this.chapterIndex = chapterIndex;
            return this;
        }

        public Builder setChapterPage(int chapterPage) {
            this.chapterPage = chapterPage;
            return this;
        }

        public Builder setCurrent(boolean current) {
            this.current = current;
            return this;
        }

        public BookChapter build() {
            return new BookChapter(this);
        }

    }


    public Chapter getLastChapter(){
        int count = mChapterList.size();
        return mChapterList.get(count - 1);
    }


}
