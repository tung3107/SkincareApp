package btl.skincareapp.model;

import java.io.Serializable;
import java.util.Date;

public class MyProduct implements Serializable {
    private String TenSp, Gia, NhanHieu, Url_Anh;
    private int id, user_id;
    private String NgayMua, NgayHetHan;

    public MyProduct() {

    }
    public MyProduct(int id, String tenSp, String gia, String nhanHieu, String url_Anh, String ngayMua, String ngayHetHan, int user_id) {
        this.id = id;
        TenSp = tenSp;
        Gia = gia;
        NhanHieu = nhanHieu;
        Url_Anh = url_Anh;
        NgayMua = ngayMua;
        NgayHetHan = ngayHetHan;
        this.user_id = user_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String tenSp) {
        TenSp = tenSp;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getNhanHieu() {
        return NhanHieu;
    }

    public void setNhanHieu(String nhanHieu) {
        NhanHieu = nhanHieu;
    }

    public String getUrl_Anh() {
        return Url_Anh;
    }

    public void setUrl_Anh(String url_Anh) {
        Url_Anh = url_Anh;
    }

    public String getNgayMua() {
        return NgayMua;
    }

    public void setNgayMua(String ngayMua) {
        NgayMua = ngayMua;
    }

    public String getNgayHetHan() {
        return NgayHetHan;
    }

    public void setNgayHetHan(String ngayHetHan) {
        NgayHetHan = ngayHetHan;
    }
}
