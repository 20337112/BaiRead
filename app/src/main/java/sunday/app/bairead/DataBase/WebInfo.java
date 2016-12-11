package sunday.app.bairead.DataBase;

import sunday.app.bairead.Parse.HtmlParse;

/**
 * Created by sunday on 2016/12/8.
 */

public  class WebInfo{
    /**
     * 站内搜索使用百度引擎的SID
     * 访问方式var url = "http://zhannei.baidu.com/cse/search?s=5334330359795686106&q="
     * + encodeURIComponent(书名);
     * */

    public static final int NAME = 0;
    public static final int LINK = 1;
    public static final int CUS_ID = 2;

    public static final String[][] TOP_WEB= {
            {"笔趣阁","http://www.biquge.com/","287293036948159515"},
            {"新笔趣阁","http://www.xxbiquge.com/","8823758711381329060"},
            {"顶点小说","http://www.23wx.com/","15772447660171623812"},
            {"假顶点小说","http://www.23us.so/","17233375349940438896"},
            {"无错小说","http://www.quledu.com/","14724046118796340648"},
            };


    private String name;
    private String link;
    private String cusId;


    public WebInfo(String name,String link,String cusId){
        this.name = name;
        this.link = link;
        this.cusId = cusId;
    }


    public String getName(){
        return name;
    }

    public String getLink(){
        return link;
    }

    public String getCusId(){
        return cusId;
    }

    private HtmlParse htmlParse;

    public void setParse(HtmlParse htmlParse){
        this.htmlParse = htmlParse;
    }

    public HtmlParse getParse(){
        return htmlParse;
    }

}



