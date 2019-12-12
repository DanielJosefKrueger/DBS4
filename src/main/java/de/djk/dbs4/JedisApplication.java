package de.djk.dbs4;

import redis.clients.jedis.Jedis;

import java.util.*;

public class JedisApplication {

    private final Jedis jd;

    private final static String TITLE_KEY ="title";
    private final  static String AUTOR_KEY = "autor";
    private final static String PRAEFIX_ARTICLE_RATING = "BewArtikel:";
    private final static String PRAEFIX_ARTICLE = "Artikel:";
    private final static String RATING_KEY = "Bewertungen";


    private final ArrayList<String> articleIds = new ArrayList<>();

    JedisApplication() {
        jd = new Jedis("localhost");
    }



    boolean postNewArticle(String articleId, String autor, String title){
        articleIds.add(articleId);

        String id = PRAEFIX_ARTICLE + articleId;
        jd.hset(id, TITLE_KEY, title);
        jd.hset(id, AUTOR_KEY, autor);
        jd.sadd(PRAEFIX_ARTICLE_RATING + articleId, "");
        return true;
    }


    boolean vote(String articleId, String voter, double voting){
        // check if already voted
        if(jd.smembers(PRAEFIX_ARTICLE_RATING + articleId).contains(voter)){
            System.out.println("Voter has already voted!");
            return false;
        }
        //add to voters
        jd.sadd(PRAEFIX_ARTICLE_RATING + articleId, voter);
        //how many voters
        Long voters = jd.scard(PRAEFIX_ARTICLE_RATING + articleId);
        // get the score
        Double previousScore = jd.zscore(RATING_KEY,articleId);
        if(previousScore==null){
            previousScore =0d;
        }

        double newScore = (previousScore * (voters-1) + voting) / voters;
        jd.zadd(RATING_KEY, newScore,  articleId);
        return true;
    }


    private void printInformation(String articleId){
        //get info from article hashmap
        Map<String,String> m = jd.hgetAll(PRAEFIX_ARTICLE + articleId);
        System.out.println("######################################");
        System.out.println("Article ID: " + articleId);
        System.out.println("Titel:      " + m.get(TITLE_KEY));
        System.out.println("Autor:      " + m.get(AUTOR_KEY));
        System.out.println("Rating:     " + jd.zscore(RATING_KEY, articleId));
        System.out.println("######################################");
    }


    void printBestArticles(int number){
        Set<String> articles = jd.zrevrange(RATING_KEY, 0, number - 1);

        int i =0;
        for (String articleId : articles) {
            System.out.println("Article Number: " + ++i);
            printInformation(articleId);
        }
    }

    String getRandomArticleID(){
        Random rand = new Random();
        return articleIds.get( rand.nextInt(articleIds.size()-1));
    }

    public List<String> getAllArticleIds(){
        return this.articleIds;
    }

    void flushEverything(){
        jd.flushAll();
        jd.flushDB();
    }
}
