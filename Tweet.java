// NEYIR ERDESER
// 260736004
import java.util.HashSet;
import java.io.*; // otherwise i had to import 6 seperate io's
public class Tweet {
  // attributes
  private String userAccount;
  private String date;
  private String time;
  private String message;
  public static  HashSet<String> stopWords;
  // constructor
  public Tweet(String user, String date, String time, String text) {
    // date YYYY-MM-DD
    // time HH:MM:SS
    this.userAccount = user;
    this.date = date;
    this.time = time;
    this.message = text;
  }
  // get methods and toString
  public String getDate() {
    return this.date;
  }
  public String getTime() {
    return this.time;
  }
  public String getMessage() {
    return this.message;
  }
  public String getUserAccount() {
    return this.userAccount;
  }
  public String toString() {
    return userAccount+"\t"+date+"\t"+time+"\t"+message;
  }
  
  // methods (actual, work doing ones)
  
  // counts how many words there are in a tweet
  // discounts the ones that appear in the stopWords list
  // considering punctuation
  public boolean checkMessage() {
    if(stopWords==null)
      throw new NullPointerException("Error checking the stopWords database: The file of stopWords has not been loaded yet");
    String[]messageWords = this.message.split(" ");
    int count = messageWords.length;
    char[]punc = {',','.',';',':'};
    String before = "";
    String after = "";
    // discounting if the actual word matches
    for(int i=0; i<messageWords.length; i++) {
      for(String s : stopWords) {
        if (s.equalsIgnoreCase(messageWords[i])) {
          count--;
        }
        // checking for punctuation and discountign if matches
        for(int j=0; j<punc.length; j++) {
          before = punc[j]+s;
          after = s+punc[j];
          if(before.equalsIgnoreCase(messageWords[i])) {
            count--; 
          }
          else if (after.equalsIgnoreCase(messageWords[i])) {
            count--;
          }
        }
      }
    }
    return count<16&&count>0;
  }
  // compares entry dates - has helper method
  public boolean isBefore(Tweet t) {
    // creating variables with date and time inforamtion for both tweet for comparison
    long entry1 = this.getEntry();
    long entry2 = t.getEntry();
    // comparison
    if(entry1<entry2)
      return true;
    else
      return false;
    // it would return false if they're posted at the exact same time
    // technically makes sense, since this tweet is not posted 'before' the other one
  }
  // returns a long variable that indicates when the tweet was sent
  private long getEntry() {
    // we know the formatting of the date and time
    // this method takes the digits at specific places and makes them into a 14-digit number
    // which tells when exactly the tweet was posted
    // note : i actually wanted to make this public and use it in the twitter class too, instead made a similar method there too
    return Long.parseLong(""+this.getDate().charAt(0)+this.getDate().charAt(1)+this.getDate().charAt(2)+this.getDate().charAt(3)
                            +this.getDate().charAt(5)+this.getDate().charAt(6)+this.getDate().charAt(8)+this.getDate().charAt(9)
                            +this.getTime().charAt(0)+this.getTime().charAt(1)+this.getTime().charAt(3)+this.getTime().charAt(4)
                            +this.getTime().charAt(6)+this.getTime().charAt(7));
  }
  // reads from file and initilizes the stopWords attribute
  public static void loadStopWords(String fileName) {
    // making a new HashSet to store the read words
    HashSet<String> wordList = new HashSet<String>();
    try {
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      String buffer = br.readLine();
      while(buffer!=null) {
       wordList.add(buffer);
        buffer = br.readLine();
      }
      br.close();
      fr.close();
      // assign the new set to the attribute
      stopWords = wordList;
    } catch (IOException e) {
      System.out.println("Invalid file name");
    }
  }
}