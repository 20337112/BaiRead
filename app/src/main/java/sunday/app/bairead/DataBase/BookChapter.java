package sunday.app.bairead.DataBase;

import android.content.ContentValues;

/**
 * Created by sunday on 2016/12/12.
 * 监听正版网站更新，
 * 搜索结果只显示一条有最新章节的源，加入书架后，自动切换源
 * 在书籍文件夹内生成一个txt文件用于保存每个章节的源和对应的章节目录，
 * 按行读取，第一行为第一章
 */

public class BookChapter{

    /**
     * 章节目录页
     */
    private String chapterLink;

    /**
     * 章节总数
     * */
    private int chapterCount;

    /**
     * 阅读章节
     * */
    private int chapterIndex;

    /**
     * 当前阅读源
     * */
    private boolean current;


    public static class Builder{
        private String chapterLink;
        private int chapterCount;
        private int chapterIndex;
        private boolean current;
        public Builder(){

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

        public Builder setCurrent(boolean current) {
            this.current = current;
            return this;
        }

        public BookChapter build(){
            return new BookChapter(this);
        }

    }

    private BookChapter(Builder builder){
        this.chapterLink = builder.chapterLink;
        this.chapterCount = builder.chapterCount;
        this.chapterIndex = builder.chapterIndex;
        this.current = builder.current;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public String getChapterLink() {
        return chapterLink;
    }

    public boolean isCurrent() {
        return current;
    }


    public void onAddToDatabase(ContentValues values){
        values.put(BookSetting.Chapter.Link,chapterLink);
        values.put(BookSetting.Chapter.INDEX,chapterIndex);
        values.put(BookSetting.Chapter.COUNT,chapterCount);

        /*
         *current 为真表示使用的是当前来源
         */
        int source = current ? 1 : 0;
        values.put(BookSetting.Chapter.CURRENT,source);
    }
}
