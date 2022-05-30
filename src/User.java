public class User {

    String username;
    boolean isClubMember;
    boolean isAdmin;
    String password;

    User(String username, String password, boolean isClubMember, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isClubMember = isClubMember;
        this.isAdmin = isAdmin;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean getIsClubMember() {
        return isClubMember;
    }

    public void setClubMember(boolean isClubMember) {
        this.isClubMember = isClubMember;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "user" + "\t" + username + "\t" + password + "\t" + isClubMember + "\t" + isAdmin + "\n";

    }

}
