/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bmlcreator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Citti
 */
public class BMLCreator {

    private static String agente = "AgentMaleTeenager";
    //poner 
    //niño      agentes="AgentMaleTeenager";
    //niña      agente = "AgentFemaleTeenager";
    //hombre    agente = "AgentMan";
    //mujer     agente = "AgentWoman";
    //anciano   agente = "AgentMaleSenior";
    //anciana   agente = "AgentFemaleSenior";

    private ActiveMQConnectionFactory connectionFactory;
    private String activeMQURL = "tcp://localhost:61616/";
    private String activeMQUser = "system";
    private String activeMQPass = "manager";

    private Connection connection;
    private String connectionName = "admin";
    private String connectionPass = "admin";

    public void startConnection(String ActiveMQUser, String activeMQPass, String activeMQURL, String connectionName, String connectionPass) throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQUser, activeMQPass, activeMQURL);
        connection = connectionFactory.createConnection(connectionName, connectionPass);
        connection.start();
    }

    public void createSession(String mensaje, String destinationAgent) throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("output_" + destinationAgent);
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage(mensaje);
        producer.send(message);
        producer.close();
        session.close();

    }

    public static String createMessage(String face, String leg, String gesture, String offsetDirection, String type, String refe, String start, String end, String hora, String finaltext) {
        String c = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n"
                + "<bml>\n"
                + "  <required>\n"
                + "    <face>" + face + "</face>\n"
                + "    <leg>" + leg + "</leg>\n"
                + "    <gesture>" + gesture + "</gesture>\n"
                + "    <object name=\"offsetDirection\">" + offsetDirection + "</object>\n"
                + "    <object name=\"type\">" + type + "</object>\n"
                + "    <object name=\"refe\">" + refe + "</object>\n"
                + "    <object name=\"start\">" + start + "</object>\n"
                + "    <object name=\"end\">" + end + "</object>\n"
                + "    <object name=\"hora\">" + hora + String.valueOf((int) (Math.random() * 60)) + "</object>\n"
                + "    <finaltext>" + finaltext + "</finaltext>\n"
                + "  </required>\n"
                + "</bml>";
        return c;
    }

    private void closeConnection() throws JMSException {
        connection.close();
    }

    public boolean enviaMensaje(String mensaje, String destinationAgent) {
        boolean retorno = false;
        try {
            startConnection(activeMQUser, activeMQPass, activeMQURL, connectionName, connectionPass);
            createSession(mensaje, destinationAgent);
            closeConnection();
            retorno = true;
        } catch (JMSException ex) {
            retorno = true;
            System.out.println(ex.getMessage());
        }
        return retorno;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BMLCreator mensajero = new BMLCreator();
        mensajero.enviaMensaje(createMessage("Hablar", "Dolor", "Triste", "Atras", "audio", "", "0", "4", "14:39:", "Noo una vívora"), agente);
    }

}






























//"<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n"
//                + "<bml>\n"
//                + "  <required>\n"
//                + "    <face>Hablar</face>\n"
//                + "    <leg>Dolor</leg>\n"
//                + "    <gesture>Triste</gesture>\n"
//                + "    <object name=\"offsetDirection\">Atras</object>\n"
//                + "    <object name=\"type\">audio</object>\n"
//                + "    <object name=\"refe\"></object>\n"
//                + "    <object name=\"start\">0</object>\n"
//                + "    <object name=\"end\">4</object>\n"
//                + "    <object name=\"hora\">14:39:" + String.valueOf((int) (Math.random() * 60)) + "</object>\n"
//                + "    <finaltext>Noo una vívora</finaltext>\n"
//                + "  </required>\n"
//                + "</bml>"
