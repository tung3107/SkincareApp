package btl.skincareapp.model;

public class Product {
    private String TenSp;
    private String MoTaSP;
    private int imageImport;
    private String imageURL;

    public Product(String tenSp, String moTaSP, int imageImport) {
        TenSp = tenSp;
        MoTaSP = moTaSP;
        this.imageImport = imageImport;
        this.imageURL = null;
    }
    public Product(String tenSp, String moTaSP, String imageURL) {
        TenSp = tenSp;
        MoTaSP = moTaSP;
        this.imageURL = imageURL;
        this.imageImport = 0;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String tenSp) {
        TenSp = tenSp;
    }

    public String getMoTaSP() {
        return MoTaSP;
    }

    public void setMoTaSP(String moTaSP) {
        MoTaSP = moTaSP;
    }

    public int getImageImport() {
        return imageImport;
    }

    public void setImageImport(int imageImport) {
        this.imageImport = imageImport;
    }
}
