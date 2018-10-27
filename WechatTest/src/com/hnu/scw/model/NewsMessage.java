package com.hnu.scw.model;

import java.util.List;

/**
 * @author scw
 * @create 2018-01-17 13:35
 * @desc 对于图文消息的实体类
 **/
public class NewsMessage extends BaseMessage{
    private int ArticleCount;
    private List<NewsBase> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<NewsBase> getArticles() {
        return Articles;
    }

    public void setArticles(List<NewsBase> articles) {
        Articles = articles;
    }
}
