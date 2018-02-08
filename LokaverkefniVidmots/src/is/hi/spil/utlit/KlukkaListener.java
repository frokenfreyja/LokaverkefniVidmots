/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.hi.spil.utlit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Listener klasi fyrir Klukku 
 * @author Freyja Sigurgísladóttir, frs24@hi.is
 */
public class KlukkaListener implements ActionListener {

    SpilUtlit utlit;            // Viðmótsklasi sem tekur við skilaboðum
    
    public KlukkaListener(SpilUtlit u) {
        utlit = u;
    }
    /**
     * Atburðarhandler sem grípur tímaatburð sem rennur út
     * Láta viðmótsklasa vita og stöðva klukkuna
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {      
        Timer t = (Timer)e.getSource();
        t.stop();
        utlit.timiUtrunninn();
    }
    
}
