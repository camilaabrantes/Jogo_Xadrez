package chess;

import boardgame.Position;

public class ChessPosition {
	
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 0 || row > 8) {
			throw new ChessException("Posicao de xadrez invalida. Valores permitidos de a1 ate h8.");
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

	public Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	public static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char) (position.getColumn() + 'a'), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}

}
