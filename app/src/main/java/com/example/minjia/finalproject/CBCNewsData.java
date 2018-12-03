package com.example.minjia.finalproject;

/**
 * CBCNewsData class is used to create a new object for news including news title, link and description
 */
public class CBCNewsData {

        private String newsTitle, newsLink, newsDescription;
        public CBCNewsData() {      }
        public CBCNewsData(String title, String link, String description) {
            setNewsTitle(title);
            setNewsDescription(description);
            setNewsLink(link);
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
}



