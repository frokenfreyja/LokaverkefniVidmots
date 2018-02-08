package is.hi.spil.vinnsla;

import is.hi.spil.utlit.SpilUtlit;
import java.util.Random;
import javax.swing.ImageIcon;
import is.hi.spil.utlit.myndir.StretchIcon;

/**
 * Vinnsluklasi í minnisleiknum. Heldur utan um stigin, myndirnar á borðinu, stærð
 * borðsins, fylki með 10 myndum, random hlut og fjöldi rétt opinna reita
 *
 * @author Freyja Sigurgísladóttir, frs24@hi.is
 */
public class Bord implements BordInterface {

    private int stig;   // stig sem notandi hefur unnið sér inn   // litir á borðinu 
    private final int staerd;      // stærð borðisns 
    private final String[] MYNDIR = {"frida", "bas", "alfred", "david", "pearl",
        "blue", "marie", "tar", "sofia", "pulp"};
    StretchIcon[] myndir;
    private final Random rand = new Random();
    private int opnir;  // fjöldi opinna reita
    private final int timi;

    /**
     * Býr til borð af stærð staerd
     *
     * @param staerd
     * @param t
     */
    public Bord(int staerd, int t) {
        this.staerd = staerd;
        myndir = new StretchIcon[staerd];
        nyrLeikur();
        timi = t;
    }

    /**
     * Ræsir nýjan leik Núllstillir stigin og setur random myndir í fylki myndir
     * sem hefur stök af taginu ImageIcon
     */
    @Override
    public void nyrLeikur() {
        nullStillaStigOgLokid();
        endursetjaLiti(staerd);
        
    }

    /**
     * Núllstilla stigin og fjöldi rétt opinna reita
     */
    private void nullStillaStigOgLokid() {
        stig = 0;
        opnir = 0;
    }
    
    /**
     * Finna mynd og skila henni svo sem StretchIcon (í réttum hlutföllum)
     * @param path
     * @return 
     */
    private static StretchIcon buaTilMynd(String path) {
        java.net.URL imgURL = SpilUtlit.class.getResource(path);
        if (imgURL != null) {
            return new StretchIcon(imgURL);
        } else {
            System.err.println("Fann ekki skrána " + path);
            return null;
        }
    }
    /**
     * setja s/2 myndir tilviljanakennt í s reiti þannig að tveir reitir séu
     * með sömu mynd
     *
     * @param s
     */
    public void endursetjaLiti(int s) {
        //búa til myndirnar
        //for(int i=0; i<staerd;i++) {
          //  myndir[i] = buaTilMynd("myndir/"+MYNDIR[i/2]+".gif");
        //}
        int[] fjoldiPerLit = new int[staerd/2];

        for (int i = 0; i < staerd/2; i++) {
            fjoldiPerLit[i] = 0;
        }
        for (int i = 0; i < s; i++) {
            int c = rand.nextInt(s / 2);
            while (fjoldiPerLit[c] == 2) {
                c = rand.nextInt(s / 2);
            }
            myndir[i] = buaTilMynd("myndir/"+MYNDIR[c]+".gif");
            fjoldiPerLit[c] = fjoldiPerLit[c] + 1;
        }
        //Fisher-Yates stokkun á myndunum
       int n = MYNDIR.length;
        for(int i=0; i<MYNDIR.length; i++) {
            int random = i + (int)(Math.random()*(n-i));
            
            String randomElement = MYNDIR[random];
            MYNDIR[random] = MYNDIR[i];
            MYNDIR[i] = randomElement;
        }
    }   

    /**
     * Lækkar stig um einn
     *
     * @return stig
     */
    @Override
    public int laekkaStig() {
        stig = stig - 1;
        return stig;
    }
    
    /**
     * Skilar mynd nr. i í fylki myndir
     *
     * @param i
     * @return myndir (ImageIcon)
     */
    @Override
    public ImageIcon getMynd(Integer i) {
        return myndir[i];
    }

    /**
     * Hækkar teljara (opnir) um einn sem heldur utan um hve margir reitir eru
     * rétt opnir, þ.e. myndir eru þær sömu.
     */
    @Override
    public void haekkaOpnir() {
        opnir = opnir + 2;
    }

    /**
     * Skilar satt ef allir reitir eru rétt opnir og þar með leik lokið
     *
     * @return
     */
    @Override
    public boolean allirOpnir() {
        return (opnir == staerd);
    }

    @Override
    public int getStaerd() {
        return staerd;
    }

    public int getTimi() {
        return timi;
    }
  
    
}
