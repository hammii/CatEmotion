package gachon.hayeong.cat.data.DTO;

public class UserDTO {
    private String id;
    private String email;

    public UserDTO(String id, String email){
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
