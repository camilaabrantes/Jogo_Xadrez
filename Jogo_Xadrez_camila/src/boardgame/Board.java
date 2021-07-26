package boardgame;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<1 || columns<1) {
			throw new BoardException("Tamanho de tabuleiro invalido.");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) {
		if(!PositionExists(row, column)) {
			throw new BoardException("Posicao nao existe no tabuleiro.");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) {
		if(!PositionExists(position)) {
			throw new BoardException("Posicao nao existe no tabuleiro.");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if(ThereIsAPiece(position)) {
			throw new BoardException("Ja existe uma peca nessa posicao.");
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		Piece Removepiece = piece(position);
		if(Removepiece != null) {
			Removepiece.position = null;
			pieces[position.getRow()][position.getColumn()] = null;
			return Removepiece;
		}
		return null;
	}
	
	public boolean PositionExists(int row, int column) {
		return row>=0 && row<rows && column>=0 && column<columns; 
	}
	
	public boolean PositionExists(Position position) {
		return PositionExists(position.getRow(), position.getColumn());
	}
	
	public boolean ThereIsAPiece(Position position) {
		if(!PositionExists(position)) {
			throw new BoardException("Posicao nao existe no tabuleiro.");
		}
		return piece(position) != null;
	}

}
