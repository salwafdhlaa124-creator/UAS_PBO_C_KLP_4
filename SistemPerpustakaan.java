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
}
