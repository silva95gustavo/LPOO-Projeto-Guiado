import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
public class Jogo {
	private static int MAP_SIDE;
	private static final int MIN_MAP_SIDE = 8;
	private static int MIN_ELEM_DIST;
	private static final int ELEM_DIST_FACTOR = 2;
	
	private static int heroi_x, heroi_y, dragao_x, dragao_y;
	private static char parede_char = 'X';
	private static char dragao_char = 'D';
	private static char dragao_espada_char = 'F';
	private static char heroi_char = 'H';
	private static char heroi_armado_char = 'A';
	private static char saida_char = 'S';
	private static char espada_char = 'E';
	private static int saida_x, saida_y, espada_x, espada_y;
	private static boolean heroi_armado, dragao;

	private static Scanner s;

	private static char[][] matrix;
	
	public static void main(String[] args) {
		heroi_x = 1;
		heroi_y = 1;
		saida_x = 9;
		saida_y = 5;
		espada_x = 1;
		espada_y = 8;
		dragao_x = 1;
		dragao_y = 3;
		heroi_armado = false;
		dragao = true;

		s = new Scanner(System.in);
		
		System.out.print("For this game, you can choose the dimmensions of the game map. The minimum size is 8,\nsince smaller sizes would make the game impossible to finish.\n");
		System.out.print("Please indicate the map size (minimum " + MIN_MAP_SIDE + ") : ");
		MAP_SIDE = s.nextInt();
		
		while(MAP_SIDE < MIN_MAP_SIDE)
		{
			System.out.print("\nGiven value is smaller than " + MIN_MAP_SIDE + ".\nPlease insert new value : ");
			MAP_SIDE = s.nextInt();
		}

		MIN_ELEM_DIST = (int) (MAP_SIDE/ELEM_DIST_FACTOR);
		MIN_ELEM_DIST = MIN_ELEM_DIST*MIN_ELEM_DIST;
		generateMap(MAP_SIDE);
		
		do{
			drawMap();
		}while(turn());
		//drawMap();

		s.close();
	}

	private static void drawMap()
	{
		for(int i = 0; i < MAP_SIDE; i++)
		{
			for(int j = 0; j < MAP_SIDE; j++)
			{
				if(j==heroi_x && i==heroi_y)
				{
					if(heroi_armado)
						System.out.print(heroi_armado_char);
					else
						System.out.print(heroi_char);						
				}
				else if (j == saida_x && i == saida_y && !dragao)
					System.out.print(saida_char);
				else if(j == espada_x && i == espada_y && !heroi_armado)
				{
					if(j == dragao_x && i == dragao_y && dragao)
						System.out.print(dragao_espada_char);
					else
						System.out.print(espada_char);
				}
				else if (j == dragao_x && i == dragao_y && dragao)
					System.out.print(dragao_char);
				else				
					System.out.print(matrix[i][j]);

				System.out.print(' ');
			}
			System.out.print('\n');
		}
	}

	private static boolean turn()
	{
		String key = s.next();

		if(key.toUpperCase().equals("A"))
			moverHeroi(heroi_x-1, heroi_y);
		else if(key.toUpperCase().equals("W"))
			moverHeroi(heroi_x, heroi_y - 1);
		else if(key.toUpperCase().equals("S"))
			moverHeroi(heroi_x, heroi_y + 1);
		else if(key.toUpperCase().equals("D"))
			moverHeroi(heroi_x+1, heroi_y);
		if(heroi_x == saida_x && heroi_y == saida_y) 
		{
			System.out.print("\n\n Congratulations! You escaped the maze!\n\n");
			return false;
		}

		if(heroi_x == espada_x && heroi_y == espada_y)
			heroi_armado = true;

		if(combateDragao())
		{
			if(dragao)
				turnDragao();
		}
		else
		{
			System.out.print("\n\n Bad luck! The dragon killed you... Take revenge next time!\n\n");
			return false;
		}

		if(combateDragao())
		{
			
			return true;
		}

		System.out.print("\n\n Bad luck! The dragon killed you... Take revenge next time!\n\n");
		return false;
	}
	private static void turnDragao()
	{
		Random x = new Random();
		boolean posicaoValida = false;
		do
		{
			switch(x.nextInt(5))
			{
			case 0:
				posicaoValida = true;
				break;
			case 1: // cima
				posicaoValida = moverDragao(dragao_x, dragao_y - 1);
				break;
			case 2: // baixo
				posicaoValida = moverDragao(dragao_x, dragao_y + 1);
				break;
			case 3: // esquerda
				posicaoValida = moverDragao(dragao_x - 1, dragao_y);
				break;
			case 4: // direita
				posicaoValida = moverDragao(dragao_x + 1, dragao_y);
				break;
			}
		} while (!posicaoValida);
	}

