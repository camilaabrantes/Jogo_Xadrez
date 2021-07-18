package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Erro instaciando ChessPosition. Valores validos sao de a1 ate h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	//converte do xadrez para matriz
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	/*converte da matriz para o xadrez
	*é um metodo static, pois não precisa de instancia, ou seja, não esta associado ao objeto
	* pega qualquer posição do tipo position e converte para posição do tabuleiro de xadrez
	* para alguma peça de xadrez pode usar
	*/
	protected static ChessPosition fromPosition(Position position){
		return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}

}
