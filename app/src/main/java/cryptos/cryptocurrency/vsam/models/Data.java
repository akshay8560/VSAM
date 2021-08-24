package cryptos.cryptocurrency.vsam.models;

public class Data {

    private boolean seen;

    public Data(boolean seen) {
        this.seen = seen;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

//    private int likes;
//    private String comments;
//    private boolean save;
//
//    public Data(int likes, String comments, boolean save) {
//        this.likes = likes;
//        this.comments = comments;
//        this.save = save;
//    }
//
//    public int getLikes() {
//        return likes;
//    }
//
//    public void setLikes(int likes) {
//        this.likes = likes;
//    }
//
//    public String getComments() {
//        return comments;
//    }
//
//    public void setComments(String comments) {
//        this.comments = comments;
//    }
//
//    public boolean isSave() {
//        return save;
//    }
//
//    public void setSave(boolean save) {
//        this.save = save;
//    }
}
