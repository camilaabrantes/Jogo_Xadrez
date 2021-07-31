package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	List<Piece> piecesOnTheBoard = new ArrayList<>();
	List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
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

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				chessPieces[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return chessPieces;
	}

	public void placeChessPiece(char column, int row, ChessPiece piece) {
		board.placePiece((Piece) piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
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

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em check");
		}

		ChessPiece movedPiece = (ChessPiece) board.piece(target);
		
		promoted = null;
		if(movedPiece instanceof Pawn && ( (movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7) )) {
			promoted = movedPiece;
			promoted = replacePromotedPiece("Q");
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}

		if (movedPiece instanceof Pawn
				&& (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)) {
			enPassantVulnerable = movedPiece;
		}

		return (ChessPiece) capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new ChessException("Peca promovida vazia.");
		}
		if(!type.equals("Q") && !type.equals("N") && !type.equals("R") && !type.equals("B")) {
			throw new ChessException("Essa Peca nao pode ser promovida.");
		}
		Position promotedPosition = promoted.getChessPosition().toPosition();
		ChessPiece removedPiece = (ChessPiece)board.removePiece(promotedPosition);
		piecesOnTheBoard.remove(removedPiece);
		
		ChessPiece newPiece;
		
		if(type.equals("R")) newPiece = new Rook(board, promoted.getColor());
		else if(type.equals("N")) newPiece = new Knight(board, promoted.getColor());
		else if(type.equals("B")) newPiece = new Bishop(board, promoted.getColor());
		else newPiece = new Queen(board, promoted.getColor());		
		
		board.placePiece(newPiece, promotedPosition);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}

	public void validateSourcePosition(Position source) {
		if (!board.ThereIsAPiece(source)) {
			throw new ChessException("Nao existe peca nessa posicao");
		}
		if (!(((ChessPiece) board.piece(source)).getColor() == currentPlayer)) {
			throw new ChessException("A peca escolhida nao e sua.");
		}
		if (!board.piece(source).IsThereAnyPossibleMove()) {
			throw new ChessException("Nao existe movimento possivel para essa peca");
		}
	}

	public void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peca escolhida nao pode se mover para a posicao de destino.");
		}
	}

	public Piece makeMove(Position source, Position target) {
		ChessPiece sourcePiece = (ChessPiece) board.removePiece(source);
		sourcePiece.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(sourcePiece, target);
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// jogada especial castling
		if (sourcePiece instanceof King && target.getRow() == source.getRow()
				&& target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			rook.increaseMoveCount();
			board.placePiece(rook, targetT);
		}

		if (sourcePiece instanceof King && target.getRow() == source.getRow()
				&& target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			rook.increaseMoveCount();
			board.placePiece(rook, targetT);
		}

		// jogada especial enPassant
		if (sourcePiece instanceof Pawn) {
			if (target.getColumn() != source.getColumn() && capturedPiece == null) {
				Position positionEnpassant;
				if (sourcePiece.getColor() == Color.WHITE) {
					positionEnpassant = new Position(target.getRow() + 1, target.getColumn());
				} else {
					positionEnpassant = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = (ChessPiece) board.removePiece(positionEnpassant);
				piecesOnTheBoard.remove(capturedPiece);
				capturedPieces.add(capturedPiece);
			}
		}

		return capturedPiece;
	}

	public void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece sourcePiece = (ChessPiece) board.removePiece(target);
		sourcePiece.decreaseMoveCount();
		board.placePiece(sourcePiece, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// jogada especial castling
		if (sourcePiece instanceof King && target.getRow() == source.getRow()
				&& target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			rook.decreaseMoveCount();
			board.placePiece(rook, sourceT);
		}

		if (sourcePiece instanceof King && target.getRow() == source.getRow()
				&& target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			rook.increaseMoveCount();
			board.placePiece(rook, sourceT);
		}

		// jogada especial enPassant
		if (sourcePiece instanceof Pawn) {
			if (target.getColumn() != source.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece enPassantPawn = (ChessPiece) board.removePiece(target);
				Position positionEnpassant;
				if (sourcePiece.getColor() == Color.WHITE) {
					positionEnpassant = new Position(3, target.getColumn());
				} else {
					positionEnpassant = new Position(4, target.getColumn());
				}
				board.placePiece(enPassantPawn, positionEnpassant);
			}
		}
	}

	public Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	public ChessPiece king(Color color) {
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());

		for (Piece p : pieces) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}

		throw new IllegalStateException("Nao existe Rei para a cor " + color + "no tabuleiro.");
	}

	public boolean testCheck(Color color) {
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color))
				.collect(Collectors.toList());
		Position kingPosition = king(color).getChessPosition().toPosition();
		for (Piece p : pieces) {
			boolean[][] possibleMoves = p.possibleMoves();
			if (possibleMoves[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}

		return false;
	}

	public boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : pieces) {
			boolean[][] possibleMoves = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (possibleMoves[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public void initialSetup() {
		placeChessPiece('a', 1, new Rook(board, Color.WHITE));
		placeChessPiece('b', 1, new Knight(board, Color.WHITE));
		placeChessPiece('c', 1, new Bishop(board, Color.WHITE));
		placeChessPiece('d', 1, new Queen(board, Color.WHITE));
		placeChessPiece('e', 1, new King(board, Color.WHITE, this));
		placeChessPiece('f', 1, new Bishop(board, Color.WHITE));
		placeChessPiece('g', 1, new Knight(board, Color.WHITE));
		placeChessPiece('h', 1, new Rook(board, Color.WHITE));
		placeChessPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeChessPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeChessPiece('a', 8, new Rook(board, Color.BLACK));
		placeChessPiece('b', 8, new Knight(board, Color.BLACK));
		placeChessPiece('c', 8, new Bishop(board, Color.BLACK));
		placeChessPiece('d', 8, new Queen(board, Color.BLACK));
		placeChessPiece('e', 8, new King(board, Color.BLACK, this));
		placeChessPiece('f', 8, new Bishop(board, Color.BLACK));
		placeChessPiece('g', 8, new Knight(board, Color.BLACK));
		placeChessPiece('h', 8, new Rook(board, Color.BLACK));
		placeChessPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeChessPiece('h', 7, new Pawn(board, Color.BLACK, this));

	}
}
