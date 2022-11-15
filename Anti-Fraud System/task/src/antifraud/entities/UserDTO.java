package antifraud.entities;


public class UserDTO {
    private long id;
    private String name;
    private String username;
    private String role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        Role role1 = user.getRoles().iterator().next();
        this.role = role1.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
