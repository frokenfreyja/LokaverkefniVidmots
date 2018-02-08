package is.hi.spil.utlit;

import is.hi.spil.vinnsla.Bord;
import java.awt.CardLayout;
import java.awt.Color;
import java.net.URL;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.Timer;
import javax.sound.sampled.*;
import javax.swing.UIManager;


/**
 * Notendaviðmótsklasi fyrir minnisspil. Sér um stýringar á valmyndum
 * og radio button
 * @author Freyja Sigurgísladóttir, frs24@hi.is
 */
public class SpilUtlit extends javax.swing.JFrame {
    private JButton [] audveldir; // Auðveldir hnappar
    private JButton [] midlungs;  // Miðlungs hnappar
    private JButton [] erfidir;   // Erfiðir hnappar
    private Bord audveltBord;     // Auðvelt borð
    private Bord midlungsBord;    // Miðlungs borð
    private Bord erfittBord;      // Erfitt borð
    
    private final static int A = 4;     // Stærð á auðveldu borði
    private final static int M = 12;    // Stærð á miðlungs borði 
    private final static int E = 20;    // Stærð á erfiðu borði
    
    
    private final static int TIMI_A = 100000;
    private final static int TIMI_M = 100000;
    private final static int TIMI_E = 100000;
    
    private Timer minnTimer;
    
    private static final String [] valkostir = {"Já    ", "Hætta við"}; 
    
    String fileMove = "/type.wav";         //nafn á hreyfihljóði
    String fileGameOver = "/yeah.wav"; //nafn á vinningshljóði
    String fileLag = "/tour.wav";    //nafn á lagi 
    Clip soundClipMove;     
    Clip soundClipGameOver;  
    Clip soundClipLag;



