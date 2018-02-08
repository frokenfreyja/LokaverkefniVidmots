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
public class PlayerHandler implements ActionListener {
    private int fjoldiSmella = 0;
    private JButton fyrsti;
    Bord mittBord;
    
    int player1Stig;
    int player2Stig;
    boolean player1 = true;
     
   
    public PlayerHandler(Bord s) {
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
        
        //Spila hreyfihljóð þegar ýtt er á reit
        SpilUtlit r = (SpilUtlit)hnappur.getTopLevelAncestor();
        if (r.soundClipMove.isRunning()) r.soundClipMove.stop();
        r.soundClipMove.setFramePosition(0); // rewind to the beginning
        r.soundClipMove.start();
        
        // Opna hnappinn
        breytaUmMynd(hnappur, hverErMynd(hnappur));
        //breytaUmMynd(fyrsti, hverErMynd(fyrsti));
        
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
        SpilUtlit s = (SpilUtlit)hnappur.getTopLevelAncestor();
        // Ná í myndirnar 
        ImageIcon mynd1 = hverErMynd(fyrsti);
        ImageIcon mynd2 = hverErMynd(hnappur);
        //Ef tvær myndir eru eins þá er athugað hver var að gera og svo 
        //athugað hvort vinningshafi sé kominn
        if(mynd1.getImage() != mynd2.getImage()) {
            lokaReitum(hnappur);
            if(player1) {
                laekkaStig1();
                player1 = false;
                s.hverErNaestur("Leikmaður 2 á að gera");
            } else {
                laekkaStig2();
                player1 = true;
                s.hverErNaestur("Leikmaður 1 á að gera");
            }
           
        } else {
            //mittBord.haekkaOpnir();
            if(player1) {
                player1Stig++;
                player1 = false;
                haekkaStig1();
                s.hverErNaestur("Leikmaður 2 á að gera");
                if(((player1Stig+player2Stig)*2) == mittBord.getStaerd()) {
                    if(player1Stig > player2Stig) {
                        s.leikLokid("Leik lokið. Leikmaður 1 vann!");
                        nullStillaStig();
                        mittBord.nyrLeikur();
                    } else if (player1Stig < player2Stig) {
                        s.leikLokid("Leik lokið. Leikmaður 2 vann!");
                        nullStillaStig();
                        mittBord.nyrLeikur();
                    } else {
                        s.leikLokid("Jafntefli");
                        nullStillaStig();
                        mittBord.nyrLeikur();
                    }
                }
            } else {
                player2Stig++;
                player1 = true;
                haekkaStig2();
                s.hverErNaestur("Leikmaður 1 á að gera");
                if(((player1Stig+player2Stig)*2) == mittBord.getStaerd()) {
                    if(player1Stig > player2Stig) {
                            s.leikLokid("Leik lokið. Leikmaður 1 vann!");
                            nullStillaStig();
                            mittBord.nyrLeikur();
                    } else if (player1Stig < player2Stig) {
                            s.leikLokid("Leik lokið. Leikmaður 2 vann!");
                            nullStillaStig();
                            mittBord.nyrLeikur();
                    } else {
                            s.leikLokid("Jafntefli");
                            nullStillaStig();
                            mittBord.nyrLeikur();
                    }
                }
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
     * Hækka stig leikmanns 1 og birta í SpilUtlit
     */
    private void haekkaStig1() {
        int i = player1Stig;
        ((SpilUtlit) fyrsti.getTopLevelAncestor()).birtaStig1(i);
    }
    
    /**
     * Hækka stig leikmanns 2 og birta í SpilUtlit
     */
    private void haekkaStig2() {
        int i = player2Stig;
        ((SpilUtlit) fyrsti.getTopLevelAncestor()).birtaStig2(i);
    }
    
    /**
     * Lækkarr stigin um einn í vinnslu og birta stigin í viðmóti
     */
    private void laekkaStig1() {
        int i = -player1Stig;
        ((SpilUtlit) fyrsti.getTopLevelAncestor()).birtaStigin(i);
    }
    
    private void laekkaStig2() {
        int i = -player2Stig;
        ((SpilUtlit)fyrsti.getTopLevelAncestor()).birtaStigin(i);
    }
    /**
     * Núllstilla stig beggja leikmanna
     */
    private void nullStillaStig() {
        player1Stig = 0;
        player2Stig = 0;
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
    
    
}
