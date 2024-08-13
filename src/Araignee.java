
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.lang.Math;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Araignee extends JFrame {
    //Initialisation des variables 
    JButton a_deplacer = null; //case qui va être déplacer (en phase2 seulement)
    JButton relancer; //Bouton pour lancer une nouvelle partie

    JLabel titre; 
    JLabel info;
    JLabel info2;
    JLabel erreur;
    boolean joueur = true; //boolean qui indique a qui est le tour (joueur = true : joueur1)
    boolean phase1 = true; //boolean qui indique la phase dans laquelle on est (1 ou pas 1 (donc 2))
    int incr = 0; //int pour compter le nombre de case posées (et ainsi terminer la phase 1 lorsqu'on atteint 6)
    boolean partie_finie = false; //boolean qui indique si la partie est finie 
    
    //Images à mettre dans les cases
    ImageIcon araignee_j1 = new ImageIcon("araignee_j1.jpg");
    ImageIcon araignee_j2 = new ImageIcon("araignee_j2.png");

    //Couleurs utilisées pour le jeu
    Color bleu_pastel = new Color(153, 204, 255); 
    Color rouge_pastel = new Color(255, 153, 153); 

    //Création des cases
    ArrayList<String> coordonees = new ArrayList<String>();
    JButton[] cases = new JButton[9]; 

    ArrayList<JButton> cases_rouges_j2 = new ArrayList<JButton>();
    ArrayList<JButton> cases_bleues_j1 = new ArrayList<JButton>();



    public static void main(String argv[]) {
        Araignee b = new Araignee(); 
    }


    public Araignee() {

        //Crétion de l'interface graphique et des panels
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel(new GridLayout(3, 3));
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p2.setBorder(new EmptyBorder(15,0,15,0));
        p2.setSize(100, 100);

        add(p1);
        add(p2);


        //Création des labels         
        titre = new JLabel("<html><b><font size='5'>Jeu de l'Araignée</font></b><html>");
        p1.add(titre);
        titre.setBorder(new EmptyBorder(10, 10, 10, 10));

        info = new JLabel("<html><b>Phase 1 :</b> <i>Positionnement</i><html>");
        p1.add(info);

        info2 = new JLabel("<html>Le <font color = '#6495ED'>joueur 1</font> peut commencer</html>");
        p1.add(info2);
        info2.setBorder(new EmptyBorder(0, 0, 10, 15));

        erreur = new JLabel("");
        p1.add(erreur);
        erreur.setBorder(new EmptyBorder(3, 0, 10, 10));

        //Création d'un bouton Nouvelle Partie
        relancer = new JButton();
        relancer.setText("Nouvelle partie");
        p1.add(relancer);

        //Adaptation des dimensions des images 
        araignee_j1 = resizeImageIcon(araignee_j1, 25,25);
        araignee_j2 = resizeImageIcon(araignee_j2, 25,25);

        
        //Sousclass Listener
        class DoItListener implements ActionListener { 
            public void actionPerformed(ActionEvent e) {
                JButton boutonClique = (JButton) e.getSource();
                
                //Réinitialisation du label d'erreur
                erreur.setText("");
                
                //Réinitialisation du jeu et de l'interface en cas de nouvelle partie
                if (boutonClique == relancer) {
                    info.setText("<html><b>Phase 1 :</b> <i>Positionnement</i><html>");
                    info2.setText("<html>Le <font color = '#6495ED'>joueur 1</font> peut commencer</html>");
                    erreur.setText("");

                    incr=0;
                    cases_bleues_j1.clear();
                    cases_rouges_j2.clear();
                    joueur = true;
                    phase1 = true;
                    partie_finie = false;

                    for (int i=0;i<=8;i++){
                        set_vide(cases[i]);
                    }
                }
                //Nouvelle partie n'a pas été cliqué, le jeu continue 
                else {
                    //Modification du label pour indiquer quel joueur doit jouer
                    if (joueur){
                        info2.setText("<html>C'est au tour du <font color = '#6495ED'>joueur 1</font></html>");
                    }
                    else {
                        info2.setText("<html>C'est au tour du <font color = '#FF6B8F'>joueur 2</font></html>");
                    }

                    //Si nous sommes toujours dans la phase1 on appelle la fonction qui lui correspond
                    if (phase1){
                        Jouer_Coup_phase1(boutonClique, joueur);
                        joueur =  !joueur;

                        if (incr==6){
                            info.setText("<html><b>Phase 2 :</b> <i>Déplacement</i><html>");
                            //L'incrément est arrivé à 6, donc 6 cases ont été posées
                            phase1 = !phase1;
                        }
                    }
                    //Dans le cas contraire, si la partie n'est pas finie, alors on est dans la phase de déplacement 
                    else if (!partie_finie){

                        // Si une case a été sélectionnée au préalable alors elle sera déplacée à la place du bouton qui vient d'être cliqué 
                        if (a_deplacer!=null){

                            //On doit d'abord vérifier que c'est bien une case vide qui a été sélectionnée et que c'est bien une des voisines de la case sélectionnée en premier 
                            if (boutonClique.getBackground() == Color.WHITE && trouve_voisines(a_deplacer).contains(boutonClique)){
                                
                                //On va modifier les cases en fonction du joueur a qui c'est le tour
                                if (joueur) {
                                //on change la nature de la case avec set_j
                                set_j1(boutonClique);
                                //La case déplacée est maintenant vide
                                set_vide(a_deplacer);
                                //C'est ensuite le tour du joueur suivant 
                                joueur = !joueur;
                                //Enfin on réadapte les paramètres internes du jeu 
                                cases_bleues_j1.remove(a_deplacer);
                                cases_bleues_j1.add(boutonClique);
                                a_deplacer = null;
                                }

                                else {
                                set_j2(boutonClique);
                                joueur = !joueur;
                                set_vide(a_deplacer);
                                cases_rouges_j2.remove(a_deplacer);
                                cases_rouges_j2.add(boutonClique);
                                a_deplacer = null;
                                }
                            }

                            //Si le bouton cliqué est le même que le bouton précédement choisi alors c'est qu'on ne souhaite plus le déplacer
                            //On va donc laisser le joueur en choisir un autre à déplacer et remettre à jour l'interface
                            else if (a_deplacer == boutonClique) {
                                if (a_deplacer.getIcon() == araignee_j1){
                                    set_j1(a_deplacer);
                                }
                                else {
                                    set_j2(a_deplacer);
                                }
                                
                                a_deplacer = null;
                            }
                            
                        }

                        // la variable a_deplacer est null donc le bouton cliqué sera la case qu'on veut déplacer
                        else {
                            
                            if (boutonClique.getIcon() == araignee_j1 && joueur) {
                                a_deplacer = boutonClique;
                                //On change la couleur de fond pour plus de visibilité 
                                boutonClique.setBackground(Color.BLUE);
                            }
                            else if (boutonClique.getIcon() == araignee_j2 && !joueur){
                                a_deplacer = boutonClique;
                                boutonClique.setBackground(Color.RED);
                            }
                            //Les conditions précédentes n'ont pas été remplies, le joueur a voulu déplacer une case qu'il ne peut pas déplacer, on lui rappelle les règles
                            else {
                                if (!joueur){
                                    erreur.setText("<html><i>Veuillez choisir une case <font color = '#FF6B8F'>rouge</font></i></html>"); 
                                }
                                else {
                                    erreur.setText("<html><i>Veuillez choisir une case <font color = '#6495ED'>bleue</font></i></html>"); 
                                }
                            }
                        }
                    }
                    //A partir du moment où un joueur a posé trois cases il est possible qu'il gagne !
                    if (cases_rouges_j2.size()==3){
                        //Si la focntion alignees renvoie true alors c'est que les 3 cases alignées, on teste pour les 3 cases rouges et pour les 3 cases bleues après chaque coup joué
                        if (alignees(cases_rouges_j2.get(0), cases_rouges_j2.get(1),cases_rouges_j2.get(2))){
                            
                                info2.setText("<html>Le <font color = '#FF6B8F'>joueur 2</font>  a gagné !</html>");
                                partie_finie = true;
                        }
                    }
                    if (cases_bleues_j1.size() == 3){
                        if (alignees(cases_bleues_j1.get(0),cases_bleues_j1.get(1),cases_bleues_j1.get(2))) {
                            
                                info2.setText(("<html>Le <font color = '#6495ED'>joueur 1</font> a gagné !</html>"));
                                partie_finie = true;

                        }

                    }
                }

            }

        }


        //Maintenant que notre sous classe est bien définie, on peut définir complètement les éléments de notre jeu
        //Les cases :
        for (int i=0;i<=8;i++){
            cases[i]= new JButton("#"); 
            cases[i].setSize(100, 100);
            p2.add(cases[i]);
            cases[i].setName("case"+i);
            cases[i].addActionListener(new DoItListener());
            cases[i].setOpaque(true);
            cases[i].setBackground(Color.WHITE);
            cases[i].setForeground(Color.BLACK);
        }

        //Le bouton Nouvelle partie 
        relancer.addActionListener(new DoItListener());

        //on crée une liste de coordonées qui va nous servir à trouver quelles cases sont voisines/alignées
        //Les coo sont par exemple : 00 pour la case en haut à gauche, 11 pour la case centre de la grille etc... un peu comme les coef d'une matrice 
        for (int i=0;i<=8;i++){
            int x = i%3;
            int y = i/3;
            String a = String.valueOf(x);
            String b = String.valueOf(y);
            coordonees.add(a+b);
        }

        //Classique fermeture
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); setVisible(true);
        
    }
    

    //Une méthode type pour resize les images de nos boutons 
    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    
    //la methode correspondant au coup de la phase 1
    public void Jouer_Coup_phase1(JButton bouton, boolean joueur) {
        if (joueur) {
            set_j1(bouton); //On met à jour l'interface
            cases_bleues_j1.add(bouton); // On ajoute notre nouvelle case bleue à la liste des cases bleues
        }

        else {
            set_j2(bouton); 
            cases_rouges_j2.add(bouton);
        }
        //On met à jour l'incrément pour savoir combien de cases ont été posées
        incr+=1;
    }
    
    //Méthode pour mettre à jour un bouton sélectionné par le j1 (bleu)
    public void set_j1(JButton bouton){
        //on retire le text pour laisser la place à l'image et on change la couleur du background 
        bouton.setText("");
        bouton.setIcon(araignee_j1);
        bouton.setBackground(bleu_pastel);
    }

    //Méthode pour mettre à jour un bouton sélectionné par le j2 (rouge)
    public void set_j2(JButton bouton){
        bouton.setText("");
        bouton.setIcon(araignee_j2);
        bouton.setBackground(rouge_pastel);
    }

    //Méthode pour reinitialiser le visuel d'une case (après qu'elle ait été déplacée par ex)
    public void set_vide(JButton bouton) {
        bouton.setText("#");
        bouton.setBackground(Color.WHITE);
        bouton.setIcon(null);
    }

    //méthode pour trouver les cases voisines en se basant sur un système de coordonées
    public ArrayList<JButton> trouve_voisines(JButton bouton) {
        ArrayList<JButton> voisines = new ArrayList<JButton>();
        
        //On récupère les coo
        int a = get_coo(bouton)[0];
        int b = get_coo(bouton)[1];

        //on va tester chacune des cases du jeu 
        for (int i=0;i<=8;i++){
            JButton case_test = cases[i];
            int x = get_coo(case_test)[0];
            int y = get_coo(case_test)[1];

            //on calcule la distance entre la case dont on cherche les voisines et les autres
            //Si elle est non nulle et inferieur a 1.5 alors c'est bien une case voisine (simple pythagore)
            if (distance(a, b, x, y) < 1.5 && distance(a, b, x, y) > 0) {
                voisines.add(case_test);
            }
        }

        return voisines;
    }

    //Méthode pour obetnir les coordonées d'une case à partir de son nom (on aurait aussi pu nommer les cases avec leur coordonée directement...)
    public int[] get_coo(JButton bouton){
        String nom = bouton.getName();
        char numero_str = nom.charAt(4);
        int numero = Character.getNumericValue(numero_str);

        //On utilise la liste coordonées créée plus tôt qui contient déjà les coordonées de chaque case
        String coo_str = coordonees.get(numero);
        int coox = coo_str.charAt(0)-'0';
        int cooy = coo_str.charAt(1)-'0';
        int[] coo = {coox, cooy};
        return coo;
    }

    //méthode pour vérifier si 3 cases sont bien alignées 
    public boolean alignees(JButton bouton1, JButton bouton2, JButton bouton3){
        boolean gagne = false;
        //Si on a une ligne verticale ou horizontale alors les coo sont par ex (0y,0y,0y) ou (x2,x2,x2)
        if ((get_coo(bouton1)[0] == get_coo(bouton2)[0] //Si on forme une ligne verticale ou horizontale
                            && get_coo(bouton1)[0] == get_coo(bouton3)[0]) 
                            || (get_coo(bouton1)[1] == get_coo(bouton2)[1] 
                            && get_coo(bouton1)[1] == get_coo(bouton3)[1])){
            gagne = true;
        }
        //Dans le cas où la ligne est une diag : la somme des coo x et des coo y fait nécessairement 3 et les deux cases les plus éloignées sont nécessairement éloignées d'au moins 2.8 (grande diag)! 
        else if (get_coo(bouton1)[0]+get_coo(bouton2)[0]+get_coo(bouton3)[0] == 3 
                && get_coo(bouton1)[1]+get_coo(bouton2)[1]+get_coo(bouton3)[1] == 3
                && (distance(get_coo(bouton1)[0], get_coo(bouton1)[1], get_coo(bouton2)[0],get_coo(bouton2)[1]) > 2.8
                || distance(get_coo(bouton1)[0], get_coo(bouton1)[1], get_coo(bouton3)[0],get_coo(bouton3)[1]) > 2.8
                || distance(get_coo(bouton2)[0], get_coo(bouton2)[1], get_coo(bouton3)[0],get_coo(bouton3)[1]) > 2.8)){
            gagne = true;
                }
        return gagne;
    }

    //Méthode type pour calculer la distance a partir des coo de deux points...
    public double distance(int a, int b, int x, int y){
        return Math.sqrt((x-a)*(x-a)+(y-b)*(y-b));
    }
    
}

