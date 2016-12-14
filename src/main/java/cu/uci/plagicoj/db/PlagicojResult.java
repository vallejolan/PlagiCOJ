/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Leandro
 */
@Entity
@Table(name = "plagicoj_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlagicojResult.findAll", query = "SELECT p FROM PlagicojResult p"),
    @NamedQuery(name = "PlagicojResult.findByIdSourceSubmission", query = "SELECT p FROM PlagicojResult p WHERE p.plagicojResultPK.idSourceSubmission = :idSourceSubmission"),
    @NamedQuery(name = "PlagicojResult.findByIdDestinationSubmission", query = "SELECT p FROM PlagicojResult p WHERE p.plagicojResultPK.idDestinationSubmission = :idDestinationSubmission"),
    @NamedQuery(name = "PlagicojResult.findByDictum", query = "SELECT p FROM PlagicojResult p WHERE p.dictum = :dictum")})
public class PlagicojResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlagicojResultPK plagicojResultPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "dictum")
    private Double dictum;

    public PlagicojResult() {
    }

    public PlagicojResult(PlagicojResultPK plagicojResultPK) {
        this.plagicojResultPK = plagicojResultPK;
    }

    public PlagicojResult(int idSourceSubmission, int idDestinationSubmission) {
        this.plagicojResultPK = new PlagicojResultPK(idSourceSubmission, idDestinationSubmission);
    }

    public PlagicojResultPK getPlagicojResultPK() {
        return plagicojResultPK;
    }

    public void setPlagicojResultPK(PlagicojResultPK plagicojResultPK) {
        this.plagicojResultPK = plagicojResultPK;
    }

    public Double getDictum() {
        return dictum;
    }

    public void setDictum(Double dictum) {
        this.dictum = dictum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plagicojResultPK != null ? plagicojResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlagicojResult)) {
            return false;
        }
        PlagicojResult other = (PlagicojResult) object;
        if ((this.plagicojResultPK == null && other.plagicojResultPK != null) || (this.plagicojResultPK != null && !this.plagicojResultPK.equals(other.plagicojResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "scpdt.db.PlagicojResult[ plagicojResultPK=" + plagicojResultPK + " ]";
    }
    
}
