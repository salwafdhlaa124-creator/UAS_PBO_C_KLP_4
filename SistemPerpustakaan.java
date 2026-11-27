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

// ======================= ADMIN =======================
class Admin extends Pengguna {

    public Admin(String username, String password, String nama) {
        super(username, password, nama);
    }

    @Override
    public String getPeran() { return "Admin"; }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n========== MENU ADMIN ==========");
        System.out.println("1. Lihat Daftar Buku");
        System.out.println("2. Cari Buku");
        System.out.println("3. Tambah Buku");
        System.out.println("4. Edit Buku");
        System.out.println("5. Hapus Buku");
        System.out.println("6. Lihat Daftar Anggota");
        System.out.println("7. Lihat Semua Riwayat Transaksi");
        System.out.println("0. Logout");
        System.out.println("================================");
    }
    @Override
    public void jalankan(Perpustakaan sistem, Scanner sc) {
        int pilih;
        do {
            tampilkanMenu();
            pilih = Input.bacaInt(sc, "Pilih menu: ");
            try {
                switch (pilih) {
                    case 1: sistem.tampilkanBuku(sistem.getDaftarBuku()); break;
                    case 2: cariBuku(sistem, sc); break;
                    case 3: tambahBuku(sistem, sc); break;
                    case 4: editBuku(sistem, sc); break;
                    case 5: hapusBuku(sistem, sc); break;
                    case 6: sistem.tampilkanAnggota(); break;
                    case 7: sistem.tampilkanTransaksi(sistem.getDaftarTransaksi()); break;
                    case 0: System.out.println("Logout berhasil."); break;
                    default: System.out.println("Menu tidak tersedia.");
                }
            } catch (PerpustakaanException e) {
                System.out.println("[GAGAL] " + e.getMessage());
            }
        } while (pilih != 0);
    } 
    
    private void cariBuku(Perpustakaan sistem, Scanner sc) {
        String kata = Input.bacaTeks(sc, "Kata kunci judul/penulis: ");
        sistem.tampilkanBuku(sistem.cariBuku(kata));
    }

    private void tambahBuku(Perpustakaan sistem, Scanner sc) throws PerpustakaanException {
        String judul = Input.bacaTeks(sc, "Judul    : ");
        String penulis = Input.bacaTeks(sc, "Penulis  : ");
        int tahun = Input.bacaInt(sc, "Tahun    : ");
        int stok = Input.bacaInt(sc, "Stok     : ");
        if (tahun < 1000 || tahun > LocalDate.now().getYear()) {
            throw new PerpustakaanException("Tahun terbit tidak valid.");
        }
        if (stok < 0) {
            throw new PerpustakaanException("Stok tidak boleh negatif.");
        }
        Buku b = new Buku(judul, penulis, tahun, stok);
        sistem.tambahBuku(b);
        System.out.println("[OK] Buku berhasil ditambahkan dengan ID " + b.getId());
    }

    private void editBuku(Perpustakaan sistem, Scanner sc) throws PerpustakaanException {
        sistem.tampilkanBuku(sistem.getDaftarBuku());
        int id = Input.bacaInt(sc, "ID buku yang diedit: ");
        Buku b = sistem.cariBukuById(id);
        System.out.println("(Kosongkan input untuk mempertahankan nilai lama)");

        String judul = Input.bacaTeksOpsional(sc, "Judul [" + b.getJudul() + "]: ");
        String penulis = Input.bacaTeksOpsional(sc, "Penulis [" + b.getPenulis() + "]: ");
        String tahun = Input.bacaTeksOpsional(sc, "Tahun [" + b.getTahun() + "]: ");
        String stok = Input.bacaTeksOpsional(sc, "Stok [" + b.getStok() + "]: ");

        try {
            int tahunBaru = tahun.isEmpty() ? b.getTahun() : Integer.parseInt(tahun);
            int stokBaru = stok.isEmpty() ? b.getStok() : Integer.parseInt(stok);
            if (tahunBaru < 1000 || tahunBaru > LocalDate.now().getYear()) {
                throw new PerpustakaanException("Tahun terbit tidak valid.");
            }
            if (stokBaru < 0) {
                throw new PerpustakaanException("Stok tidak boleh negatif.");
            }
            if (!judul.isEmpty()) b.setJudul(judul);
            if (!penulis.isEmpty()) b.setPenulis(penulis);
            b.setTahun(tahunBaru);
            b.setStok(stokBaru);
            System.out.println("[OK] Data buku berhasil diperbarui.");
        } catch (NumberFormatException e) {
            throw new PerpustakaanException("Tahun dan stok harus berupa angka.");
        }
    }

    private void hapusBuku(Perpustakaan sistem, Scanner sc) throws PerpustakaanException {
        sistem.tampilkanBuku(sistem.getDaftarBuku());
        int id = Input.bacaInt(sc, "ID buku yang dihapus: ");
        Buku b = sistem.cariBukuById(id);
        if (sistem.bukuSedangDipinjam(id)) {
            throw new PerpustakaanException("Buku masih dipinjam anggota, tidak bisa dihapus.");
        }
        String yakin = Input.bacaTeks(sc, "Yakin hapus \"" + b.getJudul() + "\"? (y/n): ");
        if (yakin.equalsIgnoreCase("y")) {
            sistem.hapusBuku(b);
            System.out.println("[OK] Buku berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }
}

// ======================= MODEL: TRANSAKSI =======================
class Transaksi {
    public static final int LAMA_PINJAM_HARI = 7;
    public static final int DENDA_PER_HARI = 1000;

    private static int counter = 0;

    private final int id;
    private final String usernameAnggota;
    private final int idBuku;
    private final String judulBuku;
    private final LocalDate tanggalPinjam;
    private final LocalDate jatuhTempo;
    private LocalDate tanggalKembali;
    private int denda;

    public Transaksi(String usernameAnggota, Buku buku, LocalDate tanggalPinjam) {
        this.id = ++counter;
        this.usernameAnggota = usernameAnggota;
        this.idBuku = buku.getId();
        this.judulBuku = buku.getJudul();
        this.tanggalPinjam = tanggalPinjam;
        this.jatuhTempo = tanggalPinjam.plusDays(LAMA_PINJAM_HARI);
    }

    public int getId() { return id; }
    public String getUsernameAnggota() { return usernameAnggota; }
    public int getIdBuku() { return idBuku; }
    public String getJudulBuku() { return judulBuku; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getJatuhTempo() { return jatuhTempo; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public int getDenda() { return denda; }

    public boolean sudahKembali() { return tanggalKembali != null; }

    public void kembalikan(LocalDate tanggal) {
        this.tanggalKembali = tanggal;
        long telat = ChronoUnit.DAYS.between(jatuhTempo, tanggal);
        this.denda = telat > 0 ? (int) telat * DENDA_PER_HARI : 0;
    }

    @Override
    public String toString() {
        String status = sudahKembali() ? "Dikembalikan " + tanggalKembali : "DIPINJAM";
        return String.format("| %-3d | %-10s | %-25s | %-10s | %-10s | %-24s | Rp%-6d |",
                id, usernameAnggota,
                judulBuku.length() > 25 ? judulBuku.substring(0, 22) + "..." : judulBuku,
                tanggalPinjam, jatuhTempo, status, denda);
    }
}

// ======================= MAIN =======================
public class SistemPerpustakaan {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Perpustakaan sistem = new Perpustakaan();
        int pilih;

        do {
            System.out.println("\n===================================");
            System.out.println("   SISTEM MANAJEMEN PERPUSTAKAAN   ");
            System.out.println("===================================");
            System.out.println("1. Login");
            System.out.println("2. Daftar Anggota Baru");
            System.out.println("0. Keluar");
            pilih = Input.bacaInt(sc, "Pilih menu: ");

            switch (pilih) {
                case 1:
                    String user = Input.bacaTeks(sc, "Username: ");
                    String pass = Input.bacaTeks(sc, "Password: ");
                    try {
                        Pengguna p = sistem.login(user, pass);
                        System.out.println("\nSelamat datang, " + p.getNama() + " (" + p.getPeran() + ")!");
                        p.jalankan(sistem, sc); // polymorphism: Admin / Anggota
                    } catch (PerpustakaanException e) {
                        System.out.println("[GAGAL] " + e.getMessage());
                    }
                    break;
                case 2:
                    String nama = Input.bacaTeks(sc, "Nama lengkap: ");
                    String u = Input.bacaTeks(sc, "Username    : ");
                    String pw = Input.bacaTeks(sc, "Password    : ");
                    try {
                        sistem.daftarAnggota(u, pw, nama);
                        System.out.println("[OK] Pendaftaran berhasil. Silakan login.");
                    } catch (PerpustakaanException e) {
                        System.out.println("[GAGAL] " + e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan sistem perpustakaan.");
                    break;
                default:
                    System.out.println("Menu tidak tersedia.");
            }
        } while (pilih != 0);

        sc.close();
    }
}
