// NEYIR ERDESER
// 260736004
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class Twitter {
  // attribute
  private ArrayList<Tweet> tweets;
  // constructor
  public Twitter() {
    ArrayList<Tweet>tweets = new ArrayList<Tweet>();
    this.tweets = tweets;
  }
  // get methods
  public int getSizeTwitter() {
    return tweets.size();
  }
  public Tweet getTweet(int index) {
    return tweets.get(index);
  }
  // methods - actaully doing work ones
  public void loadDB(String fileName) {
    // reads from file
    // creates a Tweet
    // checks if it has a message that's valid
    // if so, adds to the tweets ArrayList
    // - has a helper method, that has two helper methods :)
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      String tweet = br.readLine();
      while(tweet!=null) {
        String[]elements = tweet.split("\t");
        Tweet t = new Tweet(elements[0],elements[1],elements[2],elements[3]);
        if(t.checkMessage())
          this.tweets.add(t);
        tweet = br.readLine();
      }
      this.sortTwitter();
      br.close();
      fr.close();
    } catch (IOException e) {
      System.out.println("File not found");
    }
  }
  // using bubble sort
  // after each loop (outer) the largest element will be bubbled to the end
  // this is the helper method that has two helper methods
  public void sortTwitter() {
    boolean changeMade = true;
    int count = 1;
    while (changeMade) {
      changeMade = false;
      for (int i=0; i<tweets.size()-count; i++) {
        if(getEntry(i) > getEntry(i+1)) {
          swap(tweets,i);
          changeMade = true;
        }
      }
      count++;
    }
  }
  // swaps the element (tweet) at the given index with the next one
  // swaps them by inserting the second one right before the first one, then removing the initial second one (now after both)
  // a visual presentation of the ArrayList:
  // .......... first second ........
  // ....second first second ........
  // ... second first ...............
  private static void swap(ArrayList<Tweet>a,int i) {
    Tweet temp = a.get(i+1);
    a.set(i+1, a.get(i));
    a.set(i, temp);
  }
  // returns a long variable that indicates when the tweet was sent - as the one in Tweet class
  private long getEntry(int i) {
    return Long.parseLong(""+tweets.get(i).getDate().charAt(0)+tweets.get(i).getDate().charAt(1)+tweets.get(i).getDate().charAt(2)
                            +tweets.get(i).getDate().charAt(3)+tweets.get(i).getDate().charAt(5)+tweets.get(i).getDate().charAt(6)
                            +tweets.get(i).getDate().charAt(8)+tweets.get(i).getDate().charAt(9)+tweets.get(i).getTime().charAt(0)
                            +tweets.get(i).getTime().charAt(1)+tweets.get(i).getTime().charAt(3)+tweets.get(i).getTime().charAt(4)
                            +tweets.get(i).getTime().charAt(6)+tweets.get(i).getTime().charAt(7));
  }
  // creates a Stirng out of all loaded and sorted database using overridden toStirng
  public String printDB() {
    String dataBase = "";
    for(int i=0; i<getSizeTwitter()-1; i++) {
      dataBase += getTweet(i).toString()+"\n";
    } dataBase += getTweet(getSizeTwitter()-1).toString();
    return dataBase;
  }
  // creates a list of tweets ranging between the two input tweets (included) - earlier tweet first
  public ArrayList<Tweet> rangeTweets(Tweet t1,Tweet t2) {
    ArrayList<Tweet>ranged = new ArrayList<Tweet>();
    // since after loading the tweets are sorted, i'm simply comparing indices
    if(tweets.indexOf(t1)<tweets.indexOf(t2)) {
      for(int i=tweets.indexOf(t1); i<=tweets.indexOf(t2); i++) {
        ranged.add(tweets.get(i));
      }
    } else {
      for(int i=tweets.indexOf(t2); i<=tweets.indexOf(t1); i++) {
        ranged.add(tweets.get(i));
      }
    }
    return ranged;
  }
  // saves the output of printDB into a new file
  public void saveDB(String fileName) {
    try {
      FileWriter fw = new FileWriter(fileName);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(printDB());
      bw.close();
      fw.close();
    } catch (IOException e) {
      System.out.println("Invalid file name");
    }
  }
  // checkes each word used in messages and finds the word used in most tweets
  // has three helper methods to deal with punctuation
  public String trendingTopic() {
    // goal is to create a HashMap to count how many times each word has been repeated
    // first, a HashSet for each message, so repeated words within the message arent counted
    // then, an arrayList that will have ALL used words in it -including stopWords
    HashSet<String> messageWords = new HashSet<String>();
    ArrayList<String> words = new ArrayList<String>();
    // variables to help check punctuation
    char[]punc = {',','.',';',':'};
    String before = "";
    String after = "";
    // the outer loop splits the message into a textWords array and feeds the words(elements) to the inner for loop
    // the inner for loop checks each word for punctuation, removes if there is adds them to the messageWords HashSet
    // after all the words of one message are in the HashSet, the for each loop (inner) puts all elements of it in the arrayList
    // still in the outer loop, after both inner loops, the HashSet is cleared to start new for the next message
    for(int i=0; i<getSizeTwitter(); i++) {
      String[]textWords = tweets.get(i).getMessage().split(" ");
      for(int j=0; j<textWords.length; j++) {
      String temp = textWords[j];
      String line;
      if (hasPunctuationBefore(temp,punc) || hasPunctuationAfter(temp,punc)) // CAN TRY MAKING THIS SOONER
        line = removePunctuation(temp,punc);
      else
        line = temp;
        messageWords.add(line);
      }
      for (String s : messageWords) {
        words.add(s);
      }
      messageWords.clear();
    }
    // now removing all the stopWords from the ArrayList
    // very similar way I did in checkMessages moethod in Tweet class
    // difference: this time instead of discounting, it will;
    // remove element from the list
    // make the loop go back to index 0 so nothing is missed (since the indices will change after evey removal)
    for(int i=0; i<words.size(); i++) {
      for(String s : Tweet.stopWords) {
        if(s.equalsIgnoreCase(words.get(i))){
          words.remove(i);
          i = 0;
        }
        for (int j=0; j<punc.length; j++) {
          before = punc[j]+s;
          after = s+punc[j];
          if(before.equalsIgnoreCase(words.get(i))) {
            words.remove(i);
            i = 0;
          } if(after.equalsIgnoreCase(words.get(i))) {
            words.remove(i);
            i = 0;
          }
        }
      }
    }
    // and put them into a HashMap where keys are the words used and values are the repeat times
    // if the key wasnt used before it will be added with the value 1
    // if it was used before it will be added with 1 value higher than it's last value
    HashMap<String,Integer> counted = new HashMap<String,Integer>();
    for(int i=0; i<words.size(); i++) {
        if(counted.containsKey(words.get(i))) {  
          int value = counted.get(words.get(i));
          counted.put(words.get(i), ++value);
        }
        else {
          counted.put(words.get(i),1);
        }
      }
    // next step, return the key with highest value
    // first find the higest value
    int highest = 0;
    for(String s : counted.keySet()) {
      if(counted.get(s)>highest)
        highest = counted.get(s);
    }
    // then, find which key the highest value represents
    // in case of ties, the first word in the HashMap will be returned
    for(String s : counted.keySet()) {
      if(counted.get(s)==highest)
        return s;
    }
    return null; // because i need to return something here, also can be test return
  }
  // helper methods to trendingTopic
  // has PunctuationBefore/After checks wheter a word has a punctuation character at the beginning or the end of it
  // these characters will be given as input in an arrayt of chars
  private static boolean hasPunctuationBefore(String s, char[]c) {
    for(int i=0; i<c.length; i++) {
      if (s.charAt(0)==c[i])
        return true;
    }
    return false;
  }
  private static boolean hasPunctuationAfter(String s, char[]c) {
    for(int i=0; i<c.length; i++) {
      if (s.charAt(s.length()-1)==c[i])
        return true;
    }
    return false;
  }
  // revomes the found punctuation from the word
  private static String removePunctuation(String s, char[]c) {
    String word = "";
    if(hasPunctuationBefore(s,c)) {
      for(int i=1; i<s.length(); i++) {
        word += s.charAt(i);
      }
    } else if (hasPunctuationAfter(s,c)) {
      for(int i=0; i<s.length()-1; i++) {
        word += s.charAt(i);
      }
    }
    return word;
  }
  public static void main (String args[]) {
    // BASE COMPONENTS
    Twitter t = new Twitter();
    Tweet.loadStopWords("stopWords.txt");
    t.loadDB("tweets.txt");
    
    // EXAMPLE 2
    //System.out.println("The number of tweets is: "+t.getSizeTwitter());
    
    // EXAMPLE 3
    //System.out.println(t.printDB());
    
    // EXAMPLE 4
    //System.out.println(t.rangeTweets(t.getTweet(4), t.getTweet(2)));
    
    // EXAMPLE 5
    /*
    long set1 = System.nanoTime();
    System.out.println(t.trendingTopic());
    long set2 = System.nanoTime();
    System.out.println(set2-set1);
    */
  }
}