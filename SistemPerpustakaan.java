import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * SISTEM MANAJEMEN PERPUSTAKAAN (CLI)
 *
 * Konsep PBO: Encapsulation, Inheritance, Abstraction, Polymorphism,
 * Collection (HashMap & ArrayList), dan Exception Handling
 * (InputMismatchException + custom exception).
 *
 * Kompilasi : javac SistemPerpustakaan.java
 * Jalankan  : java SistemPerpustakaan
 */

// ======================= EXCEPTION KUSTOM =======================
class PerpustakaanException extends Exception {
    public PerpustakaanException(String pesan) {
        super(pesan);
    }
}

// ======================= MODEL: BUKU =======================
class Buku {
    private static int counter = 0;

    private final int id;
    private String judul;
    private String penulis;
    private int tahun;
    private int stok;

    public Buku(String judul, String penulis, int tahun, int stok) {
        this.id = ++counter;
        this.judul = judul;
        this.penulis = penulis;
        this.tahun = tahun;
        this.stok = stok;
    }

    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public int getTahun() { return tahun; }
    public int getStok() { return stok; }

    public void setJudul(String judul) { this.judul = judul; }
    public void setPenulis(String penulis) { this.penulis = penulis; }
    public void setTahun(int tahun) { this.tahun = tahun; }
    public void setStok(int stok) { this.stok = stok; }

    public boolean tersedia() { return stok > 0; }

    public void kurangiStok() throws PerpustakaanException {
        if (stok <= 0) {
            throw new PerpustakaanException("Stok buku \"" + judul + "\" sedang habis.");
        }
        stok--;
    }

    public void tambahStok() { stok++; }

    @Override
    public String toString() {
        return String.format("| %-3d | %-30s | %-20s | %-5d | %-4d |",
                id, potong(judul, 30), potong(penulis, 20), tahun, stok);
    }

    private static String potong(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}

// ======================= ABSTRACTION: PENGGUNA =======================
abstract class Pengguna {
    private final String username;
    private String password;
    private final String nama;

    public Pengguna(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama = nama;
    }
    public String getUsername() { return username; }
    public String getNama() { return nama; }

    public boolean cekPassword(String input) { return password.equals(input); 
    }
        public abstract String getPeran();

    // Polymorphism: tiap subclass menampilkan menu sesuai hak akses
    public abstract void tampilkanMenu();

    // Loop utama sesi pengguna setelah login
    public abstract void jalankan(Perpustakaan sistem, Scanner sc);
}
