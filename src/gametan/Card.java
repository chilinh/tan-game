/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametan;

/**
 *
 * @author linh
 */
public class Card implements Comparable<Card> {
    
    public static final int ACE = 12;
    public static final int KING = 11;
    public static final int QUEEN = 10;
    public static final int JACK = 9;

    public Card(int idCard) {
        this.idCard = idCard;
        this.special = false;
    }

    // Get Set method
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
    
    // Addition method
    public boolean isSameType(Card card) {
	if (card.getType() == this.getType())
	    return true;
	return false;
    }
    
    public int getType() {
	return this.idCard/13;
    }
    
    public boolean isSameSize(Card card) {
	if (card.getSize() == this.getSize())
	    return true;
	return false;
    }
    
    public int getSize() {
	return this.idCard%13;
    }
    
    private int idCard;         //id [0..52]
    private boolean special;    //mark special card

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Card other = (Card) obj;
	if (this.idCard != other.idCard) {
	    return false;
	}
	if (this.special != other.special) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "Card{" + this.getType() + ", " + this.getSize() + ", " + special + '}';
    }

    @Override
    public int hashCode() {
	int hash = 3;
	return hash;
    }

    @Override
    public int compareTo(Card o) {
	return (this.getSize() - o.getSize());
    }
}
