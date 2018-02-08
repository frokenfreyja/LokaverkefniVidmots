package is.hi.spil.vinnsla;

import javax.swing.ImageIcon;

/**
 * Interface fyrir vinnsluklasi í minnisleiknum. Heldur utan um stærð borðsins,
 * stigin, myndirnar á borðinu, og fjöldi rétt opinna reita
 * 
 * @author Freyja Sigurgísladóttir, frs24@hi.is
 */
public interface BordInterface {

    /**
     * Skilar satt ef allir reitir eru rétt opnir og þar með leik lokið
     *
     * @return
     */
    boolean allirOpnir();

    /**
     * Skilar mynd nr. i í fylki myndir
     *
     * @param i
     * @return myndir (ImageIcon)
     */
    ImageIcon getMynd(Integer i);

    int getStaerd();

    /**
     * Hækkar teljara (opnir) um einn sem heldur utan um hve margir reitir eru
     * rétt opnir, þ.e. myndir eru þær sömu.
     */
    void haekkaOpnir();

    /**
     * Lækkar stig um einn
     *
     * @return stig
     */
    int laekkaStig();
    
    

    /**
     * Ræsir nýjan leik Núllstillir stigin og setur random myndir í fylki myndir
     * sem hefur stök af taginu ImageIcon
     */
    void nyrLeikur();
    
    
}
