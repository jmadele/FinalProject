package com.example.minjia.finalproject;

/**
 * CBCNewsData class is used to create a new object for news including news title, link and description
 * author: Min Jia
 */
public class CBCNewsData {

        private String newsTitle, newsLink, newsDescription, pubDate, author;
        public CBCNewsData() {      }
        public CBCNewsData(String title, String link, String description, String pubDate, String author) {
            setNewsTitle(title);
            setNewsDescription(description);
            setNewsLink(link);
            setAuthor(author);
            setPubDate(pubDate);
        }

        public String getNewsTitle() {
            return newsTitle;
        }
        public void setNewsTitle(String newsTitle) {
            this.newsTitle = newsTitle;
        }
        public void setNewsLink(String newsLink) {
            this.newsLink = newsLink;
        }
        public String getNewsLink() {
            return newsLink;
        }
        public String getNewsDescription() {
            return newsDescription;
        }
        public void setNewsDescription(String newsDescription) {
            this.newsDescription = newsDescription;
        }
        public String getPubDate() {return pubDate;}
        public void setPubDate(String pubDate){this.pubDate=pubDate;}
        public String getAuthor() {return author;}
        public void setAuthor(String author){this.author=author;}
}



