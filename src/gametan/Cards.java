/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

/**
 *
 * @author linh
 */
public class Cards {

    public Cards(int idCard) {
        this.idCard = idCard;
        this.special = false;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cards other = (Cards) obj;
        if (this.idCard != other.idCard) {
            return false;
        }
        if (this.special != other.special) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.idCard;
        return hash;
    }
    
    private int idCard;         //id [0..52]
    private boolean special;    //mark special card
}
