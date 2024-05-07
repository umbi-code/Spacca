package spacca.spacca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {

    private ArrayList<Carta> mazzo = new ArrayList<>();

    //Genera e mischia il mazzo di carte
//    public Mazzo() {
//        mazzo.add(new Carta("URL", Carta.Tipo.MESCOLA));
//        mazzo.add(new Carta("URL", Carta.Tipo.STOP));
//        mazzo.add(new Carta("URL", Carta.Tipo.VITA_EXTRA));
//        mazzo.add(new Carta("URL", Carta.Tipo.GUARDA_CARTE_AL_CENTRO));
//        Collections.shuffle(mazzo);
//    }

    public ArrayList<Carta> getMazzo() {
        return mazzo;
    }




}
