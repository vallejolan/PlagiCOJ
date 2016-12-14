/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.uci.plagicoj.detectors;



/**
 *
 * @author Leandro
 */
public interface DetectorMatch extends Comparable<DetectorMatch>{

    public int getSourceIndex();

    public int getDestinationIndex();

    public int getLength();

    public void setSourceIndex(int aInt);

    public void setDestinationIndex(int aInt);

    public void setLength(int aInt);
}
