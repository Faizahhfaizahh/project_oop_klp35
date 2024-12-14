class Score{
    private int id;
    int skor_tinggi;
    String waktu;
    public Score(int id, int skor_tinggi, String waktu) {
        this.id = id;
        this.skor_tinggi = skor_tinggi;
        this.waktu = waktu;
    }
    public int getId(){
        return id;
    }
    public int getSkor_tinggi(){
        return skor_tinggi;
    }
    public String getWaktu(){
        return waktu;
    }
}