    /**
     * Smiður fyrir  SpilUtlit
     */
    public SpilUtlit() {
        initComponents();
        this.getContentPane().setBackground(Color.BLACK); //setja svartan bakgrunnslit
        
        //Setja texta á jLabel hluti
        jLabel1.setText(java.util.ResourceBundle.getBundle
        ("is/hi/spil/utlit/resources/TextiVidmot").getString("Stig"));
        jLabel2.setText(java.util.ResourceBundle.getBundle
        ("is/hi/spil/utlit/resources/TextiVidmot").getString("hverhvad"));
        jLabel3.setText(java.util.ResourceBundle.getBundle
        ("is/hi/spil/utlit/resources/TextiVidmot").getString("Leikmadur1"));
        jLabel4.setText(java.util.ResourceBundle.getBundle
        ("is/hi/spil/utlit/resources/TextiVidmot").getString("Leikmadur2"));
        
        // Set nöfn á radio button sem eru notaðir fyrir card layout 
        jA.setName("Audveldur"); 
        jM.setName("Midlungs");
        jE.setName("Erfidur");
        
        // Setja upp borðið fyrir auðveldan leik 
        audveltBord = new Bord(A, TIMI_A);
        audveldir = new JButton [] {jButton1, jButton2, jButton3, jButton4};
        utlitOgHandlerABordi(audveltBord, audveldir);

        // Setja upp borðið fyrir miðlungs leik 
        midlungsBord = new Bord(M,TIMI_M);
        midlungs = new JButton []{jButton5, jButton6, jButton7, jButton8, 
            jButton9, jButton10, jButton11, jButton12, jButton13, jButton14, 
        jButton15, jButton16};   
        utlitOgHandlerABordi(midlungsBord, midlungs);
        
        // Setja upp borðið fyrir erfiðan leik
        erfittBord = new Bord(E, TIMI_E);
        erfidir = new JButton [] {jButton25, jButton26, jButton27, jButton28, 
            jButton29, jButton30, jButton31, jButton32, jButton33, jButton34, 
            jButton35, jButton36, jButton37, jButton38, jButton39, jButton40, 
            jButton41, jButton42, jButton43, jButton44};
        utlitOgHandlerABordi(erfittBord, erfidir);
       
       // Setja stigin
       birtaStigin(0);
        
       // Ræsa klukkuna
       raesaKlukku(TIMI_A);
       
       // Sækja hljóðbútana 
       try {
        URL url = getClass().getResource(fileGameOver);
        if (url == null) {
          System.err.println("Couldn't find file: " + fileGameOver);
        } else {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            soundClipGameOver = AudioSystem.getClip();
            soundClipGameOver.open(audioIn);
        }
       
        url = getClass().getResource(fileMove);
        if (url == null) {
            System.err.println("Couldn't find file: " + fileMove);
        } else {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            soundClipMove = AudioSystem.getClip();
            soundClipMove.open(audioIn);
            }
        url = getClass().getResource(fileLag);
        if (url == null) {
            System.err.println("Couldn't find file: " + fileLag);
        } else {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            soundClipLag = AudioSystem.getClip();
            soundClipLag.open(audioIn);
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio Format not supported!");
        } catch (Exception e) {
         e.printStackTrace();
        }
       
    }

    /**
     * Setur listenera, nöfn á hnöppum og grunnMynd fyrir hnapparFylki 
     * @param b Borðið 
     * @param hnapparFylki fylki með hnöppum borðsins  
     */
    private void utlitOgHandlerABordi(Bord b, JButton [] hnapparFylki) {
        setjaListener(b, hnapparFylki);
        setjaHnapparName(hnapparFylki);
        grunnMynd(hnapparFylki);
        
    }
    /**
     * Setur listenera, nöfn á hnöppum og grunnMynd fyrir hnapparFylki
     * @param b Borðið
     * @param hnapparFylki fylki með hnöppum borðsins
     */
    
    private void spilaTveir(Bord b, JButton[] hnapparFylki) {
        setjaPlayerListener(b, hnapparFylki);
        setjaHnapparName(hnapparFylki);
        grunnMynd(hnapparFylki);
    }

    /**
     * Setja nöfn fyrir borð hnappaFylki að stærð u 
     * Nöfn eru sett á hnappa til að nota sem index í fylki fyrir myndir hnappa
     * @param hnappaFylki fylki af JButton hnöppum 
     * @param u stærð á fylki 
    */
    private void setjaHnapparName(JButton [] hnappaFylki) {
        for (int i = 0; i < hnappaFylki.length; i++) {
            hnappaFylki[i].setName(String.valueOf(i));
        }
    }
    
    /**
     * Aðferð sem sækir mynd og býr hana til með ImageIcon
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupErfStig = new javax.swing.ButtonGroup();
        jSpil = new javax.swing.JPanel();
        jAudveldur = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jMidlungs = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jErfidur = new javax.swing.JPanel();
        jButton26 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jStigin = new javax.swing.JTextField();
        jM = new javax.swing.JRadioButton();
        jA = new javax.swing.JRadioButton();
        jE = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        j2play = new javax.swing.JButton();
        jTonlist = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPlayer = new javax.swing.JLabel();
        Leikmadur1 = new javax.swing.JLabel();
        Leikmadur2 = new javax.swing.JLabel();
        jStoppa = new javax.swing.JButton();
        j1play = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jHaetta = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jLeikreglur = new javax.swing.JMenuItem();
        jUm = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jEN = new javax.swing.JMenuItem();
        jISL = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("\n");
        setResizable(false);

        jSpil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jSpil.setLayout(new java.awt.CardLayout());

        jAudveldur.setLayout(new java.awt.GridLayout(2, 2));
        jAudveldur.add(jButton1);
        jAudveldur.add(jButton2);
        jAudveldur.add(jButton3);
        jAudveldur.add(jButton4);

        jSpil.add(jAudveldur, "Audveldur");

        jMidlungs.setLayout(new java.awt.GridLayout(3, 4));
        jMidlungs.add(jButton5);
        jMidlungs.add(jButton8);
        jMidlungs.add(jButton10);
        jMidlungs.add(jButton11);
        jMidlungs.add(jButton9);
        jMidlungs.add(jButton7);
        jMidlungs.add(jButton6);
        jMidlungs.add(jButton12);
        jMidlungs.add(jButton13);
        jMidlungs.add(jButton14);
        jMidlungs.add(jButton15);
        jMidlungs.add(jButton16);

        jSpil.add(jMidlungs, "Midlungs");

        jErfidur.setLayout(new java.awt.GridLayout(4, 5));
        jErfidur.add(jButton26);
        jErfidur.add(jButton30);
        jErfidur.add(jButton27);
        jErfidur.add(jButton34);
        jErfidur.add(jButton28);
        jErfidur.add(jButton38);
        jErfidur.add(jButton39);
        jErfidur.add(jButton29);
        jErfidur.add(jButton31);
        jErfidur.add(jButton32);
        jErfidur.add(jButton33);
        jErfidur.add(jButton25);
        jErfidur.add(jButton35);
        jErfidur.add(jButton36);
        jErfidur.add(jButton37);
        jErfidur.add(jButton40);
        jErfidur.add(jButton41);
        jErfidur.add(jButton42);
        jErfidur.add(jButton43);
        jErfidur.add(jButton44);

        jSpil.add(jErfidur, "Erfidur");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Stigin:");

        jStigin.setEditable(false);

        groupErfStig.add(jM);
        jM.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jM.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("is/hi/spil/utlit/resources/TextiVidmot"); // NOI18N
        jM.setLabel(bundle.getString("<html> Midlungs <br> leikur </html>")); // NOI18N
        jM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVeljaErfidleikastigActionPerformed(evt);
            }
        });

        groupErfStig.add(jA);
        jA.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jA.setForeground(new java.awt.Color(255, 255, 255));
        jA.setSelected(true);
        jA.setToolTipText("");
        jA.setLabel(bundle.getString("<html>  Audveldur <br> leikur </html>")); // NOI18N
        jA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVeljaErfidleikastigActionPerformed(evt);
            }
        });

        groupErfStig.add(jE);
        jE.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jE.setForeground(new java.awt.Color(255, 255, 255));
        jE.setText("<html>  Erfiður <br> leikur </html>");
        jE.setLabel(bundle.getString("<html>  Erfiður <br> leikur </html>")); // NOI18N
        jE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVeljaErfidleikastigActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Silom", 0, 45)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 204, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("H  V  E  R  H  V  A  Ð");

        j2play.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        j2play.setText(bundle.getString("Tveir leikmenn")); // NOI18N
        j2play.setLabel(bundle.getString("Tveir leikmenn")); // NOI18N
        j2play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j2playActionPerformed(evt);
            }
        });

        jTonlist.setBackground(new java.awt.Color(255, 255, 255));
        jTonlist.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jTonlist.setText("Spila lag");
        jTonlist.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTonlist.setLabel(bundle.getString("Spila lag")); // NOI18N
        jTonlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTonlistActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 17)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Leikmaður eitt:");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Leikmaður tvö:");

        jPlayer.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jPlayer.setForeground(new java.awt.Color(255, 255, 255));
        jPlayer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        Leikmadur1.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        Leikmadur1.setForeground(new java.awt.Color(255, 255, 255));

        Leikmadur2.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        Leikmadur2.setForeground(new java.awt.Color(255, 255, 255));

        jStoppa.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jStoppa.setText("Stoppa lag");
        jStoppa.setLabel(bundle.getString("Stoppa lag")); // NOI18N
        jStoppa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStoppaActionPerformed(evt);
            }
        });

        j1play.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        j1play.setText("Einn leikmaður");
        j1play.setLabel(bundle.getString("Einn leikmaður")); // NOI18N
        j1play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j1playActionPerformed(evt);
            }
        });

        jMenu1.setText("Skrá");
        jMenu1.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jMenu1.setLabel(bundle.getString("Skrá")); // NOI18N

        jHaetta.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jHaetta.setText("Hætta");
        jHaetta.setLabel(bundle.getString("Hætta")); // NOI18N
        jHaetta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHaettaActionPerformed(evt);
            }
        });
        jMenu1.add(jHaetta);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Hjálp");
        jMenu3.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jMenu3.setLabel(bundle.getString("Hjálp")); // NOI18N

        jLeikreglur.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLeikreglur.setText("Leikreglur");
        jLeikreglur.setLabel(bundle.getString("Leikreglur")); // NOI18N
        jLeikreglur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLeikreglurActionPerformed(evt);
            }
        });
        jMenu3.add(jLeikreglur);

        jUm.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jUm.setText("Um forritið");
        jUm.setLabel(bundle.getString("Um forritið")); // NOI18N
        jUm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUmActionPerformed(evt);
            }
        });
        jMenu3.add(jUm);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Tungumál");
        jMenu2.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jMenu2.setLabel(bundle.getString("Tungumál")); // NOI18N

        jEN.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jEN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/is/hi/spil/utlit/myndir/adsg.png"))); // NOI18N
        jEN.setText("English");
        jEN.setLabel(bundle.getString("English")); // NOI18N
        jEN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jENActionPerformed(evt);
            }
        });
        jMenu2.add(jEN);

        jISL.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jISL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/is/hi/spil/utlit/myndir/flag-of-iceland2.jpg"))); // NOI18N
        jISL.setText("Íslenska");
        jISL.setLabel(bundle.getString("Íslenska")); // NOI18N
        jISL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jISLActionPerformed(evt);
            }
        });
        jMenu2.add(jISL);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(j2play, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(j1play))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Leikmadur1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(Leikmadur2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(jPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jSpil, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jA)
                            .addComponent(jM, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jStoppa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTonlist, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jStigin, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jA, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jTonlist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jStoppa)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jStigin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 92, Short.MAX_VALUE))
                    .addComponent(jSpil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j2play)
                    .addComponent(j1play))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Leikmadur1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Leikmadur2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jA, jM});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Velur nýtt borð, auðvelt, miðlungs eða erfitt og ræsir nýjan leik 
     * Birtir 0 stig 
     * @param evt 
     */
    private void jVeljaErfidleikastigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jVeljaErfidleikastigActionPerformed
    
        int t;
        // Náum í CardLayout fyrir panelinn
        CardLayout cl = (CardLayout)jSpil.getLayout();
        
        // Náum í nafnið á hnappnum
        String nafnCard = ((JRadioButton)evt.getSource()).getName();
  
        // Náum í spjaldið (card) sem er með sama nafni
        cl.show(jSpil, nafnCard);
        
        // ræsir nýjan leik á viðkomandi borði 
        if (nafnCard == "Audveldur") {
            audveltBord.nyrLeikur();
            t=audveltBord.getTimi();
        }
        else if (nafnCard == "Midlungs") {
            midlungsBord.nyrLeikur();
            t=midlungsBord.getTimi();
        }
        else {
            erfittBord.nyrLeikur();
            t=erfittBord.getTimi();
        }
        
        // Ræsa klukkuna 
        raesaKlukku(t);
        
        // Birtir 0 stig 
        birtaStigin(0);
        
    }//GEN-LAST:event_jVeljaErfidleikastigActionPerformed

