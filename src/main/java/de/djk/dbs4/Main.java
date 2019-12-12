package de.djk.dbs4;

import redis.clients.jedis.Jedis;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        JedisApplication jedisApplication = new JedisApplication();

        jedisApplication.flushEverything();

        jedisApplication.postNewArticle("H34", "Metcalfe/Boggs", " Ethernet(ETH)");
        jedisApplication.postNewArticle("X12", "CHEN", "ERM");
        jedisApplication.postNewArticle("D1", "Dijkstra", "GoTo");
        jedisApplication.postNewArticle("R1", "Richie/Thompson", "UNIX");
        jedisApplication.postNewArticle("Z1", "Zimmerman", "OSI");
        jedisApplication.postNewArticle("C1", "Codd", "RDB");
        jedisApplication.postNewArticle("H1", "Hoare", "CSP");
        jedisApplication.postNewArticle("D2", "Diffie/Hellman", "CRY");
        jedisApplication.postNewArticle("H34", "Metcalfe/Boggs", " Ethernet(ETH)");

        int voterCounter = 50;
        Random random = new Random();
        for (int i = 0; i < voterCounter; i++) {
            String randomArticleID = jedisApplication.getRandomArticleID();
            double voting = random.nextDouble()*5;
            jedisApplication.vote(randomArticleID, "vater" + i, voting);
        }

        jedisApplication.printBestArticles(9);


        System.out.println("\nA voter wants to vote two times!");
        String randomArticleID = jedisApplication.getRandomArticleID();
        double voting = random.nextDouble()*5;
        jedisApplication.vote(randomArticleID, "doubleVoter", voting);
        jedisApplication.vote(randomArticleID, "doubleVoter", voting);


    }
}
