package btl.skincareapp.model;

public class User {
    private int id;              // ID của người dùng
    private String name;         // Tên người dùng
    private String loaiDa;       // Loại da (VD: "Da dầu", "Da khô")
    private String email, password;

    public User(int id, String name, String loaiDa, String email, String password) {
        this.id = id;
        this.name = name;
        this.loaiDa = loaiDa;
        this.email = email;
        this.password = password;

    }
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoaiDa() {
        return loaiDa;
    }

    public void setLoaiDa(String loaiDa) {
        this.loaiDa = loaiDa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
