/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Leandro
 */
@Embeddable
public class PlagicojResultPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_source_submission")
    private int idSourceSubmission;
    @Basic(optional = false)
    @Column(name = "id_destination_submission")
    private int idDestinationSubmission;

    public PlagicojResultPK() {
    }

    public PlagicojResultPK(int idSourceSubmission, int idDestinationSubmission) {
        this.idSourceSubmission = idSourceSubmission;
        this.idDestinationSubmission = idDestinationSubmission;
    }

    public int getIdSourceSubmission() {
        return idSourceSubmission;
    }

    public void setIdSourceSubmission(int idSourceSubmission) {
        this.idSourceSubmission = idSourceSubmission;
    }

    public int getIdDestinationSubmission() {
        return idDestinationSubmission;
    }

    public void setIdDestinationSubmission(int idDestinationSubmission) {
        this.idDestinationSubmission = idDestinationSubmission;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSourceSubmission;
        hash += (int) idDestinationSubmission;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlagicojResultPK)) {
            return false;
        }
        PlagicojResultPK other = (PlagicojResultPK) object;
        if (this.idSourceSubmission != other.idSourceSubmission) {
            return false;
        }
        if (this.idDestinationSubmission != other.idDestinationSubmission) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "scpdt.db.PlagicojResultPK[ idSourceSubmission=" + idSourceSubmission + ", idDestinationSubmission=" + idDestinationSubmission + " ]";
    }
    
}
