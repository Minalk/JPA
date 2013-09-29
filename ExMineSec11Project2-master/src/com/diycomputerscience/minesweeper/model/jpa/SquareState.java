package com.diycomputerscience.minesweeper.model.jpa;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;




@Entity
public class SquareState {
	

	private long id;
	public String state;
	
	public SquareState() {
		
	}
	
	public SquareState(String state) {
		this.state = state;
	}
	
	public SquareState(com.diycomputerscience.minesweeper.SquareDo.SquareState state) {
		this.state = state.toString();
	}

	@Id	
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return this.state;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + 14;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SquareState other = (SquareState) obj;
		if (state != other.state)
			return false;
		return true;
	}

}
