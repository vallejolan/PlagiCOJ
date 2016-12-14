/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj.scpdt;

import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;

/**
 *
 * @author leandro
 */
public interface ConsumerImpl extends Runnable{     
   
   public void Connect() throws IOException;
   
   public QueueingConsumer.Delivery popDelivery() throws InterruptedException;

   public void startConsuming() throws InterruptedException;
}
