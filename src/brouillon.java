
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.lang.Math;


public class brouillon extends JFrame { 
    JButton close;
    JButton a_deplacer = null;
    JLabel titre;
    JLabel info;
    JLabel info2;
    boolean joueur = true;
    boolean phase1 = true;
    int incr = 0;
    boolean partie_finie = false;

    Color bleu_pastel = new Color(153, 204, 255); 
    Color rouge_pastel = new Color(255, 153, 153); 

    ArrayList<String> coordonees = new ArrayList<String>();
    JButton[] cases = new JButton[9]; 

    ArrayList<JButton> cases_rouges_j2 = new ArrayList<JButton>();
    ArrayList<JButton> cases_bleues_j1 = new ArrayList<JButton>();



    public static void main(String argv[]) {
        Araignee b = new Araignee(); 
    }


    public brouillon() {

        setLayout(new FlowLayout());
        JPanel p = new JPanel();
        JPanel p2 = new JPanel(new GridLayout(3, 3));
        add(p);
        add(p2);
        
        titre = new JLabel("jeu de l'airaignée");
        p.add(titre);
        info = new JLabel("Phase 1");
        p.add(info);
        info2 = new JLabel("Le joueur 1 (bleu) peut commencer");
        p.add(info2);
        

        class DoItListener implements ActionListener { 
            public void actionPerformed(ActionEvent e) {
                JButton boutonClique = (JButton) e.getSource();

                if (phase1){
                    Jouer_Coup_phase1(boutonClique, joueur);
                    joueur =  !joueur;

                    if (incr==6){
                        info.setText("Phase 2");
                        phase1 = !phase1;
                    }
                }
                else if (!partie_finie){
                    
                    // AJouter un label
                    if (a_deplacer!=null){
                        if (boutonClique.getBackground() == Color.WHITE && trouve_voisines(a_deplacer).contains(boutonClique)){
                            
                            if (joueur) {
                            set_j1(boutonClique);
                            joueur = !joueur;
                            set_vide(a_deplacer);
                            cases_rouges_j2.remove(a_deplacer);
                            cases_rouges_j2.add(boutonClique);
                            a_deplacer = null;
                            }

                            else {
                            set_j2(boutonClique);
                            joueur = !joueur;
                            set_vide(a_deplacer);
                            cases_bleues_j1.remove(a_deplacer);
                            cases_bleues_j1.add(boutonClique);
                            a_deplacer = null;
                            }
                        }
                        else if (a_deplacer == boutonClique) {
                            if (a_deplacer.getText() == "o"){
                                set_j1(a_deplacer);
                            }
                            else {
                                set_j2(a_deplacer);
                            }
                            
                            a_deplacer = null;
                        }
                        
                    }
                    else {
                        if (boutonClique.getBackground() == Color.WHITE){
                            System.out.println("mauvaise case");
                            //mettre un label  
                        }
                        
                        else if (boutonClique.getText() == "o" && joueur) {
                            a_deplacer = boutonClique;
                            boutonClique.setBackground(Color.BLUE);
                        }
                        else if (boutonClique.getText() == "x" && !joueur){
                            a_deplacer = boutonClique;
                            boutonClique.setBackground(Color.RED);
                        }
                        else {//mettre un label
                        System.out.println("biiip");
                    }
                    }
                }

                if (cases_rouges_j2.size()==3 && cases_bleues_j1.size() == 3){
                    if (gagnant(cases_rouges_j2.get(0), cases_rouges_j2.get(1),cases_rouges_j2.get(2))){
                        
                            info2.setText("Le joueur 2 (rouge) a gagné !");
                            partie_finie = true;
                    }

                    else if (gagnant(cases_bleues_j1.get(0),cases_bleues_j1.get(1),cases_bleues_j1.get(2))) {
                        
                            info2.setText(("Le joueur 1 (bleu) a gagné !"));
                            partie_finie = true;

                    }

                }

            }

        }

        class CloseListener implements ActionListener { 
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }


        for (int i=0;i<=8;i++){
            cases[i]= new JButton("#"); 
            p2.add(cases[i]);
            cases[i].setName("case"+i);
            cases[i].addActionListener(new DoItListener());
            cases[i].setOpaque(true);
            cases[i].setBackground(Color.WHITE);
            cases[i].setForeground(Color.BLACK);
        }

        
        for (int i=0;i<=8;i++){
            int x = i%3;
            int y = i/3;
            String a = String.valueOf(x);
            String b = String.valueOf(y);
            coordonees.add(a+b);
        }

        close = new JButton("Close");
        close.addActionListener(new CloseListener()); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); setVisible(true);
        
    }
    


    

    public void Jouer_Coup_phase1(JButton bouton, boolean joueur) {
        if (joueur) {set_j1(bouton); cases_rouges_j2.add(bouton);}
        else {set_j2(bouton); cases_bleues_j1.add(bouton);}
        incr+=1;
    }
    
    public void set_j1(JButton bouton){
        bouton.setText("o");
        Color bleu_pastel = new Color(153, 204, 255); 
        bouton.setBackground(bleu_pastel);
    }

    public void set_j2(JButton bouton){
        bouton.setText("x");
        Color rouge_pastel = new Color(255, 153, 153); 
        bouton.setBackground(rouge_pastel);
    }

    public void set_vide(JButton bouton) {
        bouton.setText("#");
        bouton.setBackground(Color.WHITE);
    }


    public ArrayList<JButton> trouve_voisines(JButton bouton) {
        ArrayList<JButton> voisines = new ArrayList<JButton>();
        
        int a = get_coo(bouton)[0];
        int b = get_coo(bouton)[1];


        for (int i=0;i<=8;i++){
            JButton case_test = cases[i];
            int x = get_coo(case_test)[0];
            int y = get_coo(case_test)[1];

            if (distance(a, b, x, y) < 1.5 && distance(a, b, x, y) > 0) {
                voisines.add(case_test);
            }
        }

        return voisines;
    }

    public int[] get_coo(JButton bouton){
        String nom = bouton.getName();
        char numero_str = nom.charAt(4);
        int numero = Character.getNumericValue(numero_str);

        String coo_str = coordonees.get(numero);
        int coox = coo_str.charAt(0);
        int cooy = coo_str.charAt(1);
        int[] coo = {coox, cooy};
        return coo;
    }

    public boolean gagnant(JButton bouton1, JButton bouton2, JButton bouton3){
        boolean gagne = false;
        if ((get_coo(cases_rouges_j2.get(0))[0] == get_coo(cases_rouges_j2.get(1))[0] 
                            && get_coo(cases_rouges_j2.get(0))[0] == get_coo(cases_rouges_j2.get(2))[0]) 
                            || (get_coo(cases_rouges_j2.get(0))[1] == get_coo(cases_rouges_j2.get(1))[1] 
                            && get_coo(cases_rouges_j2.get(0))[1] == get_coo(cases_rouges_j2.get(2))[1])){
            gagne = true;
        }
        return gagne;
    }
/* 
    public int[][] turn_to_vect(JButton bouton1, JButton bouton2, JButton bouton3){
        int[] coo1 = get_coo(bouton1);
        int[] coo2 = get_coo(bouton2);
        int[] coo3 = get_coo(bouton3);
        
        int[][] vecteurs = {{coo1[0],coo2[0],coo3[0]},{coo1[1],coo2[1],coo3[1]}};

        return vecteurs;
    }
*/

    public double distance(int a, int b, int x, int y){
        return Math.sqrt((x-a)*(x-a)+(y-b)*(y-b));
    }
    
}

