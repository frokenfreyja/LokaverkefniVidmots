package is.hi.spil.utlit;

import is.hi.spil.vinnsla.Bord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Freyja Sigurgísladóttir, frs24@hi.is
 */
public class ReiturListener implements ActionListener {

    private int fjoldiSmella = 0;
    private JButton fyrsti;
    Bord mittBord;
    

    public ReiturListener(Bord s) {
        mittBord = s;
        fyrsti = null;
    }

    /**
     * Opnar hnappinn með því að sýna mynd hans 
     * Ef komnir eru tveir hnappar þá athugum við hvort þeir hafa sömu mynd
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton hnappur = (JButton) e.getSource();
        // Ekki gera neitt ef ýtt er á hnapp með lit
        //if (hnappur.getIcon() != closed) {
          //  return;
        //} 
        //Spila hreyfihljóðið þegar klikkað er á reit
        SpilUtlit r = (SpilUtlit)hnappur.getTopLevelAncestor();
        if (r.soundClipMove.isRunning()) r.soundClipMove.stop();
        r.soundClipMove.setFramePosition(0); // rewind to the beginning
        r.soundClipMove.start();
        // Opna hnappinn
        breytaUmMynd(hnappur, hverErMynd(hnappur));
 
        
        // Athuga hvort komnir eru tveir hnappar og þá athuga hvort 
        // þeir eru eins eða ekki 
        kannaHnappa(hnappur);
    }
    

    /**
     *  Athuga hvort komnir eru tveir hnappar og þá athuga hvort 
     *  þeir eru eins eða ekki 
     * @param hnappur 
     */
    private void kannaHnappa(JButton hnappur) {
        fjoldiSmella = fjoldiSmella + 1;
        if (fjoldiSmella == 2) {
            opnaEdaLoka(hnappur);
            fjoldiSmella = 0;
        } else if (fjoldiSmella == 1) {
            fyrsti = hnappur;
        }
    }

    /**
     * Opnar eða lokar hnappi eftir því hvort hnappurinn var sá sami og síðasti
     * hnappur. Hækka stig og loka reitum ef við á. 
     *
     * @param hnappur
     */
    private void opnaEdaLoka(JButton hnappur) {
        // Ná í myndirnar 
        ImageIcon mynd1 = hverErMynd(fyrsti);
        ImageIcon mynd2 = hverErMynd(hnappur);  

        // Athuga hvort myndirnar eru þær sömu
        if (mynd1.getImage() != mynd2.getImage()) {
            // Fjölga stigum og birta í viðmóti 
            laekkaStig();
            // Loka reitum 
            lokaReitum(hnappur);
        }
        else {
            //hækka opnir reitir ef myndir eru þær sömu
            mittBord.haekkaOpnir();
            if (mittBord.allirOpnir()) {
                SpilUtlit s = (SpilUtlit)hnappur.getTopLevelAncestor();
                s.leikLokid("Leik lokið");
                mittBord.nyrLeikur();

            }
        }
    }

    /**
     * Loka reitunum tveimur, fyrsti og hnappur 
     * @param hnappur 
     */
    private void lokaReitum(JButton hnappur) {
        // Bíðum í eina sekúndu 
        bidaISek();
        
        // setjum bakgrunninn á reitunum 
        fyrsti.setIcon(buaTilMynd("eye.png"));
        hnappur.setIcon(buaTilMynd("eye.png"));
    }

    /**
     * Bíða í eina sekúndu
     */
    private void bidaISek() {
        try {
            Thread.sleep(Timer.ONE_SECOND);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReiturListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Breytir um mynd á hnappi.
     *
     * @param hnappur
     * @param c
     */
    private void breytaUmMynd(JButton hnappur, ImageIcon c) {
        hnappur.setIcon(c);
        hnappur.paint(hnappur.getGraphics());
    }

    /**
     * Lækkar stigin um einn í vinnslu og birta stigin í viðmóti
     */
    private void laekkaStig() {
        int i = mittBord.laekkaStig();
        ((SpilUtlit) fyrsti.getTopLevelAncestor()).birtaStigin(i);
    }

    /**
     * Skilar myndinni á hnappi hnappur
     *
     * @param hnappur
     * @return mynd á hnappi
     */
    private ImageIcon hverErMynd(JButton hnappur) {
        ImageIcon m = mittBord.getMynd(Integer.valueOf(hnappur.getName()));
        return m;
    }
    /**
     * Finnur myndina og skilar henni
     * @param path
     * @return 
     */
    private static ImageIcon buaTilMynd(String path) {
        java.net.URL imgURL = SpilUtlit.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Fann ekki skrána " + path);
            return null;
        }
    }

}
