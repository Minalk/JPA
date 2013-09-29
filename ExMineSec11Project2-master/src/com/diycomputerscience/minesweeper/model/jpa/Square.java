package com.diycomputerscience.minesweeper.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.diycomputerscience.minesweeper.SquareDo;

@Entity
public class Square implements Serializable {

	private long id; // primary key
	private int row;
	private int col;
	private boolean mine;
	private int mcount;
	@ManyToOne
	private SquareState state;	
	
	public Square() {
		
	}
	
	public Square(int row,
			      int col,
			      boolean mine,
			      int mcount,
			      SquareState state) {
		
		this.row = row;
		this.col = col;
		this.mine = mine;
		this.mcount = mcount;
		this.state = state;
	}
	
	@Id
	@GeneratedValue
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	@Column(nullable=false)
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	
	@Column(nullable=false)
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public int getMcount() {
		return mcount;
	}

	public void setMcount(int mcount) {
		this.mcount = mcount;
	}
	
	@ManyToOne( targetEntity=SquareState.class )
	public SquareState getState() {
		return state;
	}

	public void setState(SquareState state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "(" + this.row + ", " + this.col + ") " + 
	           this.isMine() + " " + 
			   this.mcount + " " + 
	           this.state;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mcount;
		result = prime * result + (mine ? 1231 : 1237);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		SquareDo other = (SquareDo) obj;
		if (mcount != other.getCount())
			return false;
		if (mine != other.isMine())
			return false;
		if (state != new SquareState(other.getState().toString()))
			return false;
		return true;
	}

}