    /**
     * Býr til klukku af lengd t og ræsir hana 
     * @param t 
     */
    private void raesaKlukku(int t) {
        minnTimer = new javax.swing.Timer(t, new KlukkaListener(this));
        minnTimer.start();
    }
    /**
     * Endurræsa klukku 
     */
    private void endurRaesaKlukku() {
        minnTimer.restart();
    }
    /**
     * 
     * Atburðarhandler fyrir hætta. Hættir í forritinu
     *
     * @param evt
     */

    private void jHaettaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHaettaActionPerformed
    int n = JOptionPane.showOptionDialog(this, // Foreldrahlutur dialogs
              "Ertu viss?",     // Skilaboðin              
              "Eyða",            // Titillinn á dialog  
              JOptionPane.YES_NO_CANCEL_OPTION,  // Option type
              JOptionPane.QUESTION_MESSAGE,      // Message type 
              null,            
              valkostir,          // Fylki með valmöguleikum
              valkostir[0]);       // Sjálfgefna gildið
    if(n == 0) {
        System.exit(0);
    }
    }//GEN-LAST:event_jHaettaActionPerformed
    /**
     * Atburðarhandler fyrir Tveir leikmenn hnappinn. Gerir tveimur leikmönnum 
     * kleyft að spila saman
     * @param evt 
     */
    private void j2playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j2playActionPerformed
    int n = JOptionPane.showOptionDialog(this, // Foreldrahlutur dialogs
              "Ertu viss?",     // Skilaboðin              
              "Tveir spila saman",            // Titillinn á dialog  
              JOptionPane.YES_NO_CANCEL_OPTION,  // Option type
              JOptionPane.QUESTION_MESSAGE,      // Message type 
              null,            
              valkostir,          // Fylki með valmöguleikum
              valkostir[0]);       // Sjálfgefna gildið
    if(n == 0) {
        jA.setName("Audveldur"); 
        jM.setName("Midlungs");
        jE.setName("Erfidur");
        
        
        eydaListener(audveltBord, audveldir);
        spilaTveir(audveltBord, audveldir);
        
        eydaListener(midlungsBord, midlungs);
        spilaTveir(midlungsBord, midlungs);
        
        eydaListener(erfittBord, midlungs);
        spilaTveir(erfittBord, erfidir);
        
       // Setja stigin
       birtaStigin(0);
       // Ræsa klukkuna
       raesaKlukku(TIMI_A);
    }
    }//GEN-LAST:event_j2playActionPerformed
    /**
     * Atburðarhandler fyrir "leikreglur"
     * @param evt 
     */
    private void jLeikreglurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLeikreglurActionPerformed
        JOptionPane.showMessageDialog(this, "Leikmaður á að finna tvær eins myndir.\n"
                +"Hann missir stig í hvert sinn sem hann opnar tvo reiti sem hafa ekki sömu mynd.",
                "Stigagjöf", JOptionPane.INFORMATION_MESSAGE, null);
    }//GEN-LAST:event_jLeikreglurActionPerformed
    /**
     * Atburðarhandler fyrir "Um forrit"
     * @param evt 
     */
    private void jUmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUmActionPerformed
                JOptionPane.showMessageDialog(this, "Dagsetning: 12. apríl, 2017 \n"
                + "Höfundur leiks: Freyja Sigurgísladóttir \nAldur: 22 ára \n"
                + "Menntun: Stúdent frá Menntaskólanum í Reykjavík og\nfyrsta árs"
                + " nemi á Tölvunarfræðibraut við Háskóla Íslands \nLag: Tour De France - Kraftwerk"
                        , "Upplýsingar", 
                JOptionPane.INFORMATION_MESSAGE, buaTilMynd("skinfaxi.png"));
    }//GEN-LAST:event_jUmActionPerformed
    /**
     * Atburðarhandler fyrir spila tónlist takkann
     * @param evt 
     */
    private void jTonlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTonlistActionPerformed

        soundClipLag.start();

    }//GEN-LAST:event_jTonlistActionPerformed
    /**
     * Atburðarhandler fyrir stoppa tónlist takkann
     * @param evt 
     */
    private void jStoppaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStoppaActionPerformed
        soundClipLag.stop();
    }//GEN-LAST:event_jStoppaActionPerformed
    /**
     * Atburðarhandler fyrir ensku
     * @param evt 
     */
    private void jENActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jENActionPerformed
        NyttTungumal t = new NyttTungumal (this, false);
        t.setVisible(true);
    }//GEN-LAST:event_jENActionPerformed
    /**
     * Atburðarhandler fyrir íslensku
     * @param evt 
     */
    private void jISLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jISLActionPerformed
        dispose();
        Locale l = new Locale("is_IS");
        Locale.setDefault(l);
        SpilUtlit h;
        h = new SpilUtlit();
        h.setVisible(true);
    }//GEN-LAST:event_jISLActionPerformed

    private void j1playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j1playActionPerformed
        dispose();
        SpilUtlit h = new SpilUtlit();
        h.setVisible(true);
    }//GEN-LAST:event_j1playActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    //break;
                }
            }
        
            javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //javax.swing.UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SpilUtlit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SpilUtlit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SpilUtlit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SpilUtlit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SpilUtlit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Leikmadur1;
    private javax.swing.JLabel Leikmadur2;
    private javax.swing.ButtonGroup groupErfStig;
    private javax.swing.JButton j1play;
    private javax.swing.JButton j2play;
    private javax.swing.JRadioButton jA;
    private javax.swing.JPanel jAudveldur;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JRadioButton jE;
    private javax.swing.JMenuItem jEN;
    private javax.swing.JPanel jErfidur;
    private javax.swing.JMenuItem jHaetta;
    private javax.swing.JMenuItem jISL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jLeikreglur;
    private javax.swing.JRadioButton jM;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jMidlungs;
    private javax.swing.JLabel jPlayer;
    private javax.swing.JPanel jSpil;
    private javax.swing.JTextField jStigin;
    private javax.swing.JButton jStoppa;
    private javax.swing.JButton jTonlist;
    private javax.swing.JMenuItem jUm;
    // End of variables declaration//GEN-END:variables

    /**
    * Býr til og setur listeners fyrir hnappana hnapparFylki 
    * @param b Bordid 
    * @param hnapparFylki 
    */
    private void setjaListener(Bord b, JButton [] hnapparFylki) {
        ReiturListener a = new ReiturListener(b);
        for (int i = 0; i<hnapparFylki.length; i++) {
            hnapparFylki[i].addActionListener(a);
        }
    }
    private void eydaListener(Bord b, JButton [] hnapparFylki) {
        ReiturListener a = new ReiturListener(b);
        for (int i = 0; i<hnapparFylki.length; i++) {
            hnapparFylki[i].removeActionListener(a);
            
        }
        
    }
    /**
     * Býr til og setur listeners fyrir hnappana hnapparFylki
     * @param b Borðið
     * @param hnapparFylki 
     */
    private void setjaPlayerListener(Bord b, JButton [] hnapparFylki) {
        PlayerHandler a = new PlayerHandler(b);
        for (int i = 0; i<hnapparFylki.length; i++) {
            hnapparFylki[i].addActionListener(a);
        }
    }
   
    /**
    * Birtir stigin i í stigaglugga jStigin
    * @param i stigafjöldi
    */
    public void birtaStigin(int i) {
        jStigin.setText(String.valueOf(i)); 
    }
    /**
     * Birtir stigin i fyrir tveggja manna leik
     * @param i 
     */
    public void birtaStigin2(int i) {
        jStigin.setText(String.valueOf(i/2));
    }
    /**
    * Leik er lokið. Birta skilaboð
    * @param s
    */
    public void leikLokid(String s) {
        if (soundClipGameOver.isRunning()) soundClipGameOver.stop();
        soundClipGameOver.setFramePosition(0); // rewind to the beginning
        soundClipGameOver.start();
        
        JOptionPane.showMessageDialog(this, s);
        jStigin.setText("");
        Leikmadur1.setText("");
        Leikmadur2.setText("");
        
        // Núllstillum öll borð
        grunnMynd(audveldir);
        grunnMynd(midlungs);
        grunnMynd(erfidir);  
        
        endurRaesaKlukku();
    }

    /**
     * Setur grunnmynd á hnappa í hnapparFylki af stærð u
     * @param hnapparFylki
     * @param u 
     */
    private void grunnMynd(JButton [] hnapparFylki) {
        for (int i=0; i<hnapparFylki.length; i++) {
            ImageIcon icon = buaTilMynd("eye.png");
            hnapparFylki[i].setIcon(icon);
        }
    }

    public void timiUtrunninn() {
        leikLokid("Leik lokið - tíminn útrunninn");
        
        audveltBord.nyrLeikur();
        midlungsBord.nyrLeikur();
        erfittBord.nyrLeikur();
    }
    /**
     * Birtir stig leikmanns 1 á jLabel
     * @param i 
     */
    public void birtaStig1(int i) {
        Leikmadur1.setText(String.valueOf(i));
    }
    /**
     * Birtir stig leikmanns 2 á jLabel
     * @param i 
     */
    public void birtaStig2(int i) {
        Leikmadur2.setText(String.valueOf(i));
    }
    /**
     * Birtir hver á að gera næst á jLabel
     * @param s 
     */
    public void hverErNaestur(String s) {
        jPlayer.setText(s);
    }
        
}
