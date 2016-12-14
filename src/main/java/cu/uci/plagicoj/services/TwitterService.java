/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj.services;

/**
 *
 * @author leandro
 */
public class TwitterService implements MessageService {

	public boolean sendMessage(String msg, String rec) {
		System.out.println("Twitter message Sent to "+rec+ " with Message="+msg);
		return true;
	}

}
