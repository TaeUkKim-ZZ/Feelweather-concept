package neolabs.feelweather;

public class CommentItem {
    public String username;
    public String comment;
    public int weatherimage;
    public String useruid;
    public String deletestring;

    public CommentItem(String username, int weatherimage, String comment, String useruid, String deletestring) {
        this.username = username;
        this.weatherimage = weatherimage;
        this.comment = comment;
        this.useruid = useruid;
        this.deletestring  = deletestring;
    }

}
