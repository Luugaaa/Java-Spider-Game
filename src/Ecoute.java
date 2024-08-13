import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;

class Ecoute implements ActionListener {
    int incr = 0;

    
    public Ecoute(JLabel label, JButton case0) {
        //this.label = label;
        System.out.println("coucouuuu");

        
    }

    @Override
    public void actionPerformed(ActionEvent e) { 

        System.out.println(incr);
        incr+=1;

            char joueur;
            if (incr%2==1){joueur = '*';}
            else {joueur = 'o';}
            
            JButton boutonClique = (JButton) e.getSource();
            Jouer_Coup(boutonClique, joueur);

        
        
    }

    public void Jouer_Coup(JButton bouton, char joueur) {
        if (joueur == '*') {
            bouton.setText("*");
            Color bleu_pastel = new Color(153, 204, 255); 
            bouton.setForeground(bleu_pastel);}
        else {
            bouton.setText("o");
            Color rouge_pastel = new Color(255, 153, 153); 
            bouton.setForeground(rouge_pastel);}

    }
}


