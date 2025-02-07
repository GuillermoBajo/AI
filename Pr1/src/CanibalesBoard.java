package aima.core.environment.Canibales;

import java.util.Arrays;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;

/**
 * @author Guillermo Bajo
 */

// Definimos las acciones disponibles en el problema de los caníbales
public class CanibalesBoard {

	public static Action MOVER1M = new DynamicAction("M1M");

	public static Action MOVER1C = new DynamicAction("M1C");
	
	public static Action MOVER2M = new DynamicAction("M2M");
	
	public static Action MOVER2C = new DynamicAction("M2C");
	
	public static Action MOVER1M1C = new DynamicAction("M1M1C");


	private int[] state; // estado del problema
	//
	// PUBLIC METHODS
	//

	// El vector tiene 5 componentes: M_izq, C_izq, Barco (0=izq, 1=der), M_der, C_der
	public CanibalesBoard() {
		state = new int[] {3, 3, 0, 0, 0};
	}

	public CanibalesBoard(int[] state) {
		this.state = new int[state.length];
		System.arraycopy(state, 0, this.state, 0, state.length);
	}

	public CanibalesBoard(CanibalesBoard copyBoard) {
		this(copyBoard.getState());
	}

	public int[] getState() {
		return state;
	}

	//Funcion para mover un misionero al otro lado del rio
	public void mover1M() {
		int m_izq = getMisioneros(0);
		int m_der = getMisioneros(1);
		int bote = getPositionBoat();
		
		if (bote == 0) {
			updateMisioneros(m_izq-1, 0);
			updateMisioneros(m_der+1, 1);
			updateBote(1);
		}
		else if (bote == 1) {
			updateMisioneros(m_izq+1, 0);
			updateMisioneros(m_der-1, 1);
			updateBote(0);
		}
	}
	
	//Funcion para mover un canibal al otro lado del rio
	public void mover1C() {
		int c_izq = getCanibales(0);
		int c_der = getCanibales(1);
		int bote = getPositionBoat();
		
		if (bote == 0) {
			updateCanibales(c_izq-1, 0);
			updateCanibales(c_der+1, 1);
			updateBote(1);
		}
		else if (bote == 1) {
			updateCanibales(c_izq+1, 0);
			updateCanibales(c_der-1, 1);
			updateBote(0);
		}
	}
	
	//Funcion para mover dos misioneros al otro lado del rio
	public void mover2M() {
		int m_izq = getMisioneros(0);
		int m_der = getMisioneros(1);
		int bote = getPositionBoat();
		
		if (bote == 0) {
			updateMisioneros(m_izq-2, 0);
			updateMisioneros(m_der+2, 1);
			updateBote(1);
		}
		else if (bote == 1) {
			updateMisioneros(m_izq+2, 0);
			updateMisioneros(m_der-2, 1);
			updateBote(0);
		}
	}

	//Funcion para mover dos canibales al otro lado del rio
	public void mover2C() {
		int c_izq = getCanibales(0);
		int c_der = getCanibales(1);
		int bote = getPositionBoat();
		
		if (bote == 0) {
			updateCanibales(c_izq-2, 0);
			updateCanibales(c_der+2, 1);
			updateBote(1);
		}
		else if (bote == 1) {
			updateCanibales(c_izq+2, 0);
			updateCanibales(c_der-2, 1);
			updateBote(0);
		}
	}

