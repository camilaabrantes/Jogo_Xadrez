package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece{
	
	private ChessMatch chessMatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);			
		
		//peao branco - se move para cima (subtrai linha)
		if(getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if(getBoard().PositionExists(p) && getBoard().piece(p) == null ) {	
				mat[p.getRow()][p.getColumn()] = true;
				if(getMoveCount() == 0 && getBoard().piece(p) == null) {
					p.setValues(position.getRow() - 2, position.getColumn());
					if(getBoard().PositionExists(p) && (getBoard().piece(p) == null || isThereOpponentPiece(p))) {
						mat[p.getRow()][p.getColumn()] = true;
					}				
				}
			}
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if(getBoard().PositionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if(getBoard().PositionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//enPassant
			if(position.getRow() == 3) {
				Position esquerda = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().PositionExists(esquerda) && isThereOpponentPiece(esquerda) && chessMatch.getEnPassantVulnerable() == getBoard().piece(esquerda)) {
					mat[esquerda.getRow() - 1][esquerda.getColumn()] = true;
				}
				Position direita = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().PositionExists(direita) && isThereOpponentPiece(direita) && chessMatch.getEnPassantVulnerable() == getBoard().piece(direita)) {
					mat[direita.getRow() - 1][direita.getColumn()] = true;
				}
			}
			
		}else { //peao preto - se move para baixo (soma linha)
			p.setValues(position.getRow() + 1, position.getColumn());
			if(getBoard().PositionExists(p) && getBoard().piece(p) == null) {	
				mat[p.getRow()][p.getColumn()] = true;
				if(getMoveCount() == 0 && getBoard().piece(p) == null) {
					p.setValues(position.getRow() + 2, position.getColumn());
					if(getBoard().PositionExists(p) && (getBoard().piece(p) == null || isThereOpponentPiece(p))) {
						mat[p.getRow()][p.getColumn()] = true;
					}				
				}
			}
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if(getBoard().PositionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if(getBoard().PositionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//enPassant
			if(position.getRow() == 4) {
				Position esquerda = new Position(position.getRow(), position.getColumn() - 1);
				if(getBoard().PositionExists(esquerda) && isThereOpponentPiece(esquerda) && chessMatch.getEnPassantVulnerable() == getBoard().piece(esquerda)) {
					mat[esquerda.getRow() + 1][esquerda.getColumn()] = true;
				}
				Position direita = new Position(position.getRow(), position.getColumn() + 1);
				if(getBoard().PositionExists(direita) && isThereOpponentPiece(direita) && chessMatch.getEnPassantVulnerable() == getBoard().piece(direita)) {
					mat[direita.getRow() + 1][direita.getColumn()] = true;
				}
			}
		}
		
		
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}
}
