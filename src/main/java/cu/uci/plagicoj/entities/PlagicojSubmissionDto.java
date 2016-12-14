/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj.entities;

/**
 *
 * @author leandro
 */
public class PlagicojSubmissionDto {
    private int submitId;
    private String language;
    private int userId;
    
    public PlagicojSubmissionDto(){
        
    }

    /**
     * @return the submitId
     */
    public int getSubmitId() {
        return submitId;
    }

    /**
     * @param submitId the submitId to set
     */
    public void setSubmitId(int submitId) {
        this.submitId = submitId;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
