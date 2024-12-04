package btl.skincareapp;

public class Product {
    private String TenSp;
    private String MoTaSP;
    private int imageImport;

    public Product(String tenSp, String moTaSP, int imageImport) {
        TenSp = tenSp;
        MoTaSP = moTaSP;
        this.imageImport = imageImport;
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
