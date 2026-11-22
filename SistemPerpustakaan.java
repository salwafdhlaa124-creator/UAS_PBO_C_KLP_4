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

 System.out.println(garis);
        for (Transaksi t : list) System.out.println(t);
        System.out.println(garis);
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