	//Funcion para mover un misionero y un canibal al otro lado del rio
	public void mover1M1C() {
		int m_izq = getMisioneros(0);
		int m_der = getMisioneros(1);
		int c_izq = getCanibales(0);
		int c_der = getCanibales(1);
		int bote = getPositionBoat();
		
		if (bote == 0) {
			updateMisioneros(m_izq-1, 0);
			updateMisioneros(m_der+1, 1);
			updateCanibales(c_izq-1, 0);
			updateCanibales(c_der+1, 1);
			updateBote(1);
		}
		else if (bote == 1) {
			updateMisioneros(m_izq+1, 0);
			updateMisioneros(m_der-1, 1);
			updateCanibales(c_izq+1, 0);
			updateCanibales(c_der-1, 1);
			updateBote(0);
		}
	}
	
	
	//Devuelve si puede realizar la acción indicada (booleano)
	public boolean canMoveGap(Action where) { 
		int m_izq = getMisioneros(0);
		int m_der = getMisioneros(1);
		int c_izq = getCanibales(0);
		int c_der = getCanibales(1);
		int bote = getPositionBoat();
		
		boolean retVal = true;
		
		if(where.equals(MOVER1C)) {
			if(bote == 0) {
				retVal = (c_izq >= 1 && (m_der >= c_der+1 || m_der == 0));
			}
			else if (bote == 1) {
				retVal = (c_der >= 1 && (m_izq >= c_izq+1 || m_izq == 0));
			}
		}
		else if(where.equals(MOVER2C)) {
			if(bote == 0) {
				retVal = (c_izq >= 2 && (m_der >= c_der+2 || m_der == 0));
			}
			else if (bote == 1) {
				retVal = (c_der >= 2 && (m_izq >= c_izq+2 || m_izq == 0));
			}
		}
		else if(where.equals(MOVER1M)) {
			if (bote == 0) {
				retVal = (m_izq >= 1 && (m_izq >= c_izq+1 || m_izq == 1) && m_der >= c_der-1);
			}
			else if (bote == 1) {
				retVal = (m_der >= 1 && (m_der >= c_der+1 || m_der == 1) && m_izq >= c_izq-1);
			}
		}
		else if(where.equals(MOVER2M)) {
			if (bote == 0) {
				retVal = (m_izq >= 2 && (m_izq >= c_izq+2 || m_izq == 2) && m_der >= c_der-2);
			}
			else if (bote == 1) {
				retVal = (m_der >= 2 && (m_der >= c_der+2 || m_der == 2) && m_izq >= c_izq-2);
			}
		}
		else if(where.equals(MOVER1M1C)) {
			if (bote == 0) {
				retVal = (m_izq >= 1 && c_izq >= 1 && m_der >= c_der);
			}
			else if(bote == 1) {
				retVal = (m_der >= 1 && c_der >= 1 && m_izq >= c_izq);
			}
		}
		return retVal;
	}
		

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		CanibalesBoard aBoard = (CanibalesBoard) o;

		return Arrays.equals(state, aBoard.state);
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < 5; i++) {
			int position = this.state[i];
			result = 37 * result + position;
		}
		return result;
	}

	@Override
	public String toString() {
		String retVal = "RIBERA-IZQ " + writeMisioneros(getMisioneros(0)) + writeCanibales(getCanibales(0))
		+ writeBoat(getPositionBoat()) + writeMisioneros(getMisioneros(1)) + writeCanibales(getCanibales(1))
		+ " RIBERA-DCH";
		return retVal;
	}

	//
	// PRIVATE METHODS
	//

	
	// Devuelve la posicion del bote
	private int getPositionBoat() {
		return state[2];
	}
	
	
	// Devuelve el numero de canibales del lado "lado"
	private int getCanibales(int lado) {
		if(lado == 0) {
			return state[1];
		}
		else if(lado == 1) {
			return state[4];
		}
		return -1;
	}
	
	// Devuelve el numero de misioneros del lado "lado"
	private int getMisioneros(int lado) {
		if(lado == 0) {
			return state[0];
		}
		else if(lado == 1) {
			return state[3];
		}
		return -1;
	}
	
	// Actualzia el numero de canibales del lado "lado" a "num"
	private void updateCanibales(int num, int lado) {
		if(lado == 0) {
			state[1] = num;
		} else if(lado == 1) {
			state[4] = num;
		}
	}
	
	// Actualzia el numero de misioneros del lado "lado" a "num"
	private void updateMisioneros(int num, int lado) {
		if(lado == 0) {
			state[0] = num;
		} else if(lado == 1) {
			state[3] = num;
		}
	}
	
	
	// Actualiza la ubicacion del bote
	private void updateBote(int lado) { 
		state[2] = lado;
	}
	
	// Escribe el numero de misionieros inndicados como parametro
	private String writeMisioneros(int num) {
		String retVal = "     ";
		if(num == 1) {
			retVal = "    M";
		}
		else if(num == 2) {
			retVal = "  M M";
		}
		else if(num == 3) {
			retVal = "M M M ";
		}
		return retVal;
	}
	
	// Escribe el numero de canibales inndicados como parametro
	private String writeCanibales(int num) {
		String retVal = "     ";
		if(num == 1) {
			retVal = "    C";
		}
		else if(num == 2) {
			retVal = "  C C";
		}
		else if(num == 3) {
			retVal = "C C C";
		}
		return retVal;
	}
	
	// Escribe la posicion del bote
	private String writeBoat(int num) { 
		String retVal = "     ";
		if (num == 1) {
			retVal = "      --RIO-- BOTE ";
		} else if (num==0){
			retVal = " BOTE --RIO--      ";
		}
		return retVal;
	}
}
