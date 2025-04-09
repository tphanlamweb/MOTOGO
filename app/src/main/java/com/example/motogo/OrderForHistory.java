package com.example.motogo;

import android.graphics.Bitmap;

public class OrderForHistory {

    private int TongGiaTien;
    private String SoCCCD, TenKH, NgayTra, NgayThue, SoHopDong, SoDT, ViTriGiaoXe;
    private Bitmap AnhCK;

    public OrderForHistory(String SoCCCD, String SoHopDong, int TongGiaTien, String TenKH, String NgayTra, String NgayThue, String SoDT, String ViTriGiaoXe, Bitmap AnhCK) {
        this.SoCCCD = SoCCCD;
        this.SoHopDong = SoHopDong;
        this.TongGiaTien = TongGiaTien;
        this.TenKH = TenKH;
        this.NgayTra = NgayTra;
        this.NgayThue = NgayThue;
        this.SoDT = SoDT;
        this.ViTriGiaoXe = ViTriGiaoXe;
        this.AnhCK = AnhCK;
    }


    public String getViTriGiaoXe() {
        return ViTriGiaoXe;
    }

    public void setViTriGiaoXe(String viTriGiaoXe) {
        ViTriGiaoXe = viTriGiaoXe;
    }

    public String getSoDT() {
        return SoDT;
    }

    public void setSoDT(String soDT) {
        SoDT = soDT;
    }

    public String getSoCCCD() {
        return SoCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        SoCCCD = soCCCD;
    }

    public Bitmap getAnhCK() {
        return AnhCK;
    }

    public void setAnhCK(Bitmap anhCK) {
        AnhCK = anhCK;
    }

    public String getSoHopDong() {
        return SoHopDong;
    }

    public void setSoHopDong(String soHopDong) {
        SoHopDong = soHopDong;
    }

    public int getTongGiaTien() {
        return TongGiaTien;
    }

    public void setTongGiaTien(int tongGiaTien) {
        TongGiaTien = tongGiaTien;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String tenKH) {
        TenKH = tenKH;
    }

    public String getNgayTra() {
        return NgayTra;
    }

    public void setNgayTra(String ngayTra) {
        NgayTra = ngayTra;
    }

    public String getNgayThue() {
        return NgayThue;
    }

    public void setNgayThue(String ngayThue) {
        NgayThue = ngayThue;
    }
}