	private static boolean moverDragao(int x, int y) {
		if (!isParede(x, y))
		{
			dragao_x = x;
			dragao_y = y;
			return true;
		}
		return false;
	}

	private static boolean moverHeroi(int x, int y) {
		if(!isParede(x, y) || (x == saida_x && y == saida_y && !dragao))
		{
			heroi_x=x;
			heroi_y=y;
			return true;
		}
		return false;
	}
	private static boolean isParede(int x, int y) {
		return matrixCoord(x, y) == parede_char;
	}
	private static char matrixCoord(int x, int y)
	{
		return matrix[y][x];
	}

	private static boolean combateDragao()
	{
		if(verificaAdjacentes(heroi_x, heroi_y, dragao_x, dragao_y) && dragao)
		{
			if(heroi_armado)
				dragao = false;
			else
				return false;
		}
		return true;
	}

	private static boolean verificaAdjacentes(int x1, int y1, int x2, int y2)
	{
		if(x1 == x2)
		{
			if(Math.abs(y2-y1)==1)
				return true;
		}
		else if(y1 == y2)
		{
			if(Math.abs(x2-x1)==1)
				return true;
		}

		return false;
	}
	private static void generateMap(int side) {

		matrix = new char[side][side];
		fillMap(side);
		generateMapExit(side);
		int start_x, start_y;
		if (saida_x == 0)
		{
			start_x = 1;
			start_y = saida_y;
		}
		else if (saida_y == 0)
		{
			start_x = saida_x;
			start_y = 1;
		}
		else if (saida_x == side - 1)
		{
			start_x = side - ((side % 2 == 0) ? 3 : 2);
			start_y = saida_y;
		}
		else
		{
			start_x = saida_x;
			start_y = side - ((side % 2 == 0) ? 3 : 2);
		}
		matrix[start_y][start_x] = ' ';
		generateMapWalls(side, start_x, start_y);

		generateMapFixEven(side);
		
		generateMapElements();

	}
	private static void generateMapFixEven(int side) {
		// Fix for even side
		if (side % 2 == 0)
		{
			Random r = new Random();
			int n;

			// Duplicate column
			n = r.nextInt(side - 4) + 2;
			for (int x = side - 2; x > n; x--) {
				for (int y = 0; y < side; y++) {
					matrix[y][x] = matrix[y][x - 1];
				}
			}
			for (int y = 0; y < side; y++) {
				if (matrix[y][n - 1] != matrix[y][n + 1])
					matrix[y][n] = parede_char;
			}

			// Correct exit position
			if (saida_x < side - 1 && n <= saida_x)
				saida_x++;

			// Duplicate line
			n = r.nextInt(side - 4) + 2;
			for (int y = side - 2; y > n; y--) {
				for (int x = 0; x < side; x++) {
					matrix[y][x] = matrix[y - 1][x];
				}
			}
			for (int x = 0; x < side; x++) {
				if (matrix[n - 1][x] != matrix[n + 1][x])
					matrix[n][x] = parede_char;
			}
			// Correct exit position
			if (saida_y < side - 1 && n <= saida_y)
				saida_y++;
		}
	}

