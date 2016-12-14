/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj.scpdt;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer implements Runnable{
    
    private Connection connection;
    private Semaphore lock;
    private String queueName;
    private Channel channel;
    QueueingConsumer consumer;    
    
    QueueingConsumer.Delivery delivery;
    
    public Consumer(){
        
    }
    
    public Consumer(String queueName,Semaphore lock) {
       this.queueName = queueName;
       this.lock = lock;       
   }
    
     public Consumer(Connection connection) {
       this.connection = connection;       
   }
    
   public Consumer(Connection connection, String queueName,Semaphore lock) {
       this.connection = connection;
       this.queueName = queueName;
       this.lock = lock;
       
   }
   
    public Consumer(Connection connection, Semaphore lock){
       this.connection = connection;
       this.lock = lock;
       
   }
   
   public void Connect() throws IOException{
        this.channel = connection.createChannel();
        
       channel.queueDeclare(getQueueName(), true, false, false, null);
       channel.basicQos(1);
       
        consumer = new QueueingConsumer(getChannel());
        
        getChannel().basicConsume(getQueueName(), false, consumer);
   }   
   
   public synchronized QueueingConsumer.Delivery popDelivery() throws InterruptedException{
       QueueingConsumer.Delivery tempDeliver = this.delivery;
       this.delivery = null;       
       notify();
       return tempDeliver;       
   }

   public void startConsuming() throws InterruptedException{

       while (true){
            this.delivery = consumer.nextDelivery();
            getLock().release();
            synchronized(this){
                while (this.delivery != null){
                    wait();
                }                
            }
       }
   }
   
    @Override
    public void run() {
        try {
            Connect();
            startConsuming();
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @return the queueName
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * @param queueName the queueName to set
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * @return the lock
     */
    public Semaphore getLock() {
        return lock;
    }

    /**
     * @param lock the lock to set
     */
    public void setLock(Semaphore lock) {
        this.lock = lock;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
