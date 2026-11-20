
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
