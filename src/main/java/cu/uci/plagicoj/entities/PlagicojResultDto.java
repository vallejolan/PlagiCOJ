/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cu.uci.plagicoj.entities;

import java.util.List;
import java.util.Map;

/**
 *
 * @author leandro
 */
public class PlagicojResultDto {
    private int sourceSubmissionId;
    private int destinationSubmissionId;
    private Double dictum;
    
    public PlagicojResultDto(){
        
    }

    /**
     * @return the sourceSubmissionId
     */
    public int getSourceSubmissionId() {
        return sourceSubmissionId;
    }

    /**
     * @param sourceSubmissionId the sourceSubmissionId to set
     */
    public void setSourceSubmissionId(int sourceSubmissionId) {
        this.sourceSubmissionId = sourceSubmissionId;
    }

    /**
     * @return the destinationSubmissionId
     */
    public int getDestinationSubmissionId() {
        return destinationSubmissionId;
    }

    /**
     * @param destinationSubmissionId the destinationSubmissionId to set
     */
    public void setDestinationSubmissionId(int destinationSubmissionId) {
        this.destinationSubmissionId = destinationSubmissionId;
    }

    /**
     * @return the dictum
     */
    public Double getDictum() {
        return dictum;
    }

    /**
     * @param dictum the dictum to set
     */
    public void setDictum(Double dictum) {
        this.dictum = dictum;
    }
    
    public String formattedDictum(){        
        return (int)Math.ceil(getDictum() * 100.0)+"%";
    }
}