	private static void generateMapExit(int side) {
		Random r = new Random();
		int n = 2 * r.nextInt(side / 2 - 2) + 1;
		switch(r.nextInt(4))
		{
		case 0:
			saida_y = 0;
			saida_x = n;
			break;
		case 1:
			saida_y = side - 1;
			saida_x = n;
			break;
		case 2:
			saida_x = 0;
			saida_y = n;
			break;
		case 3:
			saida_x = side - 1;
			saida_y = n;
			break;
		}
	}

	private static void fillMap(int side) {
		for (int y = 0; y < side; y++) {
			for (int x = 0; x < side; x++) {
				matrix[x][y] = parede_char;
			}
		}
	}

	private static void generateMapWalls(int side, int x, int y) {
		int[] directions = {0, 1, 2, 3};
		directions = randomizeArray(directions);
		for (int i = 0; i < directions.length; i++) {
			switch (directions[i]) {
			case 0: // cima
				if (removeMapWall(side, x, y, x, y - 2))
					generateMapWalls(side, x, y - 2);
				break;
			case 1: // baixo
				if (removeMapWall(side, x, y, x, y + 2))
					generateMapWalls(side, x, y + 2);
				break;
			case 2: // esquerda
				if (removeMapWall(side, x, y, x - 2, y))
					generateMapWalls(side, x - 2, y);
				break;
			case 3: // direita
				if (removeMapWall(side, x, y, x + 2, y))
					generateMapWalls(side, x + 2, y);
				break;
			}
		}
	}

	private static boolean removeMapWall(int side, int x, int y, int new_x, int new_y) {
		if (new_x > 0 && new_x < side - 1 && new_y > 0 && new_y < side - 1 && matrix[new_y][new_x] == parede_char)
		{
			matrix[new_y][new_x] = ' ';
			matrix[(new_y + y) / 2][(new_x + x) / 2] = ' ';
			return true;
		}
		return false;
	}

	private static int[] randomizeArray(int[] array) {
		Random r = new Random();
		for (int i = 0; i < array.length; i++) {
			int n = r.nextInt(array.length);
			int val = array[i];
			array[i] = array[n];
			array[n] = val;
		}
		return array;
	}

	private static void generateMapElements()
	{
		generatePosHeroi();
		generatePosEspada();
		generatePosDragao();
		
	}

	private static void generatePosHeroi() {
		Random rand = new Random();
		
		int randX, randY;
		
		do
		{
			randX = rand.nextInt(MAP_SIDE-2) + 1;
			randY = rand.nextInt(MAP_SIDE-2) + 1;			
		} while(isParede(randX, randY) || randX >= MAP_SIDE || randY >= MAP_SIDE);
		
		heroi_x = randX;
		heroi_y = randY;
	}
	
	private static void generatePosEspada() {
		Random rand = new Random();
		
		int randX, randY;
		
		do
		{
			randX = rand.nextInt(MAP_SIDE-2) + 1;
			randY= rand.nextInt(MAP_SIDE-2) + 1;			
		} while(isParede(randX, randY) || coordDistSquare(randX, randY, heroi_x, heroi_y) < MIN_ELEM_DIST || randX >= MAP_SIDE || randY >= MAP_SIDE );
		
		espada_x = randX;
		espada_y = randY;
	}
	
	private static void generatePosDragao() {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(MAP_SIDE-2) + 1;
			randY= rand.nextInt(MAP_SIDE-2) + 1;			
		} while(isParede(randX, randY) || coordDistSquare(randX, randY, heroi_x, heroi_y) < MIN_ELEM_DIST || randX >= MAP_SIDE || randY >= MAP_SIDE );

		dragao_x = randX;
		dragao_y = randY;
	}
	
	private static int coordDistSquare(int x1, int y1, int x2, int y2)
	{
		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	}
}