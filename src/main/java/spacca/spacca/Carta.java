package spacca.spacca;
public class Carta {
    public String urlFaccia;
    public String nomeCarta;



//    enum Tipo {
//        MESCOLA, STOP, VITA_EXTRA, GUARDA_CARTE_AL_CENTRO;
//
//        private static final Tipo[] tipo = Tipo.values();
//        public static Tipo getTipo(int i) {
//            return Tipo.tipo[i];
//        }
//
//
//    };

//    private final Tipo tipo;

    public Carta(String urlFaccia, String nomeCarta) {
        this.urlFaccia=urlFaccia;
        this.nomeCarta = nomeCarta;
    }

//    public Tipo getTipo() {
//        return this.tipo;
//    }

    public String getUrlFaccia() {
        return this.urlFaccia;
    }


//public static List<Carta> mazzo() {
//    List<Carta> mazzo = new ArrayList<>();
//    mazzo.add(new Carta("Nome Carta 1", Tipo.MESCOLA, "URL_Immagine_Carta_1"));
//    mazzo.add(new Carta("Nome Carta 2", Tipo.STOP, "URL_Immagine_Carta_2"));
//    mazzo.add(new Carta("Nome Carta 3", Tipo.VITAEXTRA, "URL_Immagine_Carta_3"));
//    mazzo.add(new Carta("Nome Carta 4", Tipo.GUARDACARTEALCENTRO, "URL_Immagine_Carta_4"));
//    // Aggiungi altre carte al mazzo secondo necessità
//    return mazzo;
//}
}
