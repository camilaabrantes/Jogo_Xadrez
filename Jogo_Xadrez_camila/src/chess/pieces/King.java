package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		//acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		//abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//esquerda e para baixo
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//esquerda e para cima
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//direita e para baixo
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		//direita e para cima
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if(getBoard().PositionExists(p)) {
			if(!getBoard().ThereIsAPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			} else if(isThereOpponentPiece(p)){
				mat[p.getRow()][p.getColumn()] = true;
			}			
		}
		
		return mat;
	}	
	
	@Override
	public String toString() {
		return "K";
	}

}
