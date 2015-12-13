/*
 * To change this license header, choose License Headers in Project Properties.
j* To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci_events;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import view.*; 

/*
To use the forecast.io weather java classes, include import com.github.dvdme.ForecastIOLib.*;
in your class
*/


/**
 *
 * @author avina
 */
public class CI_Events {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Login login = new Login(); //View
        login.setVisible(true);
        JFrame l = new JFrame("VENUS Classic");
        l.add(login);
        l.setSize(500,500);
        l.pack();        
        l.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.addJFrame(l);
        l.setVisible(true);
        if(login.isCloseLogin()){
            login.setVisible(false);
            l.setVisible(false);
            System.out.println("test");
        }
        
  	/* testing weather application */
        Weather csuci = new Weather("1 University Dr, Camarillo, CA 93012");
        csuci.display();
        
        Event test = new Event("test", "12/2/2015", "eric", "1 University Dr, Camarillo, CA 93012");
        test.updateWeather();
        
        Weather ucsd = new Weather("9500 Gilman Dr, La Jolla, CA 92093");
        ucsd.display();
        
    }
    
}