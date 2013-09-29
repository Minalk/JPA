package com.diycomputerscience.minesweeper.model.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.diycomputerscience.minesweeper.BoardDo;
import com.diycomputerscience.minesweeper.ConfigurationException;
import com.diycomputerscience.minesweeper.SquareDo;
import com.diycomputerscience.minesweeper.model.BoardDao;
import com.diycomputerscience.minesweeper.model.PersistenceException;
import com.diycomputerscience.minesweeper.model.PersistenceStrategy;

public class JPAPersistenceStrategy implements PersistenceStrategy {
		
	private EntityManagerFactory emFactory;
	
	public static final String PERSISTENCE_UNIT_NAME="persistence.unit.name";
	
	public JPAPersistenceStrategy() {
		
	}
	
	@Override
	public void configure(Properties properties) throws ConfigurationException {
		if(properties == null) {
			throw new NullPointerException("properties cannot be null");
		}
		
		String persistenceUnitName = properties.getProperty(PERSISTENCE_UNIT_NAME);
		if(persistenceUnitName == null) {
			throw new ConfigurationException("Property not found '" + PERSISTENCE_UNIT_NAME + "'");
		}
		
		this.emFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		
		populateMasters();
	
	}
	
	/**
	 * Save the squares in BoardDo to the database using the {@link Square} 
	 * Entity object. Entity design can be inferred from the properties  in
	 * Square.
	 * 
	 * @param board the Board object whose squares have to be saved
	 * 
	 * @throws PersistenceException If the data could not be saved
	 */
	@Override
	public void save(BoardDo board) throws PersistenceException {
		List<Square> retreivedSquares = new ArrayList<Square>();		
		EntityManager em = this.emFactory.createEntityManager();
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		 int count = em.createQuery("DELETE FROM Square").executeUpdate();
		 System.out.println("Deleted "+count);
		 trans.commit();
		List<SquareState> stateList = getSquareStateId();
		SquareDo squaresBoard[][] = board.getSquares();
		trans.begin();
		for(int ii=0; ii<squaresBoard.length;ii++){
			for(int jj=0; jj<squaresBoard[ii].length;jj++){
				SquareState squareState = null;
				for(SquareState state : stateList) {
					if(state.getState().equals(squaresBoard[ii][jj].getState().toString())) {
						squareState = state;
						break;
					}
				}
			Square square = new Square(ii, jj, squaresBoard[ii][jj].isMine(), squaresBoard[ii][jj].getCount(), squareState);
			em.persist(square);
			}
			
		}
		trans.commit();
	}

	/**
	 * Read the data for Square objects that are stored in the database and
	 * create a Board object from the saved data.
	 * 
	 * @return The Board object created from the saved data
	 * 
	 * @throws PersistenceException If the data could not be read
	 */
	@Override
	public BoardDo load() throws PersistenceException {
		
		EntityManager eManager = this.emFactory.createEntityManager();
	
		EntityTransaction trans = eManager.getTransaction();
		trans.begin();
		Query query = eManager.createQuery("from Square", Square.class);
		List squareList =query.getResultList();
		trans.commit();
		//System.out.println("In Load squareList==== "+squareList);
		BoardDo board = new BoardDo();
		List<SquareState> stateList = getSquareStateId();
		
		SquareDo squares[][] = new SquareDo[BoardDo.MAX_ROWS][BoardDo.MAX_COLS];
		for(int i=0,index=0; i<squares.length;i++){
			for(int j=0;j<squares[i].length;j++){
				Square tempSquare = (Square)squareList.get(index++);
				SquareDo sq = new SquareDo();
				
				if(tempSquare.getState().getId()== 2){
					sq.setState(com.diycomputerscience.minesweeper.SquareDo.SquareState.UNCOVERED);
					}
				if(tempSquare.getState().getId() == 1){
					sq.setState(com.diycomputerscience.minesweeper.SquareDo.SquareState.COVERED);
					}
				if(tempSquare.getState().getId()== 3){
					sq.setState(com.diycomputerscience.minesweeper.SquareDo.SquareState.MARKED);
					}
				sq.setCount(tempSquare.getMcount());
				sq.setMine(tempSquare.isMine());
				squares[i][j] = sq;
				
			}
		}	
		board.setSquares(squares);
		return board;
	}
	
	public final void close() {
		if(this.emFactory != null) {
			this.emFactory.close();
		}		
	}
	private List getSquareStateId(){
		EntityManager em = this.emFactory.createEntityManager();
		em.getTransaction().begin();
		Query fetchSquareStatesQuery = em.createQuery("select ss from SquareState ss");
		return fetchSquareStatesQuery.getResultList();
		
	}
	
	private void populateMasters() {
		boolean populate = false;
		EntityManager em = this.emFactory.createEntityManager();
		em.getTransaction().begin();
		Query fetchSquareStatesQuery = em.createQuery("from SquareState");
		if(fetchSquareStatesQuery.getResultList().size() == 0) {
			populate = true;
		}
		em.getTransaction().commit();
		
		if(populate) {
			em.getTransaction().begin();
			for(SquareDo.SquareState state : SquareDo.SquareState.values()) {
				SquareState ss = new SquareState(state);
				em.persist(ss);
			}
			em.getTransaction().commit();
		}		
	}
}
