import acm.program.*;
import acm.graphics.*;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class Chess extends GraphicsProgram {
	private static final int SIZE = 70;
	private static final int X_SPACE = 5;
	private static final int X_START = X_SPACE * SIZE;
	private static final int Y_START = 0;
	private static final int ACTIVE_SIZE = 66;
	private static final int X_END = (X_SPACE * SIZE) + (SIZE * 8);
	private static final int Y_END = (Y_START * SIZE) + (SIZE * 8);
	private static final int OFFSET = 3;
	int height, width;

	public void run() {
		activeBox = new GRect(X_START + 2, Y_START + 2, ACTIVE_SIZE,
				ACTIVE_SIZE);
		add(activeBox);
		add(bigBox);
		activeBox.sendToFront();
		addMouseListeners();
		setup();
		initTempPieces();
		initRedBoxes();
	}

	private void initRedBoxes() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				redBoxes[i][j] = 0;
			}
		}
	}

	private void initTempPieces() {
		tempPiece[1] = blackPiece.BKing;
		tempPiece[2] = blackPiece.BQueen;
		tempPiece[3] = blackPiece.BRook;
		tempPiece[4] = blackPiece.BBishop;
		tempPiece[5] = blackPiece.BKnight;
		tempPiece[6] = blackPiece.BPawn;

		tempPiece[7] = whitePiece.WKing;
		tempPiece[8] = whitePiece.WQueen;
		tempPiece[9] = whitePiece.WRook;
		tempPiece[10] = whitePiece.WBishop;
		tempPiece[11] = whitePiece.WKnight;
		tempPiece[12] = whitePiece.WPawn;
	}

	private boolean isActiveBoxesPresent() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (activeBoxes[i][j] == 1) {
					return true;
				}
			}
		}
		return false;
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (x > X_START && y > Y_START && x < X_END && y < Y_END) {
			x = x - x % 70;
			y = y - y % 70;
			activeBox.setLocation(x + 2, y + 2);
			activeBox.sendToFront();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isBlackCheck && !isActiveBoxesPresent()) {
			checkForBlackCheckMate();
		}
		if (isWhiteCheck && !isActiveBoxesPresent()) {
			checkForWhiteCheckMate();
		}
		checkForBlackCheck();
		checkForWhiteCheck();

	}

	public void mousePressed(MouseEvent e) {
		int black = 0, white = 0;
		int x = e.getX();
		int y = e.getY();
		int i, j;

		if ((iTemp + jTemp) % 2 != 0) {
			box[iTemp][jTemp].Box.setFillColor(Color.gray);
		} else {
			box[iTemp][jTemp].Box.setFillColor(Color.white);
		}
		if (x > X_START && y > Y_START && x < X_END && y < Y_END) {
			x = x - x % 70;
			y = y - y % 70;
			i = x / SIZE - X_SPACE;
			j = y / SIZE;
			iTemp = i;
			jTemp = j;

			if (isActiveBox(i, j)) {
				checkForBlackCheck();
				if (isBlackCheck) {
					black = 1;
				} else
					black = 0;
				checkForWhiteCheck();
				if (isWhiteCheck) {
					white = 1;
				} else
					white = 0;

				img[xTemp][yTemp].moveImage((i + X_SPACE) * SIZE + OFFSET, j
						* SIZE + OFFSET);
				img[xTemp][yTemp].present = false;
				img[i][j].present = true;
				img[i][j].value = img[xTemp][yTemp].value;
				img[xTemp][yTemp].value = 0;
				img[i][j].pieceImage = img[xTemp][yTemp].pieceImage;
				if (isBlackCheck || isWhiteCheck) {
					checkerX = i;
					checkerY = j;
				}

				checkForBlackCheck();

				if (img[i][j].value < 7 && isBlackCheck) {
					img[i][j].moveImage((xTemp + X_SPACE) * SIZE + OFFSET,
							yTemp * SIZE + OFFSET);
					img[i][j].present = false;
					img[xTemp][yTemp].present = true;
					img[xTemp][yTemp].value = img[i][j].value;
					img[i][j].value = 0;
					img[xTemp][yTemp].pieceImage = img[i][j].pieceImage;
					checkerX = xTemp;
					checkerY = yTemp;
					checkForBlackCheck();
					checkForWhiteCheck();
					counter++;
				}
				checkForWhiteCheck();
				if (img[i][j].value > 6 && isWhiteCheck) {
					img[i][j].moveImage((xTemp + X_SPACE) * SIZE + OFFSET,
							yTemp * SIZE + OFFSET);
					img[i][j].present = false;
					img[xTemp][yTemp].present = true;
					img[xTemp][yTemp].value = img[i][j].value;
					img[i][j].value = 0;
					img[xTemp][yTemp].pieceImage = img[i][j].pieceImage;
					checkerX = xTemp;
					checkerY = yTemp;
					checkForBlackCheck();
					checkForWhiteCheck();
					counter++;
				}

				counter++;
				clearActiveBoxes();
				checkForBlackCheck();
				checkForWhiteCheck();

				return;
			}
			if (isRedBox(i, j)) {
				checkForBlackCheck();
				if (isBlackCheck) {
					checkForBlackCheckMate();
					int temp = img[i][j].value;
					img[i][j].value = img[xTemp][yTemp].value;
					img[xTemp][yTemp].present = false;
					checkForBlackCheck();
					if (isBlackCheck) {
						img[i][j].value = temp;
						img[xTemp][yTemp].present = true;
						checkerX = xTemp;
						checkerY = yTemp;
						clearActiveBoxes();
						checkForBlackCheck();
						return;
					}
					img[i][j].value = temp;
					img[xTemp][yTemp].present = true;
				}
				checkForWhiteCheck();
				if (isWhiteCheck) {
					// checkForWhiteCheckMate();
					int temp = img[i][j].value;
					img[i][j].value = img[xTemp][yTemp].value;
					img[xTemp][yTemp].present = false;
					checkForWhiteCheck();
					if (isWhiteCheck) {
						img[i][j].value = temp;
						img[xTemp][yTemp].present = true;
						checkerX = xTemp;
						checkerY = yTemp;
						clearActiveBoxes();
						checkForWhiteCheck();
						return;
					}
					img[i][j].value = temp;
					img[xTemp][yTemp].present = true;
				}

				temp = img[i][j];
				remove(img[i][j].pieceImage);
				img[xTemp][yTemp].moveImage((i + X_SPACE) * SIZE + OFFSET, j
						* SIZE + OFFSET);
				img[xTemp][yTemp].present = false;
				img[i][j].present = true;
				img[i][j].value = img[xTemp][yTemp].value;
				img[i][j].pieceImage = img[xTemp][yTemp].pieceImage;
				img[xTemp][yTemp].value = 0;
				add(img[i][j].pieceImage);
				if (isBlackCheck || isWhiteCheck) {
					checkerX = i;
					checkerY = j;
				}

				counter++;
			}
			clearActiveBoxes();
			checkForBlackCheck();
			checkForWhiteCheck();

			if (img[i][j].present) {
				xTemp = i;
				yTemp = j;
				box[i][j].changeColor();
				if (counter % 2 == 0) {
					if (img[i][j].value <= 12 && img[i][j].value > 6)
						createActiveBoxes(i, j);
				} else if (counter % 2 == 1) {
					if (img[i][j].value > 0 && img[i][j].value < 7)
						createActiveBoxes(i, j);
				}
			}
		}
	}

	private void checkForBlackCheckMate() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (img[i][j].value < 7 && img[i][j].value != 1)
					createActiveBoxes(i, j);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isActiveBox(i, j)) {
					img[i][j].present = true;
					img[i][j].value = -1;
				}
			}
		}
		checkForBlackCheck();
		if (isBlackCheck && !isRedBox(checkerX, checkerY)) {
			GLabel label = new GLabel("WHITE WINS", 500, 630);
			label.setFont("Courier-40");
			add(label);
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isActiveBox(i, j) && img[i][j].value == -1) {
					img[i][j].present = false;
				}
				if ((i + j) % 2 != 0 && img[i][j].value != 1) {
					box[i][j].Box.setFillColor(Color.gray);
					activeBoxes[i][j] = 0;
				} else if (img[i][j].value != 1) {
					box[i][j].Box.setFillColor(Color.white);
					activeBoxes[i][j] = 0;
				}
			}
		}
	}

	private void checkForWhiteCheckMate() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (img[i][j].value > 6 && img[i][j].value != 7)
					createActiveBoxes(i, j);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isActiveBox(i, j)) {
					img[i][j].present = true;
					img[i][j].value = -1;
				}
			}
		}
		checkForWhiteCheck();
		if (isWhiteCheck && !isRedBox(checkerX, checkerY)) {
			GLabel label = new GLabel("BLACK WINS", 500, 630);
			label.setFont("Courier-40");
			add(label);
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isActiveBox(i, j) && img[i][j].value == -1) {
					img[i][j].present = false;
				}
				if ((i + j) % 2 != 0 && img[i][j].value != 7) {
					box[i][j].Box.setFillColor(Color.gray);
					activeBoxes[i][j] = 0;
				} else if (img[i][j].value != 7) {
					box[i][j].Box.setFillColor(Color.white);
					activeBoxes[i][j] = 0;
				}
			}
		}
	}

	private void checkForBlackCheck() {
		int i = 0, j = 0;

		for (int m = 0; m < 8; m++) {
			for (int n = 0; n < 8; n++) {
				if (img[m][n].value == 1) {
					i = m;
					j = n;
				}
			}
		}

		createLinkPieces(i, j);
		if (isBlackCheck) {
			box[i][j].Box.setFillColor(Color.RED);
		} else {
			if ((i + j) % 2 != 0) {
				box[i][j].Box.setFillColor(Color.gray);
			} else {
				box[i][j].Box.setFillColor(Color.white);
			}
		}
	}

	private void checkForWhiteCheck() {
		int i = 0, j = 0;

		for (int m = 0; m < 8; m++) {
			for (int n = 0; n < 8; n++) {
				if (img[m][n].value == 7) {
					i = m;
					j = n;
				}
			}
		}

		createLinkPieces(i, j);
		if (isWhiteCheck) {
			box[i][j].Box.setFillColor(Color.RED);
		} else {
			if ((i + j) % 2 != 0) {
				box[i][j].Box.setFillColor(Color.gray);
			} else {
				box[i][j].Box.setFillColor(Color.white);
			}
		}
	}

	private void createLinkPieces(int i, int j) {
		if (img[i][j].value == 7) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value > 6) {
					break;
				}
				if (isPiecePresent(i, j - k)
						&& img[i][j - k].value < 7
						&& (img[i][j - k].value == 2 || img[i][j - k].value == 3)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i, j - k)) {
					break;
				}
			}
			k = 0;

			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value > 6) {
					break;
				}
				if (isPiecePresent(i, j + k)
						&& img[i][j + k].value < 7
						&& (img[i][j + k].value == 2 || img[i][j + k].value == 3)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i, j + k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value > 6) {
					break;
				}
				if (isPiecePresent(i - k, j)
						&& img[i - k][j].value < 7
						&& (img[i - k][j].value == 2 || img[i - k][j].value == 3)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value > 6) {
					break;
				}
				if (isPiecePresent(i + k, j)
						&& img[i + k][j].value < 7
						&& (img[i + k][j].value == 2 || img[i + k][j].value == 3)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value > 6) {
					break;
				}
				if (isPiecePresent(i - k, j - k)
						&& img[i - k][j - k].value < 7
						&& (img[i - k][j - k].value == 2 || img[i - k][j - k].value == 4)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j - k)) {
					break;
				}
			}
			k = 0;

			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value > 6) {
					break;
				}
				if (isPiecePresent(i + k, j + k)
						&& img[i + k][j + k].value < 7
						&& (img[i + k][j + k].value == 2 || img[i + k][j + k].value == 4)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j + k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value > 6) {
					break;
				}
				if (isPiecePresent(i + k, j - k)
						&& img[i + k][j - k].value < 7
						&& (img[i + k][j - k].value == 2 || img[i + k][j - k].value == 4)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j - k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value > 6) {
					break;
				}
				if (isPiecePresent(i - k, j + k)
						&& img[i - k][j + k].value < 7
						&& (img[i - k][j + k].value == 2 || img[i - k][j + k].value == 4)) {
					isWhiteCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j + k)) {
					break;
				}
			}

			// checking for horses
			if (!isEndOfBoard(i - 1, j - 2) && isPiecePresent(i - 1, j - 2)
					&& img[i - 1][j - 2].value == 5) {
				isWhiteCheck = true;
				return;
			}

			if (!isEndOfBoard(i + 1, j - 2) && isPiecePresent(i + 1, j - 2)
					&& img[i + 1][j - 2].value == 5) {
				isWhiteCheck = true;
				return;
			}

			if (!isEndOfBoard(i + 1, j + 2) && isPiecePresent(i + 1, j + 2)
					&& img[i + 1][j + 2].value == 5) {
				isWhiteCheck = true;
				return;
			}

			if (!isEndOfBoard(i - 1, j + 2) && isPiecePresent(i - 1, j + 2)
					&& img[i - 1][j + 2].value == 5) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 2, j - 1) && isPiecePresent(i + 2, j - 1)
					&& img[i + 2][j - 1].value == 5) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 2, j + 1) && isPiecePresent(i + 2, j + 1)
					&& img[i + 2][j + 1].value == 5) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 2, j - 1) && isPiecePresent(i - 2, j - 1)
					&& img[i - 2][j - 1].value == 5) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 2, j + 1) && isPiecePresent(i - 2, j + 1)
					&& img[i - 2][j + 1].value == 5) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 1, j - 1) && isPiecePresent(i - 1, j - 1)
					&& img[i - 1][j - 1].value == 6) {
				isWhiteCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 1, j - 1) && isPiecePresent(i + 1, j - 1)
					&& img[i + 1][j - 1].value == 6) {
				isWhiteCheck = true;
				return;
			}
			isWhiteCheck = false;
		}

		if (img[i][j].value == 1) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value < 7) {
					break;
				}
				if (isPiecePresent(i, j - k)
						&& img[i][j - k].value > 6
						&& (img[i][j - k].value == 8 || img[i][j - k].value == 9)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i, j - k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value < 7) {
					break;
				}
				if (isPiecePresent(i, j + k)
						&& img[i][j + k].value > 6
						&& (img[i][j + k].value == 8 || img[i][j + k].value == 9)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i, j + k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value < 7) {
					break;
				}
				if (isPiecePresent(i - k, j)
						&& img[i - k][j].value > 6
						&& (img[i - k][j].value == 8 || img[i - k][j].value == 9)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value < 7) {
					break;
				}
				if (isPiecePresent(i + k, j)
						&& img[i + k][j].value > 6
						&& (img[i + k][j].value == 8 || img[i + k][j].value == 9)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value < 7) {
					break;
				}
				if (isPiecePresent(i - k, j - k)
						&& img[i - k][j - k].value > 6
						&& (img[i - k][j - k].value == 8 || img[i - k][j - k].value == 10)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j - k)) {
					break;
				}
			}
			k = 0;

			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value < 7) {
					break;
				}
				if (isPiecePresent(i + k, j + k)
						&& img[i + k][j + k].value > 6
						&& (img[i + k][j + k].value == 8 || img[i + k][j + k].value == 10)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j + k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value < 7) {
					break;
				}
				if (isPiecePresent(i + k, j - k)
						&& img[i + k][j - k].value > 6
						&& (img[i + k][j - k].value == 8 || img[i + k][j - k].value == 10)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i + k, j - k)) {
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value < 7) {
					break;
				}
				if (isPiecePresent(i - k, j + k)
						&& img[i - k][j + k].value > 6
						&& (img[i - k][j + k].value == 8 || img[i - k][j + k].value == 10)) {
					isBlackCheck = true;
					return;
				}
				if (isPiecePresent(i - k, j + k)) {
					break;
				}
			}

			// checking for horses
			if (!isEndOfBoard(i - 1, j - 2) && isPiecePresent(i - 1, j - 2)
					&& img[i - 1][j - 2].value == 11) {
				isBlackCheck = true;
				return;
			}

			if (!isEndOfBoard(i + 1, j - 2) && isPiecePresent(i + 1, j - 2)
					&& img[i + 1][j - 2].value == 11) {
				isBlackCheck = true;
				return;
			}

			if (!isEndOfBoard(i + 1, j + 2) && isPiecePresent(i + 1, j + 2)
					&& img[i + 1][j + 2].value == 11) {
				isBlackCheck = true;
				return;
			}

			if (!isEndOfBoard(i - 1, j + 2) && isPiecePresent(i - 1, j + 2)
					&& img[i - 1][j + 2].value == 11) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 2, j - 1) && isPiecePresent(i + 2, j - 1)
					&& img[i + 2][j - 1].value == 11) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 2, j + 1) && isPiecePresent(i + 2, j + 1)
					&& img[i + 2][j + 1].value == 11) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 2, j - 1) && isPiecePresent(i - 2, j - 1)
					&& img[i - 2][j - 1].value == 11) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 2, j + 1) && isPiecePresent(i - 2, j + 1)
					&& img[i - 2][j + 1].value == 11) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i - 1, j + 1) && isPiecePresent(i - 1, j - 1)
					&& img[i - 1][j - 1].value == 12) {
				isBlackCheck = true;
				return;
			}
			if (!isEndOfBoard(i + 1, j + 1) && isPiecePresent(i + 1, j - 1)
					&& img[i + 1][j - 1].value == 12) {
				isBlackCheck = true;
				return;
			}
			isBlackCheck = false;
		}

	}

	private boolean isRedBox(int i, int j) {
		if (redBoxes[i][j] == 1) {
			return true;
		}
		return false;
	}

	private boolean isActiveBox(int i, int j) {
		if (activeBoxes[i][j] == 1) {
			return true;
		}
		return false;
	}

	private void clearActiveBoxes() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				redBoxes[i][j] = 0;
				if ((i + j) % 2 != 0) {
					box[i][j].Box.setFillColor(Color.gray);
					box[i][j].setclicked(false);
					activeBoxes[i][j] = 0;
				} else {
					box[i][j].Box.setFillColor(Color.white);
					box[i][j].setclicked(false);
					activeBoxes[i][j] = 0;
				}
			}
		}
	}

	private boolean isEndOfBoard(int i, int j) {
		if (i < 0 || j < 0 || i > 7 || j > 7) {
			return true;
		}
		return false;
	}

	private boolean isPiecePresent(int i, int j) {
		if (isEndOfBoard(i, j))
			return false;
		if (img[i][j].present)
			return true;
		else
			return false;
	}

	private void createRedBox(int i, int j) {
		box[i][j].Box.setFillColor(Color.magenta);
		box[i][j].Box.setFilled(true);
		redBoxes[i][j] = 1;
	}

	private void createActiveBoxes(int i, int j) {

		if (img[i][j].value == 12) {
			// to move White Pawn
			if (j == 6) {
				if (!isPiecePresent(i, j - 1))
					activateBox(i, j - 1);
				if (!isPiecePresent(i, j - 2))
					activateBox(i, j - 2);
				if (!isEndOfBoard(i - 1, j - 1) && isPiecePresent(i - 1, j - 1)) {
					if (img[i - 1][j - 1].value < 7)
						createRedBox(i - 1, j - 1);
				}
				if (!isEndOfBoard(i + 1, j - 1) && isPiecePresent(i + 1, j - 1)) {
					if (img[i + 1][j - 1].value < 7)
						createRedBox(i + 1, j - 1);
				}
			} else {
				if (!isPiecePresent(i, j - 1))
					activateBox(i, j - 1);
				if (!isEndOfBoard(i - 1, j - 1) && isPiecePresent(i - 1, j - 1)) {
					if (img[i - 1][j - 1].value < 7)
						createRedBox(i - 1, j - 1);
				}
				if (!isEndOfBoard(i + 1, j - 1) && isPiecePresent(i + 1, j - 1)) {
					if (img[i + 1][j - 1].value < 7)
						createRedBox(i + 1, j - 1);
				}
			}
		} else if (img[i][j].value == 6) {
			if (j == 1) {
				if (!isPiecePresent(i, j + 1))
					activateBox(i, j + 1);
				if (!isPiecePresent(i, j + 2))
					activateBox(i, j + 2);
				if (!isEndOfBoard(i - 1, j + 1) && isPiecePresent(i - 1, j + 1)
						&& img[i - 1][j + 1].value > 6)
					createRedBox(i - 1, j + 1);
				if (!isEndOfBoard(i + 1, j + 1) && isPiecePresent(i + 1, j + 1)
						&& img[i + 1][j + 1].value > 6)
					createRedBox(i + 1, j + 1);
			}

			else {
				if (!isPiecePresent(i, j + 1))
					activateBox(i, j + 1);
				if (!isEndOfBoard(i - 1, j + 1) && isPiecePresent(i - 1, j + 1)
						&& img[i - 1][j + 1].value > 6)
					createRedBox(i - 1, j + 1);
				if (!isEndOfBoard(i + 1, j + 1) && isPiecePresent(i + 1, j + 1)
						&& img[i + 1][j + 1].value > 6)
					createRedBox(i + 1, j + 1);
			}
		}

		else if (img[i][j].value == 10) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value > 6)
					break;
				if (!isPiecePresent(i - k, j - k)) {
					activateBox(i - k, j - k);
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value < 7) {
					createRedBox(i - k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value > 6)
					break;
				if (!isPiecePresent(i + k, j - k)) {
					activateBox(i + k, j - k);
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value < 7) {
					createRedBox(i + k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value > 6)
					break;
				if (!isPiecePresent(i - k, j + k)) {
					activateBox(i - k, j + k);
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value < 7) {
					createRedBox(i - k, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value > 6)
					break;
				if (!isPiecePresent(i + k, j + k)) {
					activateBox(i + k, j + k);
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value < 7) {
					createRedBox(i + k, j + k);
					break;
				}
			}

		}

		else if (img[i][j].value == 4) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value < 7)
					break;
				if (!isPiecePresent(i - k, j - k)) {
					activateBox(i - k, j - k);
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value > 6) {
					createRedBox(i - k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value < 7)
					break;
				if (!isPiecePresent(i + k, j - k)) {
					activateBox(i + k, j - k);
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value > 6) {
					createRedBox(i + k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value < 7)
					break;
				if (!isPiecePresent(i - k, j + k)) {
					activateBox(i - k, j + k);
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value > 6) {
					createRedBox(i - k, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value < 7)
					break;
				if (!isPiecePresent(i + k, j + k)) {
					activateBox(i + k, j + k);
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value > 6) {
					createRedBox(i + k, j + k);
					break;
				}
			}
		}

		if (img[i][j].value == 3) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value < 7)
					break;
				if (!isPiecePresent(i, j - k)) {
					activateBox(i, j - k);
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value > 6) {
					createRedBox(i, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value < 7)
					break;
				if (!isPiecePresent(i - k, j)) {
					activateBox(i - k, j);
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value > 6) {
					createRedBox(i - k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value < 7)
					break;
				if (!isPiecePresent(i, j + k)) {
					activateBox(i, j + k);
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value > 6) {
					createRedBox(i, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value < 7)
					break;
				if (!isPiecePresent(i + k, j)) {
					activateBox(i + k, j);
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value > 6) {
					createRedBox(i + k, j);
					break;
				}
			}
		}

		if (img[i][j].value == 9) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value > 6)
					break;
				if (!isPiecePresent(i, j - k)) {
					activateBox(i, j - k);
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value < 7) {
					createRedBox(i, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value > 6)
					break;
				if (!isPiecePresent(i - k, j)) {
					activateBox(i - k, j);
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value < 7) {
					createRedBox(i - k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value > 6)
					break;
				if (!isPiecePresent(i, j + k)) {
					activateBox(i, j + k);
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value < 7) {
					createRedBox(i, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value > 6)
					break;
				if (!isPiecePresent(i + k, j)) {
					activateBox(i + k, j);
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value < 7) {
					createRedBox(i + k, j);
					break;
				}
			}
		}
		if (img[i][j].value == 8) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value > 6)
					break;
				if (!isPiecePresent(i, j - k)) {
					activateBox(i, j - k);
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value < 7) {
					createRedBox(i, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value > 6)
					break;
				if (!isPiecePresent(i - k, j)) {
					activateBox(i - k, j);
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value < 7) {
					createRedBox(i - k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value > 6)
					break;
				if (!isPiecePresent(i, j + k)) {
					activateBox(i, j + k);
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value < 7) {
					createRedBox(i, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value > 6)
					break;
				if (!isPiecePresent(i + k, j)) {
					activateBox(i + k, j);
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value < 7) {
					createRedBox(i + k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value > 6)
					break;
				if (!isPiecePresent(i - k, j - k)) {
					activateBox(i - k, j - k);
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value < 7) {
					createRedBox(i - k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value > 6)
					break;
				if (!isPiecePresent(i + k, j - k)) {
					activateBox(i + k, j - k);
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value < 7) {
					createRedBox(i + k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value > 6)
					break;
				if (!isPiecePresent(i - k, j + k)) {
					activateBox(i - k, j + k);
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value < 7) {
					createRedBox(i - k, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value > 6)
					break;
				if (!isPiecePresent(i + k, j + k)) {
					activateBox(i + k, j + k);
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value < 7) {
					createRedBox(i + k, j + k);
					break;
				}
			}
		}
		if (img[i][j].value == 2) {
			int k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j - k)) {
					break;
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value < 7)
					break;
				if (!isPiecePresent(i, j - k)) {
					activateBox(i, j - k);
				}
				if (isPiecePresent(i, j - k) && img[i][j - k].value > 6) {
					createRedBox(i, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j)) {
					break;
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value < 7)
					break;
				if (!isPiecePresent(i - k, j)) {
					activateBox(i - k, j);
				}
				if (isPiecePresent(i - k, j) && img[i - k][j].value > 6) {
					createRedBox(i - k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i, j + k)) {
					break;
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value < 7)
					break;
				if (!isPiecePresent(i, j + k)) {
					activateBox(i, j + k);
				}
				if (isPiecePresent(i, j + k) && img[i][j + k].value > 6) {
					createRedBox(i, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j)) {
					break;
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value < 7)
					break;
				if (!isPiecePresent(i + k, j)) {
					activateBox(i + k, j);
				}
				if (isPiecePresent(i + k, j) && img[i + k][j].value > 6) {
					createRedBox(i + k, j);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j - k)) {
					break;
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value < 7)
					break;
				if (!isPiecePresent(i - k, j - k)) {
					activateBox(i - k, j - k);
				}
				if (isPiecePresent(i - k, j - k) && img[i - k][j - k].value > 6) {
					createRedBox(i - k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j - k)) {
					break;
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value < 7)
					break;
				if (!isPiecePresent(i + k, j - k)) {
					activateBox(i + k, j - k);
				}
				if (isPiecePresent(i + k, j - k) && img[i + k][j - k].value > 6) {
					createRedBox(i + k, j - k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i - k, j + k)) {
					break;
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value < 7)
					break;
				if (!isPiecePresent(i - k, j + k)) {
					activateBox(i - k, j + k);
				}
				if (isPiecePresent(i - k, j + k) && img[i - k][j + k].value > 6) {
					createRedBox(i - k, j + k);
					break;
				}
			}
			k = 0;
			while (true) {
				k++;
				if (isEndOfBoard(i + k, j + k)) {
					break;
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value < 7)
					break;
				if (!isPiecePresent(i + k, j + k)) {
					activateBox(i + k, j + k);
				}
				if (isPiecePresent(i + k, j + k) && img[i + k][j + k].value > 6) {
					createRedBox(i + k, j + k);
					break;
				}
			}
		}

		// White king active Boxes

		if (img[i][j].value == 7) {
			if (!isEndOfBoard(i - 1, j - 1) && !isPiecePresent(i - 1, j - 1))
				activateBox(i - 1, j - 1);
			if (!isEndOfBoard(i - 1, j - 1) && isPiecePresent(i - 1, j - 1)
					&& img[i - 1][j - 1].value < 7)
				createRedBox(i - 1, j - 1);

			if (!isEndOfBoard(i, j - 1) && !isPiecePresent(i, j - 1))
				activateBox(i, j - 1);
			if (!isEndOfBoard(i, j - 1) && isPiecePresent(i, j - 1)
					&& img[i][j - 1].value < 7)
				createRedBox(i, j - 1);

			if (!isEndOfBoard(i - 1, j) && !isPiecePresent(i - 1, j))
				activateBox(i - 1, j);
			if (!isEndOfBoard(i - 1, j) && isPiecePresent(i - 1, j)
					&& img[i - 1][j].value < 7)
				createRedBox(i - 1, j);

			if (!isEndOfBoard(i - 1, j + 1) && !isPiecePresent(i - 1, j + 1))
				activateBox(i - 1, j + 1);
			if (!isEndOfBoard(i - 1, j + 1) && isPiecePresent(i - 1, j + 1)
					&& img[i - 1][j + 1].value < 7)
				createRedBox(i - 1, j + 1);

			if (!isEndOfBoard(i + 1, j - 1) && !isPiecePresent(i + 1, j - 1))
				activateBox(i + 1, j - 1);
			if (!isEndOfBoard(i + 1, j - 1) && isPiecePresent(i + 1, j - 1)
					&& img[i + 1][j - 1].value < 7)
				createRedBox(i + 1, j - 1);

			if (!isEndOfBoard(i + 1, j) && !isPiecePresent(i + 1, j))
				activateBox(i + 1, j);
			if (!isEndOfBoard(i + 1, j) && isPiecePresent(i + 1, j)
					&& img[i + 1][j].value < 7)
				createRedBox(i + 1, j);

			if (!isEndOfBoard(i, j + 1) && !isPiecePresent(i, j + 1))
				activateBox(i, j + 1);
			if (!isEndOfBoard(i, j + 1) && isPiecePresent(i, j + 1)
					&& img[i][j + 1].value < 7)
				createRedBox(i, j + 1);

			if (!isEndOfBoard(i + 1, j + 1) && !isPiecePresent(i + 1, j + 1))
				activateBox(i + 1, j + 1);
			if (!isEndOfBoard(i + 1, j + 1) && isPiecePresent(i + 1, j + 1)
					&& img[i + 1][j + 1].value < 7)
				createRedBox(i + 1, j + 1);
		}
		// Black King Active Boxes
		if (img[i][j].value == 1) {
			if (!isEndOfBoard(i - 1, j - 1) && !isPiecePresent(i - 1, j - 1))
				activateBox(i - 1, j - 1);
			if (!isEndOfBoard(i - 1, j - 1) && isPiecePresent(i - 1, j - 1)
					&& img[i - 1][j - 1].value > 6)
				createRedBox(i - 1, j - 1);

			if (!isEndOfBoard(i, j - 1) && !isPiecePresent(i, j - 1))
				activateBox(i, j - 1);
			if (!isEndOfBoard(i, j - 1) && isPiecePresent(i, j - 1)
					&& img[i][j - 1].value > 6)
				createRedBox(i, j - 1);

			if (!isEndOfBoard(i - 1, j) && !isPiecePresent(i - 1, j))
				activateBox(i - 1, j);
			if (!isEndOfBoard(i - 1, j) && isPiecePresent(i - 1, j)
					&& img[i - 1][j].value > 6)
				createRedBox(i - 1, j);

			if (!isEndOfBoard(i - 1, j + 1) && !isPiecePresent(i - 1, j + 1))
				activateBox(i - 1, j + 1);
			if (!isEndOfBoard(i - 1, j + 1) && isPiecePresent(i - 1, j + 1)
					&& img[i - 1][j + 1].value > 6)
				createRedBox(i - 1, j + 1);

			if (!isEndOfBoard(i + 1, j - 1) && !isPiecePresent(i + 1, j - 1))
				activateBox(i + 1, j - 1);
			if (!isEndOfBoard(i + 1, j - 1) && isPiecePresent(i + 1, j - 1)
					&& img[i + 1][j - 1].value > 6)
				createRedBox(i + 1, j - 1);

			if (!isEndOfBoard(i + 1, j) && !isPiecePresent(i + 1, j))
				activateBox(i + 1, j);
			if (!isEndOfBoard(i + 1, j) && isPiecePresent(i + 1, j)
					&& img[i + 1][j].value > 6)
				createRedBox(i + 1, j);

			if (!isEndOfBoard(i, j + 1) && !isPiecePresent(i, j + 1))
				activateBox(i, j + 1);
			if (!isEndOfBoard(i, j + 1) && isPiecePresent(i, j + 1)
					&& img[i][j + 1].value > 6)
				createRedBox(i, j + 1);

			if (!isEndOfBoard(i + 1, j + 1) && !isPiecePresent(i + 1, j + 1))
				activateBox(i + 1, j + 1);
			if (!isEndOfBoard(i + 1, j + 1) && isPiecePresent(i + 1, j + 1)
					&& img[i + 1][j + 1].value > 6)
				createRedBox(i + 1, j + 1);
		}

		// Black Horse Boxes
		if (img[i][j].value == 5) {
			if (!isEndOfBoard(i - 1, j - 2) && !isPiecePresent(i - 1, j - 2))
				activateBox(i - 1, j - 2);
			if (!isEndOfBoard(i - 1, j - 2) && isPiecePresent(i - 1, j - 2)
					&& img[i - 1][j - 2].value > 6)
				createRedBox(i - 1, j - 2);

			if (!isEndOfBoard(i + 1, j - 2) && !isPiecePresent(i + 1, j - 2))
				activateBox(i + 1, j - 2);
			if (!isEndOfBoard(i + 1, j - 2) && isPiecePresent(i + 1, j - 2)
					&& img[i + 1][j - 2].value > 6)
				createRedBox(i + 1, j - 2);

			if (!isEndOfBoard(i + 1, j + 2) && !isPiecePresent(i + 1, j + 2))
				activateBox(i + 1, j + 2);
			if (!isEndOfBoard(i + 1, j + 2) && isPiecePresent(i + 1, j + 2)
					&& img[i + 1][j + 2].value > 6)
				createRedBox(i + 1, j + 2);

			if (!isEndOfBoard(i - 1, j + 2) && !isPiecePresent(i - 1, j + 2))
				activateBox(i - 1, j + 2);
			if (!isEndOfBoard(i - 1, j + 2) && isPiecePresent(i - 1, j + 2)
					&& img[i - 1][j + 2].value > 6)
				createRedBox(i - 1, j + 2);

			if (!isEndOfBoard(i + 2, j - 1) && !isPiecePresent(i + 2, j - 1))
				activateBox(i + 2, j - 1);
			if (!isEndOfBoard(i + 2, j - 1) && isPiecePresent(i + 2, j - 1)
					&& img[i + 2][j - 1].value > 6)
				createRedBox(i + 2, j - 1);

			if (!isEndOfBoard(i + 2, j + 1) && !isPiecePresent(i + 2, j + 1))
				activateBox(i + 2, j + 1);
			if (!isEndOfBoard(i + 2, j + 1) && isPiecePresent(i + 2, j + 1)
					&& img[i + 2][j + 1].value > 6)
				createRedBox(i + 2, j + 1);

			if (!isEndOfBoard(i - 2, j - 1) && !isPiecePresent(i - 2, j - 1))
				activateBox(i - 2, j - 1);
			if (!isEndOfBoard(i - 2, j - 1) && isPiecePresent(i - 2, j - 1)
					&& img[i - 2][j - 1].value > 6)
				createRedBox(i - 2, j - 1);

			if (!isEndOfBoard(i - 2, j + 1) && !isPiecePresent(i - 2, j + 1))
				activateBox(i - 2, j + 1);
			if (!isEndOfBoard(i - 2, j + 1) && isPiecePresent(i - 2, j + 1)
					&& img[i - 2][j + 1].value > 6)
				createRedBox(i - 2, j + 1);
		}

		// white horse Boxes
		if (img[i][j].value == 11) {
			if (!isEndOfBoard(i - 1, j - 2) && !isPiecePresent(i - 1, j - 2))
				activateBox(i - 1, j - 2);
			if (!isEndOfBoard(i - 1, j - 2) && isPiecePresent(i - 1, j - 2)
					&& img[i - 1][j - 2].value < 7)
				createRedBox(i - 1, j - 2);

			if (!isEndOfBoard(i + 1, j - 2) && !isPiecePresent(i + 1, j - 2))
				activateBox(i + 1, j - 2);
			if (!isEndOfBoard(i + 1, j - 2) && isPiecePresent(i + 1, j - 2)
					&& img[i + 1][j - 2].value < 7)
				createRedBox(i + 1, j - 2);

			if (!isEndOfBoard(i + 1, j + 2) && !isPiecePresent(i + 1, j + 2))
				activateBox(i + 1, j + 2);
			if (!isEndOfBoard(i + 1, j + 2) && isPiecePresent(i + 1, j + 2)
					&& img[i + 1][j + 2].value < 7)
				createRedBox(i + 1, j + 2);

			if (!isEndOfBoard(i - 1, j + 2) && !isPiecePresent(i - 1, j + 2))
				activateBox(i - 1, j + 2);
			if (!isEndOfBoard(i - 1, j + 2) && isPiecePresent(i - 1, j + 2)
					&& img[i - 1][j + 2].value < 7)
				createRedBox(i - 1, j + 2);

			if (!isEndOfBoard(i + 2, j - 1) && !isPiecePresent(i + 2, j - 1))
				activateBox(i + 2, j - 1);
			if (!isEndOfBoard(i + 2, j - 1) && isPiecePresent(i + 2, j - 1)
					&& img[i + 2][j - 1].value < 7)
				createRedBox(i + 2, j - 1);

			if (!isEndOfBoard(i + 2, j + 1) && !isPiecePresent(i + 2, j + 1))
				activateBox(i + 2, j + 1);
			if (!isEndOfBoard(i + 2, j + 1) && isPiecePresent(i + 2, j + 1)
					&& img[i + 2][j + 1].value < 7)
				createRedBox(i + 2, j + 1);

			if (!isEndOfBoard(i - 2, j - 1) && !isPiecePresent(i - 2, j - 1))
				activateBox(i - 2, j - 1);
			if (!isEndOfBoard(i - 2, j - 1) && isPiecePresent(i - 2, j - 1)
					&& img[i - 2][j - 1].value < 7)
				createRedBox(i - 2, j - 1);

			if (!isEndOfBoard(i - 2, j + 1) && !isPiecePresent(i - 2, j + 1))
				activateBox(i - 2, j + 1);
			if (!isEndOfBoard(i - 2, j + 1) && isPiecePresent(i - 2, j + 1)
					&& img[i - 2][j + 1].value < 7)
				createRedBox(i - 2, j + 1);
		}
	}

	private void activateBox(int i, int j) {
		box[i][j].Box.setFillColor(Color.darkGray);
		activeBoxes[i][j] = 1;
	}

	public void setup() {
		for (int i = X_SPACE; i < 8 + X_SPACE; i++) {
			for (int j = 0; j < 8; j++) {
				height = i * SIZE;
				width = j * SIZE;
				box[i - X_SPACE][j] = new chessBox();
				box[i - X_SPACE][j].Box = new GRect(height, width, SIZE, SIZE);
				add(box[i - X_SPACE][j].Box);
				if ((i + j) % 2 == 0) {
					box[i - X_SPACE][j].Box.setFilled(true);
					box[i - X_SPACE][j].Box.setFillColor(Color.gray);
				} else {
					box[i - X_SPACE][j].Box.setFilled(true);
					box[i - X_SPACE][j].Box.setFillColor(Color.white);
				}
			}
		}
		add(bigBox2);
		setupPieces();
		clearActiveBoxes();
	}

	private void setupPieces() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				img[i][j] = new image();
				img[i][j].present = false;
			}
		}
		blackPiece = new BlackChessPiece();
		for (int i = 5; i < 13; i++) {
			int j = 1;
			blackPiece.BPawn = new GImage("BPawn.png", i * SIZE + OFFSET, SIZE
					+ OFFSET);
			add(blackPiece.BPawn);
			img[i - 5][j].pieceImage = blackPiece.BPawn;
			img[i - 5][j].present = true;
			img[i - 5][j].value = 6;
		}
		blackPiece.BKing = new GImage("BKing.png", 9 * SIZE + OFFSET, OFFSET);
		add(blackPiece.BKing);
		img[4][0].pieceImage = blackPiece.BKing;
		img[4][0].present = true;
		img[4][0].value = 1;

		blackPiece.BQueen = new GImage("BQueen.png", 8 * SIZE + OFFSET, OFFSET);
		add(blackPiece.BQueen);
		img[3][0].pieceImage = blackPiece.BQueen;
		img[3][0].present = true;
		img[3][0].value = 2;

		blackPiece.BKnight = new GImage("BKnight.png", 6 * SIZE + OFFSET,
				OFFSET);
		add(blackPiece.BKnight);
		img[1][0].pieceImage = blackPiece.BKnight;
		img[1][0].present = true;
		img[1][0].value = 5;

		blackPiece.BKnight = new GImage("BKnight.png", 11 * SIZE + OFFSET,
				OFFSET);
		add(blackPiece.BKnight);
		img[6][0].pieceImage = blackPiece.BKnight;
		img[6][0].present = true;
		img[6][0].value = 5;

		blackPiece.BBishop = new GImage("BBishop.png", 10 * SIZE + OFFSET,
				OFFSET);
		add(blackPiece.BBishop);
		img[5][0].pieceImage = blackPiece.BBishop;
		img[5][0].present = true;
		img[5][0].value = 4;

		blackPiece.BBishop = new GImage("BBishop.png", 7 * SIZE + OFFSET,
				OFFSET);
		add(blackPiece.BBishop);
		img[2][0].pieceImage = blackPiece.BBishop;
		img[2][0].present = true;
		img[2][0].value = 4;

		blackPiece.BRook = new GImage("BRook.png", 5 * SIZE + OFFSET, OFFSET);
		add(blackPiece.BRook);
		img[0][0].pieceImage = blackPiece.BRook;
		img[0][0].present = true;
		img[0][0].value = 3;

		blackPiece.BRook = new GImage("BRook.png", 12 * SIZE + OFFSET, OFFSET);
		add(blackPiece.BRook);
		img[7][0].pieceImage = blackPiece.BRook;
		img[7][0].present = true;
		img[7][0].value = 3;

		whitePiece = new whiteChessPiece();
		for (int i = 5; i < 13; i++) {
			int j = 6;
			whitePiece.WPawn = new GImage("WPawn.png", i * SIZE + OFFSET,
					(SIZE * 6) + OFFSET);
			add(whitePiece.WPawn);
			img[i - 5][j].pieceImage = whitePiece.WPawn;
			img[i - 5][j].present = true;
			img[i - 5][j].value = 12;
		}
		whitePiece.WKing = new GImage("WKing.png", 9 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WKing);
		img[4][7].pieceImage = whitePiece.WKing;
		img[4][7].present = true;
		img[4][7].value = 7;

		whitePiece.WQueen = new GImage("WQueen.png", 8 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WQueen);
		img[3][7].pieceImage = whitePiece.WQueen;
		img[3][7].present = true;
		img[3][7].value = 8;

		whitePiece.WKnight = new GImage("WKnight.png", 6 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WKnight);
		img[1][7].pieceImage = whitePiece.WKnight;
		img[1][7].present = true;
		img[1][7].value = 11;

		whitePiece.WKnight = new GImage("WKnight.png", 11 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WKnight);
		img[6][7].pieceImage = whitePiece.WKnight;
		img[6][7].present = true;
		img[6][7].value = 11;

		whitePiece.WBishop = new GImage("WBishop.png", 10 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WBishop);
		img[5][7].pieceImage = whitePiece.WBishop;
		img[5][7].present = true;
		img[5][7].value = 10;

		whitePiece.WBishop = new GImage("WBishop.png", 7 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WBishop);
		img[2][7].pieceImage = whitePiece.WBishop;
		img[2][7].present = true;
		img[2][7].value = 10;

		whitePiece.WRook = new GImage("WRook.png", 5 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WRook);
		img[0][7].pieceImage = whitePiece.WRook;
		img[0][7].present = true;
		img[0][7].value = 9;
		println("hi");
		whitePiece.WRook = new GImage("WRook.png", 12 * SIZE + OFFSET,
				(7 * SIZE) + OFFSET);
		add(whitePiece.WRook);
		img[7][7].pieceImage = whitePiece.WRook;
		img[7][7].present = true;
		img[7][7].value = 9;
	}

	private boolean isBlackCheck = false, isWhiteCheck = false;
	private int counter = 2;
	private int redBoxes[][] = new int[8][8];
	private image img[][] = new image[8][8];
	private whiteChessPiece whitePiece;
	private BlackChessPiece blackPiece;
	private int iTemp, jTemp;
	chessBox box[][] = new chessBox[8][8];
	private GRect activeBox;
	private GRect bigBox = new GRect(X_START - 2, Y_START - 2, X_END - X_START
			+ 4, Y_END - Y_START + 4);
	private int activeBoxes[][] = new int[8][8];
	private GRect bigBox2 = new GRect(X_START - 3, Y_START - 3, X_END - X_START
			+ 5, Y_END - Y_START + 5);
	private int xTemp, yTemp;
	private GImage tempPiece[] = new GImage[13];
	private image temp;
	private boolean isMate;
	private int checkerX, checkerY;
}
