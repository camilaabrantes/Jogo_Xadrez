package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	private int turn;
	private Color currentPlayer;

	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		initialSetup();
		currentPlayer = Color.WHITE;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i=0; i<board.getRows(); i++) {
			for(int j=0; j<board.getColumns(); j++) {
				chessPieces[i][j] = (ChessPiece) board.piece(i, j);
			}
		}		
		return chessPieces;		
	}
	
	public void placeChessPiece(char column, int row, ChessPiece piece) {
		board.placePiece((Piece) piece, new ChessPosition(column,row).toPosition());
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		validateSourcePosition(source);
		Position target = targetPosition.toPosition();		
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		nextTurn();
		return (ChessPiece) capturedPiece;
	}
	
	public void validateSourcePosition(Position source) {
		if(!board.ThereIsAPiece(source)) {
			throw new ChessException("Nao existe peca nessa posicao");
		}
		if(!(((ChessPiece)board.piece(source)).getColor() == currentPlayer)) {
			throw new ChessException("A peca escolhida nao e sua.");
		}
		if(!board.piece(source).IsThereAnyPossibleMove()) {
			throw new ChessException("Nao existe movimento possivel para essa peca");
		}
	}
	
	public void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peca escolhida nao pode se mover para a posicao de destino.");
		}
	}
	
	public Piece makeMove(Position source, Position target) {
		Piece movePiece = board.removePiece(source);
		Piece capturedPiece = board.piece(target);
		board.placePiece(movePiece, target);	
		return capturedPiece;
	}
	
	public void initialSetup()
	{		
		placeChessPiece('c', 1, new Rook(board, Color.WHITE));
        placeChessPiece('c', 2, new Rook(board, Color.WHITE));
        placeChessPiece('d', 2, new Rook(board, Color.WHITE));
        placeChessPiece('e', 2, new Rook(board, Color.WHITE));
        placeChessPiece('e', 1, new Rook(board, Color.WHITE));
        placeChessPiece('d', 1, new King(board, Color.WHITE));
		
        placeChessPiece('c', 7, new Rook(board, Color.BLACK));
        placeChessPiece('c', 8, new Rook(board, Color.BLACK));
        placeChessPiece('d', 7, new Rook(board, Color.BLACK));
        placeChessPiece('e', 7, new Rook(board, Color.BLACK));
        placeChessPiece('e', 8, new Rook(board, Color.BLACK));
        placeChessPiece('d', 8, new King(board, Color.BLACK));
	}
}
